package cloud.antony.mariogptclient.engine.core;

import java.awt.Graphics;
import java.util.ArrayList;
import cloud.antony.mariogptclient.engine.helper.TileFeature;
import cloud.antony.mariogptclient.engine.helper.Assets;
import cloud.antony.mariogptclient.engine.graphics.MarioImage;
import cloud.antony.mariogptclient.engine.graphics.MarioTilemap;
import cloud.antony.mariogptclient.engine.helper.SpriteType;

public class MarioLevel
{
    public int width;
    public int tileWidth;
    public int height;
    public int tileHeight;
    public int totalCoins;
    public int marioTileX;
    public int marioTileY;
    public int exitTileX;
    public int exitTileY;
    public int[][] levelTiles;
    private SpriteType[][] spriteTemplates;
    private int[][] lastSpawnTime;
    private MarioTilemap graphics;
    private MarioImage flag;
    
    public MarioLevel(final String[] levelRows, final boolean b) {
        this.width = 256;
        this.tileWidth = 16;
        this.height = 256;
        this.tileHeight = 16;
        this.totalCoins = 0;
        if (levelRows.length == 0) {
            this.tileWidth = 0;
            this.width = 0;
            this.tileHeight = 0;
            this.height = 0;
            return;
        }

        appendWorld(levelRows, b);
    }

