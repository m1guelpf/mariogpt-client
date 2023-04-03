package cloud.antony.mariogptclient.engine.gpt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class MarioGptHttpGenerator implements AutoCloseable {

    private static final String REPLICATE_VERSION = "99b5f28ac6443d6b9154d00568c5e7de9c6e1d5afe330235ed9e5aae4ef3e93e";

    private static final short GPT_SIZE = 1000;

    // repeated scheduled tasks can be stopped from inside by throwing an error
    //  there can be a more elegant solution, but this is good enough for now
    //  errors with this as it's message can be treated differently because they're only used for stopping a scheduler
    private static final String SCHEDULER_STOP_ERROR_MESSAGE = "__stop_scheduler";

    private final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();

    private final String apiUrl;
    private final String apiKey;
    private final String prompt;

    private final CompletableFuture<Void> playableFuture = new CompletableFuture<>();
    private final List<Consumer<String[]>> columnUpdateListeners = new ArrayList<>();

    private boolean running = false;
    private boolean canContinue = true;
    private String[] lastResponse = new String[0];
    private String[] fullResponse = new String[0];

    private String statusUrl;

    public MarioGptHttpGenerator(String apiUrl, String apiKey, String prompt) {
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.prompt = prompt;
    }

    public CompletableFuture<Void> waitForPlayable() {
        return this.playableFuture;
    }

    public void registerColumnListener(Consumer<String[]> listener) {
        columnUpdateListeners.add(listener);
    }

    public void continueGeneration() {
        if (running || !canContinue)
            return;

        running = true;
        scheduledExecutor.scheduleWithFixedDelay(() -> {
            try {
                if (statusUrl == null) {
                    Optional<String> maybeStatusUrl = this.makeInitialRequest(
                            prompt,
                            lastResponse.length == 0 ? null : String.join("", lastResponse),
                            GPT_SIZE);
                    if (!maybeStatusUrl.isPresent()) {
                        System.out.println("Failed to get level data from AI");
                        System.exit(1);
                        return;
                    }

                    this.statusUrl = maybeStatusUrl.get();
                    System.out.println("Started new GPT job: " + this.statusUrl);
                    return;
                }

                Optional<PredictionStatus> maybeStatus = getPredictionStatus(this.statusUrl);
                if (!maybeStatus.isPresent()) {
                    this.canContinue = false;
                    System.out.println("Something failed with getting the status. Game is still playable, but generation will not continue!");
                    throw new RuntimeException(SCHEDULER_STOP_ERROR_MESSAGE);
                }

                PredictionStatus status = maybeStatus.get();
                String[] cols = status.getCols();

                for (int i = 0; i < cols.length; i++)
                    cols[i] = cols[i].charAt(13) + cols[i].substring(0, 13);

                if (status.getStatus().equals("starting")) {
                    System.out.println(System.currentTimeMillis() + " -> " + "model starting");
                }

                int previousLength = this.fullResponse.length;
                this.fullResponse = status.getCols();

                if (previousLength != cols.length) {
                    System.out.println("Generated " + cols.length + "/" + GPT_SIZE);
                    if (this.fullResponse.length >= 20)
                        this.playableFuture.complete(null);

                    this.columnUpdateListeners.forEach(it -> it.accept(this.fullResponse));
                }

                if (status.getStatus().equals("succeeded")) {
                    this.lastResponse = cols;
                    System.out.println("Generation done for " + this.statusUrl);
                    this.statusUrl = null;
                    throw new RuntimeException(SCHEDULER_STOP_ERROR_MESSAGE);
                }
            } catch (Exception e) {
                if (!SCHEDULER_STOP_ERROR_MESSAGE.equals(e.getMessage()))
                    e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    // will give the status url we can ping to get response
    private Optional<String> makeInitialRequest(String prompt, String seed, short size) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "Mario-GPT/1.0.0");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Token " + this.apiKey);
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(2000);
            connection.setDoOutput(true);

            OutputStream outputStream = connection.getOutputStream();
            OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
            streamWriter.write("{" +
                    "\"version\":\"" + REPLICATE_VERSION + "\"," +
                    "\"input\":" +
                        "{" +
                            "\"prompt\":\"" + prompt + "\"," +
                            (seed == null || seed.length() == 0 ? "" : "\"seed\":\"" + seed + "\",") +
                            "\"size\":" + size +
                        "}" +
                    "}");
            streamWriter.flush();
            streamWriter.close();
            outputStream.close();

            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_CREATED)
                return Optional.empty();

            JsonNode node = new ObjectMapper().readTree(connection.getInputStream());

            return Optional.ofNullable(node.get("urls").get("get").asText());
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private Optional<PredictionStatus> getPredictionStatus(String predictionUrl) {
        try {
            URL url = new URL(predictionUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mario-GPT/1.0.0");
            connection.setRequestProperty("Authorization", "Token " + this.apiKey);
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(2000);
            connection.setDoOutput(true);

            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                return Optional.empty();

            JsonNode node = new ObjectMapper().readTree(connection.getInputStream());

            String status = node.get("status").asText();

            List<String> cols = new ArrayList<>();
            JsonNode outputNode = node.get("output");

            if (outputNode.isArray()) {
                for (JsonNode arrayNode : outputNode)
                    cols.add(arrayNode.asText());
            }

            return Optional.of(new PredictionStatus(status, cols.toArray(new String[0])));
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public String[] getFullResponse() {
        return fullResponse;
    }

    @Override
    public void close() throws Exception {
        this.scheduledExecutor.shutdownNow();
    }

    private static class PredictionStatus {

        private final String status;
        private final String[] cols;

        public PredictionStatus(String status, String[] cols) {
            this.status = status;
            this.cols = cols;
        }

        public String getStatus() {
            return status;
        }

        public String[] getCols() {
            return cols;
        }

    }

}
