package cloud.antony.mariogptclient;

import cloud.antony.mariogptclient.agents.robinBaumgarten.Agent;
import cloud.antony.mariogptclient.engine.core.MarioGame;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import cloud.antony.mariogptclient.engine.core.MarioResult;

public class PlayAstar
{
    public static void printResults(final MarioResult marioResult) {
        System.out.println("****************************************************************");
        System.out.println("Game Status: " + marioResult.getGameStatus().toString() + " Percentage Completion: " + marioResult.getCompletionPercentage());
        System.out.println("Lives: " + marioResult.getCurrentLives() + " Coins: " + marioResult.getCurrentCoins() + " Remaining Time: " + (int)Math.ceil(marioResult.getRemainingTime() / 1000.0f));
        System.out.println("Mario State: " + marioResult.getMarioMode() + " (Mushrooms: " + marioResult.getNumCollectedMushrooms() + " Fire Flowers: " + marioResult.getNumCollectedFireflower() + ")");
        System.out.println("Total Kills: " + marioResult.getKillsTotal() + " (Stomps: " + marioResult.getKillsByStomp() + " Fireballs: " + marioResult.getKillsByFire() + " Shells: " + marioResult.getKillsByShell() + " Falls: " + marioResult.getKillsByFall() + ")");
        System.out.println("Bricks: " + marioResult.getNumDestroyedBricks() + " Jumps: " + marioResult.getNumJumps() + " Max X Jump: " + marioResult.getMaxXJump() + " Max Air Time: " + marioResult.getMaxJumpAirTime());
        System.out.println("****************************************************************");
    }
    
    public static String getLevel(final String first) {
        String s = "";
        try {
            s = new String(Files.readAllBytes(Paths.get(first, new String[0])));
        }
        catch (IOException ex) {}
        return s;
    }
    
    public static void playAstar(final MarioGame marioGame, final String s, final boolean b) {
        System.out.println("Running Interactive Play!");
        System.out.println("=========================");
        printResults(marioGame.playAstar(new Agent(), getLevel(s), 200, b));
    }
    
    public static void playAstar(final MarioGame marioGame, final String s, final boolean b, final String s2) {
        System.out.println("Running Interactive Play!");
        System.out.println("=========================");
        printResults(marioGame.playAstar(new Agent(), getLevel(s), 200, b, s2));
    }
    
    public static void main(final String[] array) {
        final MarioGame marioGame = new MarioGame();
        final String s = array[0];
        final String s2 = array[1];
        boolean b = false;
        if (array[1].equals("cloud/antony/mariogptclient/agents/human")) {
            b = true;
        }
        if (array.length > 2) {
            playAstar(marioGame, s, b, array[2]);
        }
        else {
            playAstar(marioGame, s, b);
        }
    }
}