    public void appendWorld(String[] levelRows, boolean b) {
        this.tileWidth = levelRows[0].length();
        this.width = this.tileWidth * 16;
        this.tileHeight = levelRows.length;
        this.height = this.tileHeight * 16;
        int shift = Math.max(0, this.levelTiles == null ? 0 : (this.levelTiles.length - 1));
        int[][] newLevelTiles = new int[levelRows[0].length()][levelRows.length];
        if (this.levelTiles != null)
            System.arraycopy(this.levelTiles, 0, newLevelTiles, 0, shift);

        SpriteType[][] newSpriteTemplates = new SpriteType[levelRows[0].length()][levelRows.length];
        if (this.spriteTemplates != null)
            System.arraycopy(this.spriteTemplates, 0, newSpriteTemplates, 0, shift);

        int[][] newLastSpawnTime = new int[levelRows[0].length()][levelRows.length];
        if (this.lastSpawnTime != null)
            System.arraycopy(this.lastSpawnTime, 0, newLastSpawnTime, 0, shift);

        for (int i = 0; i < levelRows.length; ++i) {
            for (int j = shift; j < levelRows[i].length(); ++j) {
                newLevelTiles[j][i] = 0;
                newSpriteTemplates[j][i] = SpriteType.NONE;
                newLastSpawnTime[j][i] = -40;
            }
        }
        boolean b2 = false;
        boolean b3 = false;
        for (int k = 0; k < levelRows.length; ++k) {
            for (int l = shift; l < levelRows[k].length(); ++l) {
                switch (levelRows[k].charAt(l)) {
                    case 'M': {
                        this.marioTileX = l;
                        this.marioTileY = k;
                        b2 = true;
                        break;
                    }
                    case 'F': {
                        this.exitTileX = l;
                        this.exitTileY = k;
                        b3 = true;
                        break;
                    }
                    case 'y': {
                        newSpriteTemplates[l][k] = SpriteType.SPIKY;
                        break;
                    }
                    case 'Y': {
                        newSpriteTemplates[l][k] = SpriteType.SPIKY_WINGED;
                        break;
                    }
                    case 'E':
                    case 'g': {
                        newSpriteTemplates[l][k] = SpriteType.GOOMBA;
                        break;
                    }
                    case 'G': {
                        newSpriteTemplates[l][k] = SpriteType.GOOMBA_WINGED;
                        break;
                    }
                    case 'k': {
                        newSpriteTemplates[l][k] = SpriteType.GREEN_KOOPA;
                        break;
                    }
                    case 'K': {
                        newSpriteTemplates[l][k] = SpriteType.GREEN_KOOPA_WINGED;
                        break;
                    }
                    case 'r': {
                        newSpriteTemplates[l][k] = SpriteType.RED_KOOPA;
                        break;
                    }
                    case 'R': {
                        newSpriteTemplates[l][k] = SpriteType.RED_KOOPA_WINGED;
                        break;
                    }
                    case 'X': {
                        newLevelTiles[l][k] = 1;
                        break;
                    }
                    case '#': {
                        newLevelTiles[l][k] = 2;
                        break;
                    }
                    case '%': {
                        int n = 0;
                        if (l > 0 && levelRows[k].charAt(l - 1) == '%') {
                            n += 2;
                        }
                        if (l < newLevelTiles.length - 1 && levelRows[k].charAt(l + 1) == '%') {
                            ++n;
                        }
                        newLevelTiles[l][k] = 43 + n;
                        break;
                    }
                    case '|': {
                        newLevelTiles[l][k] = 47;
                        break;
                    }
                    case '*': {
                        int n2 = 0;
                        if (k > 0 && levelRows[k - 1].charAt(l) == '*') {
                            ++n2;
                        }
                        if (k > 1 && levelRows[k - 2].charAt(l) == '*') {
                            ++n2;
                        }
                        newLevelTiles[l][k] = 3 + n2;
                        break;
                    }
                    case 'B': {
                        newLevelTiles[l][k] = 3;
                        break;
                    }
                    case 'b': {
                        int n3 = 0;
                        if (k > 1 && levelRows[k - 2].charAt(l) == 'B') {
                            ++n3;
                        }
                        newLevelTiles[l][k] = 4 + n3;
                        break;
                    }
                    case '?':
                    case '@': {
                        newLevelTiles[l][k] = 8;
                        break;
                    }
                    case '!':
                    case 'Q': {
                        ++this.totalCoins;
                        newLevelTiles[l][k] = 11;
                        break;
                    }
                    case '1': {
                        newLevelTiles[l][k] = 48;
                        break;
                    }
                    case '2': {
                        ++this.totalCoins;
                        newLevelTiles[l][k] = 49;
                        break;
                    }
                    case 'D': {
                        newLevelTiles[l][k] = 14;
                        break;
                    }
                    case 'S': {
                        newLevelTiles[l][k] = 6;
                        break;
                    }
                    case 'C': {
                        ++this.totalCoins;
                        newLevelTiles[l][k] = 7;
                        break;
                    }
                    case 'U': {
                        newLevelTiles[l][k] = 50;
                        break;
                    }
                    case 'L': {
                        newLevelTiles[l][k] = 51;
                        break;
                    }
                    case 'o': {
                        ++this.totalCoins;
                        newLevelTiles[l][k] = 15;
                        break;
                    }
                    case 't': {
                        int n4 = 0;
                        boolean b4 = false;
                        if (l < levelRows[k].length() - 1 && Character.toLowerCase(levelRows[k].charAt(l + 1)) != 't' && l > 0 && Character.toLowerCase(levelRows[k].charAt(l - 1)) != 't') {
                            b4 = true;
                        }
                        if (l > 0 && (newLevelTiles[l - 1][k] == 18 || newLevelTiles[l - 1][k] == 20)) {
                            ++n4;
                        }
                        if (k > 0 && Character.toLowerCase(levelRows[k - 1].charAt(l)) == 't') {
                            if (b4) {
                                ++n4;
                            }
                            else {
                                n4 += 2;
                            }
                        }
                        if (b4) {
                            newLevelTiles[l][k] = 52 + n4;
                            break;
                        }
                        newLevelTiles[l][k] = 18 + n4;
                        break;
                    }
                    case 'T': {
                        int n5 = 0;
                        final boolean b5 = l < levelRows[k].length() - 1 && Character.toLowerCase(levelRows[k].charAt(l + 1)) != 't' && l > 0 && Character.toLowerCase(levelRows[k].charAt(l - 1)) != 't';
                        if (l > 0 && (newLevelTiles[l - 1][k] == 18 || newLevelTiles[l - 1][k] == 20)) {
                            ++n5;
                        }
                        if (k > 0 && Character.toLowerCase(levelRows[k - 1].charAt(l)) == 't') {
                            if (b5) {
                                ++n5;
                            }
                            else {
                                n5 += 2;
                            }
                        }
                        if (b5) {
                            newLevelTiles[l][k] = 52 + n5;
                            break;
                        }
                        if (n5 == 0) {
                            newSpriteTemplates[l][k] = SpriteType.ENEMY_FLOWER;
                        }
                        newLevelTiles[l][k] = 18 + n5;
                        break;
                    }
                    case '<': {
                        newLevelTiles[l][k] = 18;
                        break;
                    }
                    case '>': {
                        newLevelTiles[l][k] = 19;
                        break;
                    }
                    case '[': {
                        newLevelTiles[l][k] = 20;
                        break;
                    }
                    case ']': {
                        newLevelTiles[l][k] = 21;
                        break;
                    }
                }
            }
        }
        if (!b2) {
            this.marioTileX = 0;
            this.marioTileY = this.findFirstFloor(levelRows, this.marioTileX);
        }
        if (!b3) {
            this.exitTileX = levelRows[0].length() - 1;
            this.exitTileY = this.findFirstFloor(levelRows, this.exitTileX);
        }
        for (int exitTileY = this.exitTileY; exitTileY > Math.max(1, this.exitTileY - 11); --exitTileY) {
            newLevelTiles[this.exitTileX][exitTileY] = 40;
        }
        newLevelTiles[this.exitTileX][Math.max(1, this.exitTileY - 11)] = 39;
        this.levelTiles = newLevelTiles;
        this.spriteTemplates = newSpriteTemplates;
        this.lastSpawnTime = newLastSpawnTime;
        if (b) {
            this.graphics = new MarioTilemap(Assets.level, this.levelTiles);
            this.flag = new MarioImage(Assets.level, 41);
            this.flag.width = 16;
            this.flag.height = 16;
        }
    }
    
