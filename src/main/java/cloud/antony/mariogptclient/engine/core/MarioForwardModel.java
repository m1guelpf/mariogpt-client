package cloud.antony.mariogptclient.engine.core;

import java.util.ArrayList;
import java.util.Objects;

import cloud.antony.mariogptclient.engine.helper.GameStatus;
import cloud.antony.mariogptclient.engine.helper.EventType;
import cloud.antony.mariogptclient.engine.helper.SpriteType;

public class MarioForwardModel
{
    private static final int OBS_SCENE_SHIFT = 16;
    public static final int OBS_NONE = 0;
    public static final int OBS_UNDEF = -42;
    public static final int OBS_SOLID = 17;
    public static final int OBS_BRICK = 22;
    public static final int OBS_QUESTION_BLOCK = 24;
    public static final int OBS_COIN = 31;
    public static final int OBS_PYRAMID_SOLID = 18;
    public static final int OBS_PIPE_BODY_RIGHT = 37;
    public static final int OBS_PIPE_BODY_LEFT = 36;
    public static final int OBS_PIPE_TOP_RIGHT = 35;
    public static final int OBS_PIPE_TOP_LEFT = 34;
    public static final int OBS_USED_BLOCK = 30;
    public static final int OBS_BULLET_BILL_BODY = 21;
    public static final int OBS_BULLET_BILL_NECT = 20;
    public static final int OBS_BULLET_BILL_HEAD = 19;
    public static final int OBS_BACKGROUND = 63;
    public static final int OBS_PLATFORM_SINGLE = 59;
    public static final int OBS_PLATFORM_LEFT = 60;
    public static final int OBS_PLATFORM_RIGHT = 61;
    public static final int OBS_PLATFORM_CENTER = 62;
    public static final int OBS_PLATFORM = 59;
    public static final int OBS_CANNON = 19;
    public static final int OBS_PIPE = 34;
    public static final int OBS_SCENE_OBJECT = 100;
    public static final int OBS_FIREBALL = 16;
    public static final int OBS_GOOMBA = 2;
    public static final int OBS_GOOMBA_WINGED = 3;
    public static final int OBS_RED_KOOPA = 4;
    public static final int OBS_RED_KOOPA_WINGED = 5;
    public static final int OBS_GREEN_KOOPA = 6;
    public static final int OBS_GREEN_KOOPA_WINGED = 7;
    public static final int OBS_SPIKY = 8;
    public static final int OBS_SPIKY_WINGED = 9;
    public static final int OBS_BULLET_BILL = 10;
    public static final int OBS_ENEMY_FLOWER = 11;
    public static final int OBS_MUSHROOM = 12;
    public static final int OBS_FIRE_FLOWER = 13;
    public static final int OBS_SHELL = 14;
    public static final int OBS_LIFE_MUSHROOM = 15;
    public static final int OBS_STOMPABLE_ENEMY = 2;
    public static final int OBS_NONSTOMPABLE_ENEMY = 8;
    public static final int OBS_SPECIAL_ITEM = 12;
    public static final int OBS_ENEMY = 1;
    public final int obsGridWidth = 16;
    public final int obsGridHeight = 16;
    private MarioWorld world;
    private int fallKill;
    private int stompKill;
    private int fireKill;
    private int shellKill;
    private int mushrooms;
    private int flowers;
    private int breakBlock;
    
    public static int getSpriteTypeGeneralization(final SpriteType spriteType, final int n) {
        switch (n) {
            case 0: {
                if (Objects.requireNonNull(spriteType) == SpriteType.MARIO) {
                    return 0;
                }
            }
            case 1: {
                switch (spriteType) {
                    case FIREBALL: {
                        return 16;
                    }
                    case MUSHROOM:
                    case LIFE_MUSHROOM:
                    case FIRE_FLOWER: {
                        return 12;
                    }
                    case BULLET_BILL:
                    case SHELL:
                    case GOOMBA:
                    case GOOMBA_WINGED:
                    case GREEN_KOOPA:
                    case GREEN_KOOPA_WINGED:
                    case RED_KOOPA:
                    case RED_KOOPA_WINGED: {
                        return 2;
                    }
                    case SPIKY:
                    case SPIKY_WINGED:
                    case ENEMY_FLOWER: {
                        return 8;
                    }
                    default: {
                        return 0;
                    }
                }
            }
            case 2: {
                switch (spriteType) {
                    case MARIO:
                    case FIREBALL:
                    case MUSHROOM:
                    case LIFE_MUSHROOM:
                    case FIRE_FLOWER: {
                        return 0;
                    }
                    default: {
                        return 1;
                    }
                }
            }
            default: {
                return -42;
            }
        }
    }
    
