package cloud.antony.mariogptclient.engine.helper;

import cloud.antony.mariogptclient.engine.core.MarioSprite;
import cloud.antony.mariogptclient.engine.sprites.Enemy;
import cloud.antony.mariogptclient.engine.sprites.FlowerEnemy;

public enum SpriteType
{
    NONE(0), 
    UNDEF(-42), 
    MARIO(-31), 
    FIREBALL(16), 
    GOOMBA(2, 16), 
    GOOMBA_WINGED(3, 16), 
    RED_KOOPA(4, 0), 
    RED_KOOPA_WINGED(5, 0), 
    GREEN_KOOPA(6, 8), 
    GREEN_KOOPA_WINGED(7, 8), 
    SPIKY(8, 24), 
    SPIKY_WINGED(9, 24), 
    BULLET_BILL(10, 40), 
    ENEMY_FLOWER(11, 48), 
    MUSHROOM(12), 
    FIRE_FLOWER(13), 
    SHELL(14), 
    LIFE_MUSHROOM(15);
    
    private int value;
    private int startIndex;
    
    private SpriteType(final int value) {
        this.value = value;
    }
    
    private SpriteType(final int value, final int startIndex) {
        this.value = value;
        this.startIndex = startIndex;
    }
    
    public int getValue() {
        return this.value;
    }
    
    public int getStartIndex() {
        return this.startIndex;
    }
    
    public MarioSprite spawnSprite(final boolean b, final int n, final int n2, final int n3) {
        if (this == SpriteType.ENEMY_FLOWER) {
            return new FlowerEnemy(b, (float)(n * 16 + 17), (float)(n2 * 16 + 18));
        }
        return new Enemy(b, (float)(n * 16 + 8), (float)(n2 * 16 + 15), n3, this);
    }
}
