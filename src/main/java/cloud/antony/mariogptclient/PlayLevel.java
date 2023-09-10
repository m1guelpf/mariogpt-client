package cloud.antony.mariogptclient;

import cloud.antony.mariogptclient.agents.human.Agent;
import cloud.antony.mariogptclient.engine.core.MarioGame;

import java.util.Scanner;

import cloud.antony.mariogptclient.engine.core.MarioResult;
import cloud.antony.mariogptclient.engine.gpt.MarioGptHttpGenerator;

public class PlayLevel
{

    private static final String DEFAULT_PROMPT = "unbroken floor, no pipes, some blocks, no enemies, a lot of coins";

    public static void printResults(final MarioResult marioResult) {
        System.out.println("****************************************************************");
        System.out.println("Game Status: " + marioResult.getGameStatus().toString() + " Percentage Completion: " + marioResult.getCompletionPercentage());
        System.out.println("Lives: " + marioResult.getCurrentLives() + " Coins: " + marioResult.getCurrentCoins() + " Remaining Time: " + (int)Math.ceil(marioResult.getRemainingTime() / 1000.0f));
        System.out.println("Mario State: " + marioResult.getMarioMode() + " (Mushrooms: " + marioResult.getNumCollectedMushrooms() + " Fire Flowers: " + marioResult.getNumCollectedFireflower() + ")");
        System.out.println("Total Kills: " + marioResult.getKillsTotal() + " (Stomps: " + marioResult.getKillsByStomp() + " Fireballs: " + marioResult.getKillsByFire() + " Shells: " + marioResult.getKillsByShell() + " Falls: " + marioResult.getKillsByFall() + ")");
        System.out.println("Bricks: " + marioResult.getNumDestroyedBricks() + " Jumps: " + marioResult.getNumJumps() + " Max X Jump: " + marioResult.getMaxXJump() + " Max Air Time: " + marioResult.getMaxJumpAirTime());
        System.out.println("****************************************************************");
    }
    
    public static void playLevel(final MarioGame marioGame, MarioGptHttpGenerator gptGenerator) {
        System.out.println("Running Interactive Play!");
        System.out.println("=========================");
        printResults(marioGame.playInteractive(new Agent(), gptGenerator, 200, 999));
    }
    
    public static void playLevel(final MarioGame marioGame, MarioGptHttpGenerator gptGenerator, final String s2) {
        System.out.println("Running Interactive Play!");
        System.out.println("=========================");
        printResults(marioGame.playInteractive(new Agent(), gptGenerator, 200, 999, s2));
    }
    
    public static void main(final String[] array) throws Exception {
        String prompt = DEFAULT_PROMPT;
        Scanner scanner = new Scanner(System.in);
        String apiUrl = "https://api.replicate.com/v1/predictions";

        System.out.println("Api key:");
        String apiKey = scanner.nextLine();

        System.out.println("Generating with prompt: " + DEFAULT_PROMPT);
        System.out.println("Waiting for base level");
        MarioGptHttpGenerator gptGenerator = new MarioGptHttpGenerator(apiUrl, apiKey, prompt);
        gptGenerator.continueGeneration();
        gptGenerator.waitForPlayable().join();

        final MarioGame marioGame = new MarioGame();
        if (array.length == 1) {
            playLevel(marioGame, gptGenerator, array[0]);
        }
        else {
            playLevel(marioGame, gptGenerator);
        }

        gptGenerator.close();
    }
}