    public static int getBlockValueGeneralization(final int n, final int n2) {
        if (n == 0) {
            return 0;
        }
        switch (n2) {
            case 0: {
                switch (n) {
                    case 48:
                    case 49: {
                        return 0;
                    }
                    case 6:
                    case 7:
                    case 50:
                    case 51: {
                        return 22;
                    }
                    case 8:
                    case 11: {
                        return 24;
                    }
                    default: {
                        return n + 16;
                    }
                }
            }
            case 1: {
                switch (n) {
                    case 47:
                    case 48:
                    case 49: {
                        return 0;
                    }
                    case 1:
                    case 2:
                    case 14: {
                        return 17;
                    }
                    case 3:
                    case 4:
                    case 5: {
                        return 19;
                    }
                    case 18:
                    case 19:
                    case 20:
                    case 21: {
                        return 34;
                    }
                    case 6:
                    case 7:
                    case 50:
                    case 51: {
                        return 22;
                    }
                    case 8:
                    case 11: {
                        return 24;
                    }
                    case 15: {
                        return 31;
                    }
                    case 44:
                    case 45:
                    case 46: {
                        return 59;
                    }
                    default: {
                        return 0;
                    }
                }
            }
            case 2: {
                switch (n) {
                    case 47:
                    case 48:
                    case 49: {
                        return 0;
                    }
                    default: {
                        return 100;
                    }
                }
            }
            default: {
                return -42;
            }
        }
    }
    
    public MarioForwardModel(final MarioWorld world) {
        this.world = world;
    }
    
    public MarioForwardModel clone() {
        final MarioForwardModel marioForwardModel = new MarioForwardModel(this.world.clone());
        marioForwardModel.fallKill = this.fallKill;
        marioForwardModel.stompKill = this.stompKill;
        marioForwardModel.fireKill = this.fireKill;
        marioForwardModel.shellKill = this.shellKill;
        marioForwardModel.mushrooms = this.mushrooms;
        marioForwardModel.flowers = this.flowers;
        marioForwardModel.breakBlock = this.breakBlock;
        return marioForwardModel;
    }
    
    public void advance(final boolean[] array) {
        this.world.update(array);
        for (final MarioEvent marioEvent : this.world.lastFrameEvents) {
            if (marioEvent.getEventType() == EventType.FIRE_KILL.getValue()) {
                ++this.fireKill;
            }
            if (marioEvent.getEventType() == EventType.STOMP_KILL.getValue()) {
                ++this.stompKill;
            }
            if (marioEvent.getEventType() == EventType.FALL_KILL.getValue()) {
                ++this.fallKill;
            }
            if (marioEvent.getEventType() == EventType.SHELL_KILL.getValue()) {
                ++this.shellKill;
            }
            if (marioEvent.getEventType() == EventType.COLLECT.getValue()) {
                if (marioEvent.getEventParam() == SpriteType.FIRE_FLOWER.getValue()) {
                    ++this.flowers;
                }
                if (marioEvent.getEventParam() == SpriteType.MUSHROOM.getValue()) {
                    ++this.mushrooms;
                }
            }
            if (marioEvent.getEventType() == EventType.BUMP.getValue() && marioEvent.getEventParam() == 22 && marioEvent.getMarioState() > 0) {
                ++this.breakBlock;
            }
        }
    }
    
    public GameStatus getGameStatus() {
        return this.world.gameStatus;
    }
    