    public MarioLevel clone() {
        final MarioLevel marioLevel = new MarioLevel(new String[0], false);
        marioLevel.width = this.width;
        marioLevel.height = this.height;
        marioLevel.tileWidth = this.tileWidth;
        marioLevel.tileHeight = this.tileHeight;
        marioLevel.totalCoins = this.totalCoins;
        marioLevel.marioTileX = this.marioTileX;
        marioLevel.marioTileY = this.marioTileY;
        marioLevel.exitTileX = this.exitTileX;
        marioLevel.exitTileY = this.exitTileY;
        marioLevel.levelTiles = new int[this.levelTiles.length][this.levelTiles[0].length];
        marioLevel.lastSpawnTime = new int[this.levelTiles.length][this.levelTiles[0].length];
        for (int i = 0; i < marioLevel.levelTiles.length; ++i) {
            for (int j = 0; j < marioLevel.levelTiles[i].length; ++j) {
                marioLevel.levelTiles[i][j] = this.levelTiles[i][j];
                marioLevel.lastSpawnTime[i][j] = this.lastSpawnTime[i][j];
            }
        }
        marioLevel.spriteTemplates = this.spriteTemplates;
        return marioLevel;
    }
    
    public boolean isBlocking(final int n, final int n2, final float n3, final float n4) {
        final ArrayList<TileFeature> tileType = TileFeature.getTileType(this.getBlock(n, n2));
        return tileType.contains(TileFeature.BLOCK_ALL) | (n4 < 0.0f && tileType.contains(TileFeature.BLOCK_UPPER)) | (n4 > 0.0f && tileType.contains(TileFeature.BLOCK_LOWER));
    }
    
    public int getBlock(int n, final int n2) {
        if (n < 0) {
            n = 0;
        }
        if (n > this.tileWidth - 1) {
            n = this.tileWidth - 1;
        }
        if (n2 < 0 || n2 > this.tileHeight - 1) {
            return 0;
        }
        return this.levelTiles[n][n2];
    }
    
    public void setBlock(final int n, final int n2, final int n3) {
        if (n < 0 || n2 < 0 || n > this.tileWidth - 1 || n2 > this.tileHeight - 1) {
            return;
        }
        this.levelTiles[n][n2] = n3;
    }
    
    public void setShiftIndex(final int n, final int n2, final int n3) {
        if (this.graphics == null || n < 0 || n2 < 0 || n > this.tileWidth - 1 || n2 > this.tileHeight - 1) {
            return;
        }
        this.graphics.moveShift[n][n2] = (float)n3;
    }
    
    public SpriteType getSpriteType(final int n, final int n2) {
        if (n < 0 || n2 < 0 || n >= this.tileWidth || n2 >= this.tileHeight) {
            return SpriteType.NONE;
        }
        return this.spriteTemplates[n][n2];
    }
    
    public int getLastSpawnTick(final int n, final int n2) {
        if (n < 0 || n2 < 0 || n > this.tileWidth - 1 || n2 > this.tileHeight - 1) {
            return 0;
        }
        return this.lastSpawnTime[n][n2];
    }
    
    public void setLastSpawnTick(final int n, final int n2, final int n3) {
        if (n < 0 || n2 < 0 || n > this.tileWidth - 1 || n2 > this.tileHeight - 1) {
            return;
        }
        this.lastSpawnTime[n][n2] = n3;
    }
    
    public String getSpriteCode(final int i, final int j) {
        return i + "_" + j + "_" + this.getSpriteType(i, j).getValue();
    }
    
    private boolean isSolid(final char c) {
        return c == 'X' || c == '#' || c == '@' || c == '!' || c == 'B' || c == 'C' || c == 'Q' || c == '<' || c == '>' || c == '[' || c == ']' || c == '?' || c == 'S' || c == 'U' || c == 'D' || c == '%' || c == 't' || c == 'T';
    }
    
    private int findFirstFloor(final String[] array, final int index) {
        boolean b = true;
        for (int i = array.length - 1; i >= 0; --i) {
            final char value = array[i].charAt(index);
            if (this.isSolid(value)) {
                b = false;
            }
            else if (!b && !this.isSolid(value)) {
                return i;
            }
        }
        return -1;
    }
    
    public void update(final int n, final int n2) {
    }
    
    public void render(final Graphics graphics, final int n, final int n2) {
        this.graphics.render(graphics, n, n2);
        if (n + 256 >= this.exitTileX * 16) {
            this.flag.render(graphics, this.exitTileX * 16 - 8 - n, Math.max(1, this.exitTileY - 11) * 16 + 16 - n2);
        }
    }
}
