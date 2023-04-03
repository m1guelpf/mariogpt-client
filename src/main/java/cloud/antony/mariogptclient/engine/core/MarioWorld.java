package cloud.antony.mariogptclient.engine.core;

import java.awt.Graphics;
import cloud.antony.mariogptclient.engine.effects.BrickEffect;
import cloud.antony.mariogptclient.engine.effects.CoinEffect;
import cloud.antony.mariogptclient.engine.gpt.MarioGptHttpGenerator;
import cloud.antony.mariogptclient.engine.sprites.LifeMushroom;
import cloud.antony.mariogptclient.engine.sprites.FireFlower;
import cloud.antony.mariogptclient.engine.sprites.Mushroom;
import cloud.antony.mariogptclient.engine.effects.FireballEffect;
import cloud.antony.mariogptclient.engine.helper.TileFeature;
import cloud.antony.mariogptclient.engine.sprites.BulletBill;
import cloud.antony.mariogptclient.engine.sprites.FlowerEnemy;
import cloud.antony.mariogptclient.engine.sprites.Enemy;
import cloud.antony.mariogptclient.engine.helper.EventType;
import cloud.antony.mariogptclient.engine.helper.SpriteType;

import java.util.Arrays;
import java.util.Iterator;
import java.awt.GraphicsConfiguration;
import cloud.antony.mariogptclient.engine.graphics.MarioBackground;
import cloud.antony.mariogptclient.engine.sprites.Fireball;
import cloud.antony.mariogptclient.engine.sprites.Shell;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import cloud.antony.mariogptclient.engine.sprites.Mario;
import cloud.antony.mariogptclient.engine.helper.GameStatus;

public class MarioWorld
{
    public GameStatus gameStatus;
    public int pauseTimer;
    public int fireballsOnScreen;
    public int currentTimer;
    public float cameraX;
    public float cameraY;
    public Mario mario;
    public MarioLevel level;
    public boolean visuals;
    public int currentTick;
    public int coins;
    public int lives;
    public ArrayList<MarioEvent> lastFrameEvents;
    private MarioEvent[] killEvents;
    private ArrayList<MarioSprite> sprites;
    private ArrayList<Shell> shellsToCheck;
    private ArrayList<Fireball> fireballsToCheck;
    private ArrayList<MarioSprite> addedSprites;
    private ArrayList<MarioSprite> removedSprites;
    private ArrayList<MarioEffect> effects;
    private MarioBackground[] backgrounds;
    
    public MarioWorld(final MarioEvent[] killEvents) {
        this.pauseTimer = 0;
        this.fireballsOnScreen = 0;
        this.currentTimer = -1;
        this.backgrounds = new MarioBackground[2];
        this.pauseTimer = 0;
        this.gameStatus = GameStatus.RUNNING;
        this.sprites = new ArrayList<MarioSprite>();
        this.shellsToCheck = new ArrayList<Shell>();
        this.fireballsToCheck = new ArrayList<Fireball>();
        this.addedSprites = new ArrayList<MarioSprite>();
        this.removedSprites = new ArrayList<MarioSprite>();
        this.effects = new ArrayList<MarioEffect>();
        this.lastFrameEvents = new ArrayList<MarioEvent>();
        this.killEvents = killEvents;
    }
    
    public MarioWorld(final MarioEvent[] killEvents, final int lives) {
        this.pauseTimer = 0;
        this.fireballsOnScreen = 0;
        this.currentTimer = -1;
        this.backgrounds = new MarioBackground[2];
        this.pauseTimer = 0;
        this.gameStatus = GameStatus.RUNNING;
        this.sprites = new ArrayList<MarioSprite>();
        this.shellsToCheck = new ArrayList<Shell>();
        this.fireballsToCheck = new ArrayList<Fireball>();
        this.addedSprites = new ArrayList<MarioSprite>();
        this.removedSprites = new ArrayList<MarioSprite>();
        this.effects = new ArrayList<MarioEffect>();
        this.lastFrameEvents = new ArrayList<MarioEvent>();
        this.killEvents = killEvents;
        this.lives = lives;
    }
    
