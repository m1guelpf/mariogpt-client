package cloud.antony.mariogptclient.engine.core;

import cloud.antony.mariogptclient.engine.helper.SpriteType;
import java.util.Iterator;
import cloud.antony.mariogptclient.engine.helper.EventType;
import cloud.antony.mariogptclient.engine.helper.GameStatus;
import java.util.ArrayList;

public class MarioResult
{
    private MarioWorld world;
    private ArrayList<MarioEvent> gameEvents;
    private ArrayList<MarioAgentEvent> agentEvents;
    
    public MarioResult(final MarioWorld world, final ArrayList<MarioEvent> gameEvents, final ArrayList<MarioAgentEvent> agentEvents) {
        this.world = world;
        this.gameEvents = gameEvents;
        this.agentEvents = agentEvents;
    }
    
    public GameStatus getGameStatus() {
        return this.world.gameStatus;
    }
    
    public float getCompletionPercentage() {
        return this.world.mario.x / (this.world.level.exitTileX * 16);
    }
    
    public int getRemainingTime() {
        return this.world.currentTimer;
    }
    
    public int getMarioMode() {
        int n = 0;
        if (this.world.mario.isLarge) {
            n = 1;
        }
        if (this.world.mario.isFire) {
            n = 2;
        }
        return n;
    }
    
    public ArrayList<MarioEvent> getGameEvents() {
        return this.gameEvents;
    }
    
    public ArrayList<MarioAgentEvent> getAgentEvents() {
        return this.agentEvents;
    }
    
    public int getKillsTotal() {
        int n = 0;
        for (final MarioEvent marioEvent : this.gameEvents) {
            if (marioEvent.getEventType() == EventType.STOMP_KILL.getValue() || marioEvent.getEventType() == EventType.FIRE_KILL.getValue() || marioEvent.getEventType() == EventType.FALL_KILL.getValue() || marioEvent.getEventType() == EventType.SHELL_KILL.getValue()) {
                ++n;
            }
        }
        return n;
    }
    
    public int getKillsByFire() {
        int n = 0;
        final Iterator<MarioEvent> iterator = this.gameEvents.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getEventType() == EventType.FIRE_KILL.getValue()) {
                ++n;
            }
        }
        return n;
    }
    
    public int getKillsByStomp() {
        int n = 0;
        final Iterator<MarioEvent> iterator = this.gameEvents.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getEventType() == EventType.STOMP_KILL.getValue()) {
                ++n;
            }
        }
        return n;
    }
    
    public int getKillsByShell() {
        int n = 0;
        final Iterator<MarioEvent> iterator = this.gameEvents.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getEventType() == EventType.SHELL_KILL.getValue()) {
                ++n;
            }
        }
        return n;
    }
    
    public int getMarioNumKills(final int n) {
        int n2 = 0;
        for (final MarioEvent marioEvent : this.gameEvents) {
            if ((marioEvent.getEventType() == EventType.SHELL_KILL.getValue() || marioEvent.getEventType() == EventType.FIRE_KILL.getValue() || marioEvent.getEventType() == EventType.STOMP_KILL.getValue()) && marioEvent.getEventParam() == n) {
                ++n2;
            }
        }
        return n2;
    }
    
    public int getMarioNumHurts() {
        int n = 0;
        final Iterator<MarioEvent> iterator = this.gameEvents.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getEventType() == EventType.HURT.getValue()) {
                ++n;
            }
        }
        return n;
    }
    
    public int getNumBumpQuestionBlock() {
        int n = 0;
        for (final MarioEvent marioEvent : this.gameEvents) {
            if (marioEvent.getEventType() == EventType.BUMP.getValue() && marioEvent.getEventParam() == 24) {
                ++n;
            }
        }
        return n;
    }
    
    public int getNumBumpBrick() {
        int n = 0;
        for (final MarioEvent marioEvent : this.gameEvents) {
            if (marioEvent.getEventType() == EventType.BUMP.getValue() && marioEvent.getEventParam() == 22) {
                ++n;
            }
        }
        return n;
    }
    
    public int getKillsByFall() {
        int n = 0;
        final Iterator<MarioEvent> iterator = this.gameEvents.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getEventType() == EventType.FALL_KILL.getValue()) {
                ++n;
            }
        }
        return n;
    }
    
    public int getNumJumps() {
        int n = 0;
        final Iterator<MarioEvent> iterator = this.gameEvents.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getEventType() == EventType.JUMP.getValue()) {
                ++n;
            }
        }
        return n;
    }
    
    public float getMaxXJump() {
        float abs = 0.0f;
        float marioX = -100.0f;
        for (final MarioEvent marioEvent : this.gameEvents) {
            if (marioEvent.getEventType() == EventType.JUMP.getValue()) {
                marioX = marioEvent.getMarioX();
            }
            if (marioEvent.getEventType() == EventType.LAND.getValue() && Math.abs(marioEvent.getMarioX() - marioX) > abs) {
                abs = Math.abs(marioEvent.getMarioX() - marioX);
            }
        }
        return abs;
    }
    
    public int getMaxJumpAirTime() {
        int n = 0;
        int time = -100;
        for (final MarioEvent marioEvent : this.gameEvents) {
            if (marioEvent.getEventType() == EventType.JUMP.getValue()) {
                time = marioEvent.getTime();
            }
            if (marioEvent.getEventType() == EventType.LAND.getValue() && marioEvent.getTime() - time > n) {
                n = marioEvent.getTime() - time;
            }
        }
        return n;
    }
    
    public int getCurrentLives() {
        return this.world.lives;
    }
    
    public int getCurrentCoins() {
        return this.world.coins;
    }
    
    public int getNumCollectedMushrooms() {
        int n = 0;
        for (final MarioEvent marioEvent : this.gameEvents) {
            if (marioEvent.getEventType() == EventType.COLLECT.getValue() && marioEvent.getEventParam() == SpriteType.MUSHROOM.getValue()) {
                ++n;
            }
        }
        return n;
    }
    
    public int getNumCollectedFireflower() {
        int n = 0;
        for (final MarioEvent marioEvent : this.gameEvents) {
            if (marioEvent.getEventType() == EventType.COLLECT.getValue() && marioEvent.getEventParam() == SpriteType.FIRE_FLOWER.getValue()) {
                ++n;
            }
        }
        return n;
    }
    
    public int getNumCollectedTileCoins() {
        int n = 0;
        for (final MarioEvent marioEvent : this.gameEvents) {
            if (marioEvent.getEventType() == EventType.COLLECT.getValue() && marioEvent.getEventParam() == 31) {
                ++n;
            }
        }
        return n;
    }
    
    public int getNumDestroyedBricks() {
        int n = 0;
        for (final MarioEvent marioEvent : this.gameEvents) {
            if (marioEvent.getEventType() == EventType.BUMP.getValue() && marioEvent.getEventParam() == 22 && marioEvent.getMarioState() > 0) {
                ++n;
            }
        }
        return n;
    }
}
