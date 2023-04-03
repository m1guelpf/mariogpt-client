package cloud.antony.mariogptclient.engine.core;

import java.awt.Graphics;
import java.awt.image.VolatileImage;
import java.util.ArrayList;

import cloud.antony.mariogptclient.agents.robinBaumgarten.Agent;
import cloud.antony.mariogptclient.engine.gpt.MarioGptHttpGenerator;
import cloud.antony.mariogptclient.engine.helper.MarioActions;
import cloud.antony.mariogptclient.engine.helper.GameStatus;

import java.awt.event.KeyListener;
import java.awt.event.KeyAdapter;
import javax.swing.JFrame;

public class MarioGame
{
    public static final long maxTime = 40L;
    public static final long graceTime = 10L;
    public static final int width = 256;
    public static final int height = 256;
    public static final int tileWidth = 16;
    public static final int tileHeight = 16;
    public static final boolean verbose = false;
    public boolean pause;
    private MarioEvent[] killEvents;
    private JFrame window;
    private MarioRender render;
    private MarioAgent agent;
    private MarioWorld world;
    
    public MarioGame() {
        this.pause = false;
        this.window = null;
        this.render = null;
        this.agent = null;
        this.world = null;
    }
    
    public MarioGame(final MarioEvent[] killEvents) {
        this.pause = false;
        this.window = null;
        this.render = null;
        this.agent = null;
        this.world = null;
        this.killEvents = killEvents;
    }
    
    private int getDelay(final int n) {
        if (n <= 0) {
            return 0;
        }
        return 1000 / n;
    }
    
    private void setAgent(final MarioAgent agent) {
        this.agent = agent;
        if (agent instanceof KeyAdapter) {
            this.render.addKeyListener((KeyListener)this.agent);
        }
    }
    
    public MarioResult playInteractive(final cloud.antony.mariogptclient.agents.human.Agent agent, MarioGptHttpGenerator gptGenerator, final int n, final int n2) {
        return this.playInteractive(agent, gptGenerator, n, n2, null);
    }
    
    public MarioResult playInteractive(final cloud.antony.mariogptclient.agents.human.Agent agent, MarioGptHttpGenerator gptGenerator, final int n, int n2, final String s2) {
        this.window = new JFrame("Mario AI Framework");
        this.render = new MarioRender(2.0f);
        this.window.setContentPane(this.render);
        this.window.pack();
        this.window.setResizable(false);
        this.window.setDefaultCloseOperation(3);
        if (s2 != null && !s2.isEmpty()) {
            this.render.init(s2);
        }
        else {
            this.render.init();
        }
        this.window.setVisible(true);
        this.setAgent(agent);
        MarioResult gameLoop = null;
        for (int i = 0; i < n2; --n2, ++i) {
            this.world = new MarioWorld(this.killEvents, n2);
            gameLoop = this.gameLoop(gptGenerator, n, 1, true, 30, n2, this.world);
            if (gameLoop.getGameStatus() == GameStatus.WIN) {
                break;
            }
        }
        this.window.dispose();
        this.render = null;
        this.world = null;
        return gameLoop;
    }
    
    public MarioResult playAstar(final Agent agent, final String s, final int n, final boolean b) {
        return this.playAstar(agent, s, n, b, null);
    }
    
    public MarioResult playAstar(final Agent agent, final String s, final int n, final boolean b, final String s2) {
        if (b) {
            this.window = new JFrame("Mario AI Framework");
            this.render = new MarioRender(2.0f);
            this.window.setContentPane(this.render);
            this.window.pack();
            this.window.setResizable(false);
            this.window.setDefaultCloseOperation(3);
            if (s2 != null && !s2.isEmpty()) {
                this.render.init(s2);
            }
            else {
                this.render.init();
            }
            this.window.setVisible(true);
        }
        this.setAgent(agent);
        this.world = new MarioWorld(this.killEvents, 1);
        final MarioResult gameLoop = this.gameLoop(n, 1, b, 30, 1, this.world);
        if (b) {
            this.window.dispose();
            this.render = null;
            this.world = null;
        }
        return gameLoop;
    }

    private MarioResult gameLoop(final int n, final int n2, final boolean b, final int n3, final int n4, final MarioWorld marioWorld) {
        throw new IllegalStateException("loop without GPT generator not supported in this version");
    }

    private MarioResult gameLoop(MarioGptHttpGenerator gptGenerator, final int n, final int n2, final boolean b, final int n3) {
        return this.gameLoop(gptGenerator, n, n2, b, n3, 1);
    }
    
    private MarioWorld initializeWorld(MarioGptHttpGenerator gptGenerator, final int n, final int n2, final boolean visuals, final int n3, final int n4, final MarioWorld marioWorld) {
        marioWorld.visuals = visuals;
        marioWorld.initializeLevel(gptGenerator, 1000 * n);
        if (visuals) {
            marioWorld.initializeVisuals(this.render.getGraphicsConfiguration());
        }
        marioWorld.mario.isLarge = (n2 > 0);
        marioWorld.mario.isFire = (n2 > 1);
        marioWorld.update(new boolean[MarioActions.numberOfActions()]);
        return marioWorld;
    }
    
    private MarioResult gameLoop(MarioGptHttpGenerator gptGenerator, final int n, final int n2, final boolean b, final int n3, final int n4) {
        this.world = new MarioWorld(this.killEvents, n4);
        return this.gameLoop(gptGenerator, n, n2, b, n3, n4, this.world);
    }
    
    private MarioResult gameLoop(MarioGptHttpGenerator gptGenerator, final int n, final int n2, final boolean b, final int n3, final int n4, final MarioWorld marioWorld) {
        long currentTimeMillis = System.currentTimeMillis();
        this.world = this.initializeWorld(gptGenerator, n, n2, b, n3, n4, marioWorld);
        VolatileImage volatileImage = null;
        Graphics graphics = null;
        Graphics graphics2 = null;
        if (b) {
            volatileImage = this.render.createVolatileImage(256, 256);
            graphics = this.render.getGraphics();
            graphics2 = volatileImage.getGraphics();
            this.render.addFocusListener(this.render);
        }
        this.agent.initialize(new MarioForwardModel(this.world.clone()), new MarioTimer(40L));
        final ArrayList<MarioEvent> list = new ArrayList<MarioEvent>();
        final ArrayList<MarioAgentEvent> list2 = new ArrayList<MarioAgentEvent>();
        while (this.world.gameStatus == GameStatus.RUNNING) {
            if (!this.pause) {
                final boolean[] actions = this.agent.getActions(new MarioForwardModel(this.world.clone()), new MarioTimer(40L));
                this.world.update(actions);
                list.addAll(this.world.lastFrameEvents);
                list2.add(new MarioAgentEvent(actions, this.world.mario.x, this.world.mario.y, (this.world.mario.isLarge || this.world.mario.isFire) ? 1 : 0, this.world.mario.onGround, this.world.currentTick));
            }
            if (b) {
                this.render.renderWorld(this.world, volatileImage, graphics, graphics2);
            }
            if (this.getDelay(n3) > 0) {
                try {
                    currentTimeMillis += this.getDelay(n3);
                    Thread.sleep(Math.max(0L, currentTimeMillis - System.currentTimeMillis()));
                    continue;
                }
                catch (InterruptedException ex) {}
                break;
            }
        }
        return new MarioResult(this.world, list, list2);
    }
}