    public float getCompletionPercentage() {
        return this.world.mario.x / (this.world.level.exitTileX * 16);
    }
    
    public float[] getLevelFloatDimensions() {
        return new float[] { (float)this.world.level.width, (float)this.world.level.height };
    }
    
    public int getRemainingTime() {
        return this.world.currentTimer;
    }
    
    public float[] getMarioFloatPos() {
        return new float[] { this.world.mario.x, this.world.mario.y };
    }
    
    public float[] getMarioFloatVelocity() {
        return new float[] { this.world.mario.xa, this.world.mario.ya };
    }
    
    public boolean getMarioCanJumpHigher() {
        return this.world.mario.jumpTime > 0;
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
    
    public boolean isMarioOnGround() {
        return this.world.mario.onGround;
    }
    
    public boolean mayMarioJump() {
        return this.world.mario.mayJump;
    }
    
    public float[] getEnemiesFloatPos() {
        final ArrayList<MarioSprite> enemies = this.world.getEnemies();
        final float[] array = new float[enemies.size() * 3];
        for (int i = 0; i < enemies.size(); ++i) {
            array[3 * i] = (float)enemies.get(i).type.getValue();
            array[3 * i + 1] = enemies.get(i).x;
            array[3 * i + 2] = enemies.get(i).y;
        }
        return array;
    }
    
    public int getKillsTotal() {
        return this.fallKill + this.fireKill + this.shellKill + this.stompKill;
    }
    
    public int getKillsByFire() {
        return this.fireKill;
    }
    
    public int getKillsByStomp() {
        return this.stompKill;
    }
    
    public int getKillsByShell() {
        return this.shellKill;
    }
    
    public int getKillsByFall() {
        return this.fallKill;
    }
    
    public int getNumLives() {
        return this.world.lives;
    }
    
    public int getNumCollectedMushrooms() {
        return this.mushrooms;
    }
    
    public int getNumCollectedFireflower() {
        return this.flowers;
    }
    
    public int getNumCollectedCoins() {
        return this.world.coins;
    }
    
    public int getNumDestroyedBricks() {
        return this.breakBlock;
    }
    
    public int[] getMarioScreenTilePos() {
        return new int[] { (int)((this.world.mario.x - this.world.cameraX) / 16.0f), (int)(this.world.mario.y / 16.0f) };
    }
    
    public int[][] getScreenCompleteObservation() {
        return this.getScreenCompleteObservation(1, 0);
    }
    
    public int[][] getScreenEnemiesObservation() {
        return this.getScreenEnemiesObservation(0);
    }
    
    public int[][] getScreenSceneObservation() {
        return this.getScreenSceneObservation(1);
    }
    
    public int[][] getMarioCompleteObservation() {
        return this.getMarioCompleteObservation(1, 0);
    }
    
    public int[][] getMarioEnemiesObservation() {
        return this.getMarioEnemiesObservation(0);
    }
    
    public int[][] getMarioSceneObservation() {
        return this.getMarioSceneObservation(1);
    }
    
    public int[][] getScreenCompleteObservation(final int n, final int n2) {
        return this.world.getMergedObservation(this.world.cameraX + 128.0f, 128.0f, n, n2);
    }
    
    public int[][] getScreenEnemiesObservation(final int n) {
        return this.world.getEnemiesObservation(this.world.cameraX + 128.0f, 128.0f, n);
    }
    
    public int[][] getScreenSceneObservation(final int n) {
        return this.world.getSceneObservation(this.world.cameraX + 128.0f, 128.0f, n);
    }
    
    public int[][] getMarioCompleteObservation(final int n, final int n2) {
        return this.world.getMergedObservation(this.world.mario.x, this.world.mario.y, n, n2);
    }
    
    public int[][] getMarioEnemiesObservation(final int n) {
        return this.world.getEnemiesObservation(this.world.mario.x, this.world.mario.y, n);
    }
    
    public int[][] getMarioSceneObservation(final int n) {
        return this.world.getSceneObservation(this.world.mario.x, this.world.mario.y, n);
    }
}
