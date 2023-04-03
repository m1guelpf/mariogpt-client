package cloud.antony.mariogptclient.engine.sprites;

import cloud.antony.mariogptclient.engine.core.MarioSprite;
import cloud.antony.mariogptclient.engine.helper.SpriteType;

public class FlowerEnemy extends Enemy
{
    private float yStart;
    private int tick;
    private int waitTime;
    
    public FlowerEnemy(final boolean b, final float n, final float n2) {
        super(b, n, n2, 0, SpriteType.ENEMY_FLOWER);
        this.winged = false;
        this.noFireballDeath = false;
        this.width = 2;
        this.yStart = this.y;
        this.ya = -1.0f;
        --this.y;
        for (int i = 0; i < 4; ++i) {
            this.update();
        }
        if (b) {
            this.graphics.originY = 24;
            this.tick = 0;
        }
    }
    
    @Override
    public MarioSprite clone() {
        final FlowerEnemy flowerEnemy = new FlowerEnemy(false, this.x, this.y);
        flowerEnemy.xa = this.xa;
        flowerEnemy.ya = this.ya;
        flowerEnemy.initialCode = this.initialCode;
        flowerEnemy.width = this.width;
        flowerEnemy.height = this.height;
        flowerEnemy.onGround = this.onGround;
        flowerEnemy.winged = this.winged;
        flowerEnemy.avoidCliffs = this.avoidCliffs;
        flowerEnemy.noFireballDeath = this.noFireballDeath;
        flowerEnemy.yStart = this.yStart;
        flowerEnemy.waitTime = this.waitTime;
        return flowerEnemy;
    }
    
    @Override
    public void update() {
        if (!this.alive) {
            return;
        }
        if (this.ya > 0.0f) {
            if (this.y >= this.yStart) {
                this.y = this.yStart;
                final int n = (int)Math.abs(this.world.mario.x - this.x);
                ++this.waitTime;
                if (this.waitTime > 40 && n > 24) {
                    this.waitTime = 0;
                    this.ya = -1.0f;
                }
            }
        }
        else if (this.ya < 0.0f && this.yStart - this.y > 20.0f) {
            this.y = this.yStart - 20.0f;
            ++this.waitTime;
            if (this.waitTime > 40) {
                this.waitTime = 0;
                this.ya = 1.0f;
            }
        }
        this.y += this.ya;
        if (this.graphics != null) {
            ++this.tick;
            this.graphics.index = this.type.getStartIndex() + (this.tick / 2 & 0x1) * 2 + (this.tick / 6 & 0x1);
        }
    }
}
