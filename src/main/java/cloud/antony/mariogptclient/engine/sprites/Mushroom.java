package cloud.antony.mariogptclient.engine.sprites;

import java.awt.Graphics;

import cloud.antony.mariogptclient.engine.core.MarioSprite;
import cloud.antony.mariogptclient.engine.graphics.MarioImage;
import cloud.antony.mariogptclient.engine.helper.EventType;
import cloud.antony.mariogptclient.engine.helper.Assets;
import cloud.antony.mariogptclient.engine.helper.SpriteType;

public class Mushroom extends MarioSprite
{
    private boolean onGround;
    private int life;
    private MarioImage graphics;
    private static final float GROUND_INERTIA = 0.89f;
    private static final float AIR_INERTIA = 0.89f;
    
    public Mushroom(final boolean b, final float n, final float n2) {
        super(n, n2, SpriteType.MUSHROOM);
        this.onGround = false;
        this.width = 4;
        this.height = 12;
        this.facing = 1;
        this.life = 0;
        if (b) {
            this.graphics = new MarioImage(Assets.items, 0);
            this.graphics.width = 16;
            this.graphics.height = 16;
            this.graphics.originX = 8;
            this.graphics.originY = 15;
        }
    }
    
    @Override
    public MarioSprite clone() {
        final Mushroom mushroom = new Mushroom(false, this.x, this.y);
        mushroom.xa = this.xa;
        mushroom.ya = this.ya;
        mushroom.initialCode = this.initialCode;
        mushroom.width = this.width;
        mushroom.height = this.height;
        mushroom.facing = this.facing;
        mushroom.life = this.life;
        mushroom.onGround = this.onGround;
        return mushroom;
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
            this.world.mario.getMushroom();
            this.world.removeSprite(this);
        }
    }
    
    private boolean isBlocking(final float n, final float n2, final float n3, final float n4) {
        final int n5 = (int)(n / 16.0f);
        final int n6 = (int)(n2 / 16.0f);
        return (n5 != (int)(this.x / 16.0f) || n6 != (int)(this.y / 16.0f)) && this.world.level.isBlocking(n5, n6, n3, n4);
    }
    
    @Override
    public void bumpCheck(final int n, final int n2) {
        if (!this.alive) {
            return;
        }
        if (this.x + this.width > n * 16 && this.x - this.width < n * 16 + 16 && n2 == (int)((this.y - 1.0f) / 16.0f)) {
            this.facing = -this.world.mario.facing;
            this.ya = -10.0f;
        }
    }
    
    private boolean move(float n, float n2) {
        while (n > 8.0f) {
            if (!this.move(8.0f, 0.0f)) {
                return false;
            }
            n -= 8.0f;
        }
        while (n < -8.0f) {
            if (!this.move(-8.0f, 0.0f)) {
                return false;
            }
            n += 8.0f;
        }
        while (n2 > 8.0f) {
            if (!this.move(0.0f, 8.0f)) {
                return false;
            }
            n2 -= 8.0f;
        }
        while (n2 < -8.0f) {
            if (!this.move(0.0f, -8.0f)) {
                return false;
            }
            n2 += 8.0f;
        }
        int n3 = 0;
        if (n2 > 0.0f) {
            if (this.isBlocking(this.x + n - this.width, this.y + n2, n, 0.0f)) {
                n3 = 1;
            }
            else if (this.isBlocking(this.x + n + this.width, this.y + n2, n, 0.0f)) {
                n3 = 1;
            }
            else if (this.isBlocking(this.x + n - this.width, this.y + n2 + 1.0f, n, n2)) {
                n3 = 1;
            }
            else if (this.isBlocking(this.x + n + this.width, this.y + n2 + 1.0f, n, n2)) {
                n3 = 1;
            }
        }
        if (n2 < 0.0f) {
            if (this.isBlocking(this.x + n, this.y + n2 - this.height, n, n2)) {
                n3 = 1;
            }
            else if (n3 != 0 || this.isBlocking(this.x + n - this.width, this.y + n2 - this.height, n, n2)) {
                n3 = 1;
            }
            else if (n3 != 0 || this.isBlocking(this.x + n + this.width, this.y + n2 - this.height, n, n2)) {
                n3 = 1;
            }
        }
        if (n > 0.0f) {
            if (this.isBlocking(this.x + n + this.width, this.y + n2 - this.height, n, n2)) {
                n3 = 1;
            }
            if (this.isBlocking(this.x + n + this.width, this.y + n2 - this.height / 2, n, n2)) {
                n3 = 1;
            }
            if (this.isBlocking(this.x + n + this.width, this.y + n2, n, n2)) {
                n3 = 1;
            }
        }
        if (n < 0.0f) {
            if (this.isBlocking(this.x + n - this.width, this.y + n2 - this.height, n, n2)) {
                n3 = 1;
            }
            if (this.isBlocking(this.x + n - this.width, this.y + n2 - this.height / 2, n, n2)) {
                n3 = 1;
            }
            if (this.isBlocking(this.x + n - this.width, this.y + n2, n, n2)) {
                n3 = 1;
            }
        }
        if (n3 != 0) {
            if (n < 0.0f) {
                this.x = (float)((int)((this.x - this.width) / 16.0f) * 16 + this.width);
                this.xa = 0.0f;
            }
            if (n > 0.0f) {
                this.x = (float)((int)((this.x + this.width) / 16.0f + 1.0f) * 16 - this.width - 1);
                this.xa = 0.0f;
            }
            if (n2 < 0.0f) {
                this.y = (float)((int)((this.y - this.height) / 16.0f) * 16 + this.height);
                this.ya = 0.0f;
            }
            if (n2 > 0.0f) {
                this.y = (float)((int)(this.y / 16.0f + 1.0f) * 16 - 1);
                this.onGround = true;
            }
            return false;
        }
        this.x += n;
        this.y += n2;
        return true;
    }
    
    @Override
    public void update() {
        if (!this.alive) {
            return;
        }
        if (this.life < 9) {
            --this.y;
            ++this.life;
            return;
        }
        final float n = 1.75f;
        if (this.xa > 2.0f) {
            this.facing = 1;
        }
        if (this.xa < -2.0f) {
            this.facing = -1;
        }
        this.xa = this.facing * n;
        if (!this.move(this.xa, 0.0f)) {
            this.facing = -this.facing;
        }
        this.onGround = false;
        this.move(0.0f, this.ya);
        this.ya *= 0.85f;
        if (this.onGround) {
            this.xa *= 0.89f;
        }
        else {
            this.xa *= 0.89f;
        }
        if (!this.onGround) {
            this.ya += 2.0f;
        }
    }
    
    @Override
    public void render(final Graphics graphics) {
        super.render(graphics);
        this.graphics.render(graphics, (int)(this.x - this.world.cameraX), (int)(this.y - this.world.cameraY));
    }
}
