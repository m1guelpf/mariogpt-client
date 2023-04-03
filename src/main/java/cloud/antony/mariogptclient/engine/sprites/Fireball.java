package cloud.antony.mariogptclient.engine.sprites;

import java.awt.Graphics;

import cloud.antony.mariogptclient.engine.core.MarioSprite;
import cloud.antony.mariogptclient.engine.graphics.MarioImage;
import cloud.antony.mariogptclient.engine.helper.Assets;
import cloud.antony.mariogptclient.engine.helper.SpriteType;

public class Fireball extends MarioSprite
{
    private static final float GROUND_INERTIA = 0.89f;
    private static final float AIR_INERTIA = 0.89f;
    private boolean onGround;
    private MarioImage graphics;
    private int anim;
    
    public Fireball(final boolean b, final float n, final float n2, final int facing) {
        super(n, n2, SpriteType.FIREBALL);
        this.onGround = false;
        this.anim = 0;
        this.facing = facing;
        this.ya = 4.0f;
        this.width = 4;
        this.height = 8;
        if (b) {
            this.graphics = new MarioImage(Assets.particles, 24);
            this.graphics.originX = 8;
            this.graphics.originY = 8;
            this.graphics.width = 16;
            this.graphics.height = 16;
        }
    }
    
    @Override
    public MarioSprite clone() {
        final Fireball fireball = new Fireball(false, this.x, this.y, this.facing);
        fireball.xa = this.xa;
        fireball.ya = this.ya;
        fireball.initialCode = this.initialCode;
        fireball.width = this.width;
        fireball.height = this.height;
        fireball.onGround = this.onGround;
        return fireball;
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
    
    private boolean isBlocking(final float n, final float n2, final float n3, final float n4) {
        final int n5 = (int)(n / 16.0f);
        final int n6 = (int)(n2 / 16.0f);
        return (n5 != (int)(this.x / 16.0f) || n6 != (int)(this.y / 16.0f)) && this.world.level.isBlocking(n5, n6, n3, n4);
    }
    
    @Override
    public void update() {
        if (!this.alive) {
            return;
        }
        if (this.facing != 0) {
            ++this.anim;
        }
        final float n = 8.0f;
        if (this.xa > 2.0f) {
            this.facing = 1;
        }
        if (this.xa < -2.0f) {
            this.facing = -1;
        }
        this.xa = this.facing * n;
        this.world.checkFireballCollide(this);
        if (!this.move(this.xa, 0.0f)) {
            this.world.removeSprite(this);
            return;
        }
        this.onGround = false;
        this.move(0.0f, this.ya);
        if (this.onGround) {
            this.ya = -10.0f;
        }
        this.ya *= 0.95f;
        if (this.onGround) {
            this.xa *= 0.89f;
        }
        else {
            this.xa *= 0.89f;
        }
        if (!this.onGround) {
            this.ya += 1.5;
        }
        if (this.graphics != null) {
            this.graphics.flipX = (this.facing == -1);
            this.graphics.index = 24 + this.anim % 4;
        }
    }
    
    @Override
    public void render(final Graphics graphics) {
        super.render(graphics);
        this.graphics.render(graphics, (int)(this.x - this.world.cameraX), (int)(this.y - this.world.cameraY));
    }
}