    public void initializeVisuals(final GraphicsConfiguration graphicsConfiguration) {
        this.backgrounds[0] = new MarioBackground(graphicsConfiguration, 256, new int[][] { { 42 }, { 42 }, { 42 }, { 42 }, { 42 }, { 42 }, { 42 }, { 42 }, { 42 }, { 42 }, { 42 }, { 42 }, { 42 }, { 42 }, { 42 }, { 42 } });
        this.backgrounds[1] = new MarioBackground(graphicsConfiguration, 256, new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 31, 32, 33, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 34, 35, 36, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 31, 32, 33, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 34, 35, 36, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } });
    }
    
    public void initializeLevel(MarioGptHttpGenerator gptGenerator, final int currentTimer) {
        this.currentTimer = currentTimer;

        gptGenerator.registerColumnListener((cols) -> {
            this.level.appendWorld(parseGptColsIntoRows(cols), this.visuals);
        });

        this.level = new MarioLevel(parseGptColsIntoRows(gptGenerator.getFullResponse()), this.visuals);
        this.mario = new Mario(this.visuals, (float)(this.level.marioTileX * 16), (float)(this.level.marioTileY * 16));
        this.mario.alive = true;
        this.mario.world = this;
        this.sprites.add(mario);
    }

    private String[] parseGptColsIntoRows(String[] cols) {
        String[] rows = new String[14];

        for (int i = 0; i < 14; i++) {
            StringBuilder rowBuilder = new StringBuilder();

            for (String column : cols) {
                rowBuilder.append(column.charAt(13 - i));
            }

            rows[i] = rowBuilder.toString();
        }

        return rows;
    }
    
    public ArrayList<MarioSprite> getEnemies() {
        final ArrayList<MarioSprite> list = new ArrayList<MarioSprite>();
        for (final MarioSprite e : this.sprites) {
            if (this.isEnemy(e)) {
                list.add(e);
            }
        }
        return list;
    }
    
    public MarioWorld clone() {
        final MarioWorld world = new MarioWorld(this.killEvents);
        world.visuals = false;
        world.cameraX = this.cameraX;
        world.cameraY = this.cameraY;
        world.fireballsOnScreen = this.fireballsOnScreen;
        world.gameStatus = this.gameStatus;
        world.pauseTimer = this.pauseTimer;
        world.currentTimer = this.currentTimer;
        world.currentTick = this.currentTick;
        world.level = this.level.clone();
        final Iterator<MarioSprite> iterator = this.sprites.iterator();
        while (iterator.hasNext()) {
            final MarioSprite clone = iterator.next().clone();
            clone.world = world;
            if (clone.type == SpriteType.MARIO) {
                world.mario = (Mario)clone;
            }
            world.sprites.add(clone);
        }
        if (world.mario == null) {
            world.mario = (Mario)this.mario.clone();
        }
        world.coins = this.coins;
        world.lives = this.lives;
        return world;
    }
    
    public void addEvent(final EventType eventType, final int n) {
        int n2 = 0;
        if (this.mario.isLarge) {
            n2 = 1;
        }
        if (this.mario.isFire) {
            n2 = 2;
        }
        this.lastFrameEvents.add(new MarioEvent(eventType, n, this.mario.x, this.mario.y, n2, this.currentTick));
    }
    
    public void addEffect(final MarioEffect e) {
        this.effects.add(e);
    }
    
    public void addSprite(final MarioSprite e) {
        this.addedSprites.add(e);
        e.alive = true;
        e.world = this;
        e.added();
        e.update();
    }
    
    public void removeSprite(final MarioSprite e) {
        this.removedSprites.add(e);
        e.alive = false;
        e.removed();
        e.world = null;
    }
    
    public void checkShellCollide(final Shell e) {
        this.shellsToCheck.add(e);
    }
    
    public void checkFireballCollide(final Fireball e) {
        this.fireballsToCheck.add(e);
    }
    
    public void win() {
        this.addEvent(EventType.WIN, 0);
        this.gameStatus = GameStatus.WIN;
    }
    
    public void lose() {
        this.addEvent(EventType.LOSE, 0);
        this.gameStatus = GameStatus.LOSE;
        this.mario.alive = false;
    }
    
    public void timeout() {
        this.gameStatus = GameStatus.TIME_OUT;
        this.mario.alive = false;
    }
    
    public int[][] getSceneObservation(final float n, final float n2, final int n3) {
        final int[][] array = new int[16][16];
        final int n4 = (int)n / 16;
        for (int n5 = (int)n2 / 16, i = n5 - 8, n6 = 0; i < n5 + 8; ++i, ++n6) {
            for (int j = n4 - 8, n7 = 0; j < n4 + 8; ++j, ++n7) {
                int n8 = j;
                if (n8 < 0) {
                    n8 = 0;
                }
                if (n8 > this.level.tileWidth - 1) {
                    n8 = this.level.tileWidth - 1;
                }
                int n9 = i;
                if (n9 < 0) {
                    n9 = 0;
                }
                if (n9 > this.level.tileHeight - 1) {
                    n9 = this.level.tileHeight - 1;
                }
                array[n7][n6] = MarioForwardModel.getBlockValueGeneralization(this.level.getBlock(n8, n9), n3);
            }
        }
        return array;
    }
    
    public int[][] getEnemiesObservation(final float n, final float n2, final int n3) {
        final int[][] array = new int[16][16];
        final int n4 = (int)n / 16;
        final int n5 = (int)n2 / 16;
        for (int i = 0; i < array.length; ++i) {
            for (int j = 0; j < array[0].length; ++j) {
                array[i][j] = 0;
            }
        }
        for (final MarioSprite marioSprite : this.sprites) {
            if (marioSprite.type == SpriteType.MARIO) {
                continue;
            }
            if (marioSprite.getMapX() < 0 || marioSprite.getMapX() <= n4 - 8 || marioSprite.getMapX() >= n4 + 8 || marioSprite.getMapY() < 0 || marioSprite.getMapY() <= n5 - 8 || marioSprite.getMapY() >= n5 + 8) {
                continue;
            }
            array[marioSprite.getMapX() - n4 + 8][marioSprite.getMapY() - n5 + 8] = MarioForwardModel.getSpriteTypeGeneralization(marioSprite.type, n3);
        }
        return array;
    }
    
    public int[][] getMergedObservation(final float n, final float n2, final int n3, final int n4) {
        final int[][] array = new int[16][16];
        final int n5 = (int)n / 16;
        final int n6 = (int)n2 / 16;
        for (int i = n6 - 8, n7 = 0; i < n6 + 8; ++i, ++n7) {
            for (int j = n5 - 8, n8 = 0; j < n5 + 8; ++j, ++n8) {
                int n9 = j;
                if (n9 < 0) {
                    n9 = 0;
                }
                if (n9 > this.level.tileWidth - 1) {
                    final int n10 = this.level.tileWidth - 1;
                }
                int n11 = i;
                if (n11 < 0) {
                    n11 = 0;
                }
                if (n11 > this.level.tileHeight - 1) {
                    final int n12 = this.level.tileHeight - 1;
                }
                array[n8][n7] = MarioForwardModel.getBlockValueGeneralization(this.level.getBlock(j, i), n3);
            }
        }
        for (final MarioSprite marioSprite : this.sprites) {
            if (marioSprite.type == SpriteType.MARIO) {
                continue;
            }
            if (marioSprite.getMapX() < 0 || marioSprite.getMapX() <= n5 - 8 || marioSprite.getMapX() >= n5 + 8 || marioSprite.getMapY() < 0 || marioSprite.getMapY() <= n6 - 8 || marioSprite.getMapY() >= n6 + 8) {
                continue;
            }
            final int n13 = marioSprite.getMapX() - n5 + 8;
            final int n14 = marioSprite.getMapY() - n6 + 8;
            final int spriteTypeGeneralization = MarioForwardModel.getSpriteTypeGeneralization(marioSprite.type, n4);
            if (spriteTypeGeneralization == SpriteType.NONE.getValue()) {
                continue;
            }
            array[n13][n14] = spriteTypeGeneralization;
        }
        return array;
    }
    
    private boolean isEnemy(final MarioSprite marioSprite) {
        return marioSprite instanceof Enemy || marioSprite instanceof FlowerEnemy || marioSprite instanceof BulletBill;
    }
    
    public void update(final boolean[] actions) {
        if (this.gameStatus != GameStatus.RUNNING) {
            return;
        }
        if (this.pauseTimer > 0) {
            --this.pauseTimer;
            if (this.visuals) {
                this.mario.updateGraphics();
            }
            return;
        }
        if (this.currentTimer > 0) {
            this.currentTimer -= 30;
            if (this.currentTimer <= 0) {
                this.currentTimer = 0;
                this.timeout();
                return;
            }
        }
        ++this.currentTick;
        this.cameraX = this.mario.x - 128.0f;
        if (this.cameraX + 256.0f > this.level.width) {
            this.cameraX = (float)(this.level.width - 256);
        }
        if (this.cameraX < 0.0f) {
            this.cameraX = 0.0f;
        }
        this.cameraY = this.mario.y - 128.0f;
        if (this.cameraY + 256.0f > this.level.height) {
            this.cameraY = (float)(this.level.height - 256);
        }
        if (this.cameraY < 0.0f) {
            this.cameraY = 0.0f;
        }
        this.lastFrameEvents.clear();
        this.fireballsOnScreen = 0;
        for (final MarioSprite marioSprite : this.sprites) {
            if (marioSprite.x < this.cameraX - 64.0f || marioSprite.x > this.cameraX + 256.0f + 64.0f || marioSprite.y > this.level.height + 32) {
                if (marioSprite.type == SpriteType.MARIO) {
                    this.lose();
                }
                this.removeSprite(marioSprite);
                if (!this.isEnemy(marioSprite) || marioSprite.y <= 288.0f) {
                    continue;
                }
                this.addEvent(EventType.FALL_KILL, marioSprite.type.getValue());
            }
            else {
                if (marioSprite.type != SpriteType.FIREBALL) {
                    continue;
                }
                ++this.fireballsOnScreen;
            }
        }
        this.level.update((int)this.cameraX, (int)this.cameraY);
        for (int i = (int)this.cameraX / 16 - 1; i <= (int)(this.cameraX + 256.0f) / 16 + 1; ++i) {
            for (int j = (int)this.cameraY / 16 - 1; j <= (int)(this.cameraY + 256.0f) / 16 + 1; ++j) {
                int n = 0;
                if (i * 16 + 8 > this.mario.x + 16.0f) {
                    n = -1;
                }
                if (i * 16 + 8 < this.mario.x - 16.0f) {
                    n = 1;
                }
                final SpriteType spriteType = this.level.getSpriteType(i, j);
                if (spriteType != null && spriteType != SpriteType.NONE) {
                    final String spriteCode = this.level.getSpriteCode(i, j);
                    boolean b = false;
                    final Iterator<MarioSprite> iterator2 = this.sprites.iterator();
                    while (iterator2.hasNext()) {
                        if (iterator2.next().initialCode.equals(spriteCode)) {
                            b = true;
                            break;
                        }
                    }
                    if (!b && this.level.getLastSpawnTick(i, j) != this.currentTick - 1) {
                        final MarioSprite spawnSprite = spriteType.spawnSprite(this.visuals, i, j, n);
                        spawnSprite.initialCode = spriteCode;
                        this.addSprite(spawnSprite);
                    }
                    this.level.setLastSpawnTick(i, j, this.currentTick);
                }
                if (n != 0 && TileFeature.getTileType(this.level.getBlock(i, j)).contains(TileFeature.SPAWNER) && this.currentTick % 100 == 0) {
                    this.addSprite(new BulletBill(this.visuals, (float)(i * 16 + 8 + n * 8), (float)(j * 16 + 15), n));
                }
            }
        }
        this.mario.actions = actions;
        for (final MarioSprite marioSprite2 : this.sprites) {
            if (!marioSprite2.alive) {
                continue;
            }
            marioSprite2.update();
        }
        for (final MarioSprite marioSprite3 : this.sprites) {
            if (!marioSprite3.alive) {
                continue;
            }
            marioSprite3.collideCheck();
        }
        for (final Shell shell : this.shellsToCheck) {
            for (final MarioSprite marioSprite4 : this.sprites) {
                if (marioSprite4 != shell && shell.alive && marioSprite4.alive && marioSprite4.shellCollideCheck(shell)) {
                    this.removeSprite(marioSprite4);
                }
            }
        }
        this.shellsToCheck.clear();
        for (final Fireball fireball : this.fireballsToCheck) {
            for (final MarioSprite marioSprite5 : this.sprites) {
                if (marioSprite5 != fireball && fireball.alive && marioSprite5.alive && marioSprite5.fireballCollideCheck(fireball)) {
                    if (this.visuals) {
                        this.addEffect(new FireballEffect(fireball.x, fireball.y));
                    }
                    this.removeSprite(fireball);
                }
            }
        }
        this.fireballsToCheck.clear();
        this.sprites.addAll(0, this.addedSprites);
        this.sprites.removeAll(this.removedSprites);
        this.addedSprites.clear();
        this.removedSprites.clear();
        if (this.killEvents != null) {
            final MarioEvent[] killEvents = this.killEvents;
            for (int length = killEvents.length, k = 0; k < length; ++k) {
                if (this.lastFrameEvents.contains(killEvents[k])) {
                    this.lose();
                }
            }
        }
    }
    
    public void bump(final int n, final int n2, final boolean b) {
        final ArrayList<TileFeature> tileType = TileFeature.getTileType(this.level.getBlock(n, n2));
        if (tileType.contains(TileFeature.BUMPABLE)) {
            this.bumpInto(n, n2 - 1);
            this.addEvent(EventType.BUMP, 24);
            this.level.setBlock(n, n2, 14);
            this.level.setShiftIndex(n, n2, 4);
            if (tileType.contains(TileFeature.SPECIAL)) {
                if (!this.mario.isLarge) {
                    this.addSprite(new Mushroom(this.visuals, (float)(n * 16 + 9), (float)(n2 * 16 + 8)));
                }
                else {
                    this.addSprite(new FireFlower(this.visuals, (float)(n * 16 + 9), (float)(n2 * 16 + 8)));
                }
            }
            else if (tileType.contains(TileFeature.LIFE)) {
                this.addSprite(new LifeMushroom(this.visuals, (float)(n * 16 + 9), (float)(n2 * 16 + 8)));
            }
            else {
                this.mario.collectCoin();
                if (this.visuals) {
                    this.addEffect(new CoinEffect((float)(n * 16 + 8), (float)(n2 * 16)));
                }
            }
        }
        if (tileType.contains(TileFeature.BREAKABLE)) {
            this.bumpInto(n, n2 - 1);
            if (b) {
                this.addEvent(EventType.BUMP, 22);
                this.level.setBlock(n, n2, 0);
                if (this.visuals) {
                    for (int i = 0; i < 2; ++i) {
                        for (int j = 0; j < 2; ++j) {
                            this.addEffect(new BrickEffect((float)(n * 16 + i * 8 + 4), (float)(n2 * 16 + j * 8 + 4), (float)((i * 2 - 1) * 4), (float)((j * 2 - 1) * 4 - 8)));
                        }
                    }
                }
            }
            else {
                this.level.setShiftIndex(n, n2, 4);
            }
        }
    }
    
    public void bumpInto(final int n, final int n2) {
        final int block = this.level.getBlock(n, n2);
        if (TileFeature.getTileType(block).contains(TileFeature.PICKABLE)) {
            this.addEvent(EventType.COLLECT, block);
            this.mario.collectCoin();
            this.level.setBlock(n, n2, 0);
            if (this.visuals) {
                this.addEffect(new CoinEffect((float)(n * 16 + 8), (float)(n2 * 16 + 8)));
            }
        }
        final Iterator<MarioSprite> iterator = this.sprites.iterator();
        while (iterator.hasNext()) {
            iterator.next().bumpCheck(n, n2);
        }
    }
    
    public void render(final Graphics graphics) {
        for (int i = 0; i < this.backgrounds.length; ++i) {
            this.backgrounds[i].render(graphics, (int)this.cameraX, (int)this.cameraY);
        }
        for (final MarioSprite marioSprite : this.sprites) {
            if (marioSprite.type == SpriteType.MUSHROOM || marioSprite.type == SpriteType.LIFE_MUSHROOM || marioSprite.type == SpriteType.FIRE_FLOWER || marioSprite.type == SpriteType.ENEMY_FLOWER) {
                marioSprite.render(graphics);
            }
        }
        this.level.render(graphics, (int)this.cameraX, (int)this.cameraY);
        for (final MarioSprite marioSprite2 : this.sprites) {
            if (marioSprite2.type != SpriteType.MUSHROOM && marioSprite2.type != SpriteType.LIFE_MUSHROOM && marioSprite2.type != SpriteType.FIRE_FLOWER && marioSprite2.type != SpriteType.ENEMY_FLOWER) {
                marioSprite2.render(graphics);
            }
        }
        for (int j = 0; j < this.effects.size(); ++j) {
            if (this.effects.get(j).life <= 0) {
                this.effects.remove(j);
                --j;
            }
            else {
                this.effects.get(j).render(graphics, this.cameraX, this.cameraY);
            }
        }
    }
}
