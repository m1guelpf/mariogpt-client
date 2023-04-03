package cloud.antony.mariogptclient.engine.sprites;

import cloud.antony.mariogptclient.engine.core.MarioSprite;
import cloud.antony.mariogptclient.engine.graphics.MarioImage;
import cloud.antony.mariogptclient.engine.helper.EventType;
import cloud.antony.mariogptclient.engine.effects.DeathEffect;
import java.awt.Graphics;
import cloud.antony.mariogptclient.engine.helper.Assets;
import cloud.antony.mariogptclient.engine.helper.SpriteType;

public class BulletBill extends MarioSprite
{
    private MarioImage graphics;
    
    public BulletBill(final boolean b, final float n, final float n2, final int facing) {
        super(n, n2, SpriteType.BULLET_BILL);
        this.width = 4;
        this.height = 12;
        this.ya = -5.0f;
        this.facing = facing;
        if (b) {
            this.graphics = new MarioImage(Assets.enemies, 40);
            this.graphics.originX = 8;
            this.graphics.originY = 31;
            this.graphics.width = 16;
        }
    }
    
    @Override
    public MarioSprite clone() {
        final BulletBill bulletBill = new BulletBill(false, this.x, this.y, this.facing);
        bulletBill.xa = this.xa;
        bulletBill.ya = this.ya;
        bulletBill.width = this.width;
        bulletBill.height = this.height;
        return bulletBill;
    }
    
    @Override
    public void update() {
        if (!this.alive) {
            return;
        }
        super.update();
        this.move(this.xa = this.facing * 4.0f, 0.0f);
        if (this.graphics != null) {
            this.graphics.flipX = (this.facing == -1);
        }
    }
    
    @Override
    public void render(final Graphics graphics) {
        super.render(graphics);
        this.graphics.render(graphics, (int)(this.x - this.world.cameraX), (int)(this.y - this.world.cameraY));
    }
    
    @Override
    public void collideCheck() {
        if (!this.alive) {
            return;
        }
        final float n = this.world.mario.x - this.x;
        final float n2 = this.world.mario.y - this.y;
        if (n > -16.0f && n < 16.0f && n2 > -this.height && n2 < this.world.mario.height) {
            if (this.world.mario.ya > 0.0f && n2 <= 0.0f && (!this.world.mario.onGround || !this.world.mario.wasOnGround)) {
                this.world.mario.stomp(this);
                if (this.graphics != null) {
                    this.world.addEffect(new DeathEffect(this.x, this.y - 7.0f, this.graphics.flipX, 43, 0.0f));
                }
                this.world.removeSprite(this);
            }
            else {
                this.world.addEvent(EventType.HURT, this.type.getValue());
                this.world.mario.getHurt();
            }
        }
    }
    
    private boolean move(final float n, final float n2) {
        this.x += n;
        return true;
    }
    
    @Override
    public boolean fireballCollideCheck(final Fireball fireball) {
        if (!this.alive) {
            return false;
        }
        final float n = fireball.x - this.x;
        final float n2 = fireball.y - this.y;
        return n > -16.0f && n < 16.0f && n2 > -this.height && n2 < fireball.height;
    }
    
    @Override
    public boolean shellCollideCheck(final Shell shell) {
        if (!this.alive) {
            return false;
        }
        final float n = shell.x - this.x;
        final float n2 = shell.y - this.y;
        if (n > -16.0f && n < 16.0f && n2 > -this.height && n2 < shell.height) {
            if (this.graphics != null) {
                this.world.addEffect(new DeathEffect(this.x, this.y - 7.0f, this.graphics.flipX, 43, -1.0f));
            }
            this.world.addEvent(EventType.SHELL_KILL, this.type.getValue());
            this.world.removeSprite(this);
            return true;
        }
        return false;
    }
}
