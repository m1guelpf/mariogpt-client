package cloud.antony.mariogptclient.engine.sprites;

import java.awt.Graphics;

import cloud.antony.mariogptclient.engine.core.MarioSprite;
import cloud.antony.mariogptclient.engine.graphics.MarioImage;
import cloud.antony.mariogptclient.engine.helper.EventType;
import cloud.antony.mariogptclient.engine.helper.Assets;
import cloud.antony.mariogptclient.engine.helper.SpriteType;

public class FireFlower extends MarioSprite
{
    private MarioImage graphics;
    private int life;
    
    public FireFlower(final boolean b, final float n, final float n2) {
        super(n, n2, SpriteType.FIRE_FLOWER);
        this.width = 4;
        this.height = 12;
        this.facing = 1;
        this.life = 0;
        if (b) {
            this.graphics = new MarioImage(Assets.items, 1);
            this.graphics.originX = 8;
            this.graphics.originY = 15;
            this.graphics.width = 16;
            this.graphics.height = 16;
        }
    }
    
    @Override
    public MarioSprite clone() {
        final FireFlower fireFlower = new FireFlower(false, this.x, this.y);
        fireFlower.xa = this.xa;
        fireFlower.ya = this.ya;
        fireFlower.initialCode = this.initialCode;
        fireFlower.width = this.width;
        fireFlower.height = this.height;
        fireFlower.facing = this.facing;
        fireFlower.life = this.life;
        return fireFlower;
    }
    
    @Override
    public void collideCheck() {
        if (!this.alive) {
            return;
        }
        final float n = this.world.mario.x - this.x;
        final float n2 = this.world.mario.y - this.y;
        if (n > -16.0f && n < 16.0f && n2 > -this.height && n2 < this.world.mario.height) {
            this.world.addEvent(EventType.COLLECT, this.type.getValue());
            this.world.mario.getFlower();
            this.world.removeSprite(this);
        }
    }
    
    @Override
    public void update() {
        if (!this.alive) {
            return;
        }
        super.update();
        ++this.life;
        if (this.life < 9) {
            --this.y;
            return;
        }
        if (this.graphics != null) {
            this.graphics.index = 1 + this.life / 2 % 2;
        }
    }
    
    @Override
    public void render(final Graphics graphics) {
        super.render(graphics);
        this.graphics.render(graphics, (int)(this.x - this.world.cameraX), (int)(this.y - this.world.cameraY));
    }
}
