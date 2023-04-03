package cloud.antony.mariogptclient.engine.sprites;

import java.awt.Graphics;

import cloud.antony.mariogptclient.engine.core.MarioSprite;
import cloud.antony.mariogptclient.engine.graphics.MarioImage;
import cloud.antony.mariogptclient.engine.effects.DeathEffect;
import cloud.antony.mariogptclient.engine.helper.EventType;
import cloud.antony.mariogptclient.engine.effects.SquishEffect;
import cloud.antony.mariogptclient.engine.helper.Assets;
import cloud.antony.mariogptclient.engine.helper.SpriteType;

public class Enemy extends MarioSprite
{
    private static final float GROUND_INERTIA = 0.89f;
    private static final float AIR_INERTIA = 0.89f;
    protected boolean onGround;
    protected boolean avoidCliffs;
    protected boolean winged;
    protected boolean noFireballDeath;
    protected float runTime;
    protected int wingTime;
    protected MarioImage wingGraphics;
    protected MarioImage graphics;
    
    public Enemy(final boolean b, final float n, final float n2, final int facing, final SpriteType spriteType) {
        super(n, n2, spriteType);
        this.onGround = false;
        this.avoidCliffs = true;
        this.winged = true;
        this.wingTime = 0;
        this.width = 4;
        this.height = 24;
        if (this.type != SpriteType.RED_KOOPA && this.type != SpriteType.GREEN_KOOPA && this.type != SpriteType.RED_KOOPA_WINGED && this.type != SpriteType.GREEN_KOOPA_WINGED) {
            this.height = 12;
        }
        this.winged = (this.type.getValue() % 2 == 1);
        this.avoidCliffs = (this.type == SpriteType.RED_KOOPA || this.type == SpriteType.RED_KOOPA_WINGED);
        this.noFireballDeath = (this.type == SpriteType.SPIKY || this.type == SpriteType.SPIKY_WINGED);
        this.facing = facing;
        if (this.facing == 0) {
            this.facing = 1;
        }
        if (b) {
            this.graphics = new MarioImage(Assets.enemies, this.type.getStartIndex());
            this.graphics.originX = 8;
            this.graphics.originY = 31;
            this.graphics.width = 16;
            this.wingGraphics = new MarioImage(Assets.enemies, 32);
            this.wingGraphics.originX = 16;
            this.wingGraphics.originY = 31;
            this.wingGraphics.width = 16;
        }
    }
    
    @Override
    public MarioSprite clone() {
        final Enemy enemy = new Enemy(false, this.x, this.y, this.facing, this.type);
        enemy.xa = this.xa;
        enemy.ya = this.ya;
        enemy.initialCode = this.initialCode;
        enemy.width = this.width;
        enemy.height = this.height;
        enemy.onGround = this.onGround;
        enemy.winged = this.winged;
        enemy.avoidCliffs = this.avoidCliffs;
        enemy.noFireballDeath = this.noFireballDeath;
        return enemy;
    }
    
    @Override
    public void collideCheck() {
        if (!this.alive) {
            return;
        }
        final float n = this.world.mario.x - this.x;
        final float n2 = this.world.mario.y - this.y;
        if (n > -this.width * 2 - 4 && n < this.width * 2 + 4 && n2 > -this.height && n2 < this.world.mario.height) {
            if (this.type != SpriteType.SPIKY && this.type != SpriteType.SPIKY_WINGED && this.type != SpriteType.ENEMY_FLOWER && this.world.mario.ya > 0.0f && n2 <= 0.0f && (!this.world.mario.onGround || !this.world.mario.wasOnGround)) {
                this.world.mario.stomp(this);
                if (this.winged) {
                    this.winged = false;
                    this.ya = 0.0f;
                }
                else {
                    if (this.type == SpriteType.GREEN_KOOPA || this.type == SpriteType.GREEN_KOOPA_WINGED) {
                        this.world.addSprite(new Shell(this.graphics != null, this.x, this.y, 1, this.initialCode));
                    }
                    else if (this.type == SpriteType.RED_KOOPA || this.type == SpriteType.RED_KOOPA_WINGED) {
                        this.world.addSprite(new Shell(this.graphics != null, this.x, this.y, 0, this.initialCode));
                    }
                    else if ((this.type == SpriteType.GOOMBA || this.type == SpriteType.GOOMBA_WINGED) && this.graphics != null) {
                        this.world.addEffect(new SquishEffect(this.x, this.y - 7.0f));
                    }
                    this.world.addEvent(EventType.STOMP_KILL, this.type.getValue());
                    this.world.removeSprite(this);
                }
            }
            else {
                this.world.addEvent(EventType.HURT, this.type.getValue());
                this.world.mario.getHurt();
            }
        }
    }
    
    private void updateGraphics() {
        ++this.wingTime;
        this.wingGraphics.index = 32 + this.wingTime / 4 % 2;
        this.graphics.flipX = (this.facing == -1);
        this.runTime += Math.abs(this.xa) + 5.0f;
        int n = (int)(this.runTime / 20.0f) % 2;
        if (!this.onGround) {
            n = 1;
        }
        if (this.winged) {
            n = this.wingTime / 4 % 2;
        }
        this.graphics.index = this.type.getStartIndex() + n;
    }
    
    @Override
    public void update() {
        if (!this.alive) {
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
        this.ya *= (this.winged ? 0.95f : 0.85f);
        if (this.onGround) {
            this.xa *= 0.89f;
        }
        else {
            this.xa *= 0.89f;
        }
        if (!this.onGround) {
            if (this.winged) {
                this.ya += 0.6f;
            }
            else {
                this.ya += 2.0f;
            }
        }
        else if (this.winged) {
            this.ya = -10.0f;
        }
        if (this.graphics != null) {
            this.updateGraphics();
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
            if (this.avoidCliffs && this.onGround && !this.world.level.isBlocking((int)((this.x + n + this.width) / 16.0f), (int)(this.y / 16.0f + 1.0f), n, 1.0f)) {
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
            if (this.avoidCliffs && this.onGround && !this.world.level.isBlocking((int)((this.x + n - this.width) / 16.0f), (int)(this.y / 16.0f + 1.0f), n, 1.0f)) {
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
    public boolean shellCollideCheck(final Shell shell) {
        if (!this.alive) {
            return false;
        }
        final float n = shell.x - this.x;
        final float n2 = shell.y - this.y;
        if (n > -16.0f && n < 16.0f && n2 > -this.height && n2 < shell.height) {
            this.xa = (float)(shell.facing * 2);
            this.ya = -5.0f;
            this.world.addEvent(EventType.SHELL_KILL, this.type.getValue());
            if (this.graphics != null) {
                if (this.type == SpriteType.GREEN_KOOPA || this.type == SpriteType.GREEN_KOOPA_WINGED) {
                    this.world.addEffect(new DeathEffect(this.x, this.y, this.graphics.flipX, 42, -5.0f));
                }
                else if (this.type == SpriteType.RED_KOOPA || this.type == SpriteType.RED_KOOPA_WINGED) {
                    this.world.addEffect(new DeathEffect(this.x, this.y, this.graphics.flipX, 41, -5.0f));
                }
                else if (this.type == SpriteType.GOOMBA || this.type == SpriteType.GOOMBA_WINGED) {
                    this.world.addEffect(new DeathEffect(this.x, this.y, this.graphics.flipX, 44, -5.0f));
                }
                else if (this.type == SpriteType.SPIKY || this.type == SpriteType.SPIKY_WINGED) {
                    this.world.addEffect(new DeathEffect(this.x, this.y, this.graphics.flipX, 45, -5.0f));
                }
            }
            this.world.removeSprite(this);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean fireballCollideCheck(final Fireball fireball) {
        if (!this.alive) {
            return false;
        }
        final float n = fireball.x - this.x;
        final float n2 = fireball.y - this.y;
        if (n <= -16.0f || n >= 16.0f || n2 <= -this.height || n2 >= fireball.height) {
            return false;
        }
        if (this.noFireballDeath) {
            return true;
        }
        this.xa = (float)(fireball.facing * 2);
        this.ya = -5.0f;
        this.world.addEvent(EventType.FIRE_KILL, this.type.getValue());
        if (this.graphics != null) {
            if (this.type == SpriteType.GREEN_KOOPA || this.type == SpriteType.GREEN_KOOPA_WINGED) {
                this.world.addEffect(new DeathEffect(this.x, this.y, this.graphics.flipX, 42, -5.0f));
            }
            else if (this.type == SpriteType.RED_KOOPA || this.type == SpriteType.RED_KOOPA_WINGED) {
                this.world.addEffect(new DeathEffect(this.x, this.y, this.graphics.flipX, 41, -5.0f));
            }
            else if (this.type == SpriteType.GOOMBA || this.type == SpriteType.GOOMBA_WINGED) {
                this.world.addEffect(new DeathEffect(this.x, this.y, this.graphics.flipX, 44, -5.0f));
            }
        }
        this.world.removeSprite(this);
        return true;
    }
    
    @Override
    public void bumpCheck(final int n, final int n2) {
        if (!this.alive) {
            return;
        }
        if (this.x + this.width > n * 16 && this.x - this.width < n * 16 + 16 && n2 == (int)((this.y - 1.0f) / 16.0f)) {
            this.xa = (float)(-this.world.mario.facing * 2);
            this.ya = -5.0f;
            if (this.graphics != null) {
                if (this.type == SpriteType.GREEN_KOOPA || this.type == SpriteType.GREEN_KOOPA_WINGED) {
                    this.world.addEffect(new DeathEffect(this.x, this.y, this.graphics.flipX, 42, -5.0f));
                }
                else if (this.type == SpriteType.RED_KOOPA || this.type == SpriteType.RED_KOOPA_WINGED) {
                    this.world.addEffect(new DeathEffect(this.x, this.y, this.graphics.flipX, 41, -5.0f));
                }
                else if (this.type == SpriteType.GOOMBA || this.type == SpriteType.GOOMBA_WINGED) {
                    this.world.addEffect(new DeathEffect(this.x, this.y, this.graphics.flipX, 44, -5.0f));
                }
                else if (this.type == SpriteType.SPIKY || this.type == SpriteType.SPIKY_WINGED) {
                    this.world.addEffect(new DeathEffect(this.x, this.y, this.graphics.flipX, 45, -5.0f));
                }
            }
            this.world.removeSprite(this);
        }
    }
    
    @Override
    public void render(final Graphics graphics) {
        if (this.winged && this.type != SpriteType.RED_KOOPA && this.type != SpriteType.GREEN_KOOPA && this.type != SpriteType.RED_KOOPA_WINGED && this.type != SpriteType.GREEN_KOOPA_WINGED) {
            this.wingGraphics.flipX = false;
            this.wingGraphics.render(graphics, (int)(this.x - this.world.cameraX - 6.0f), (int)(this.y - this.world.cameraY - 6.0f));
            this.wingGraphics.flipX = true;
            this.wingGraphics.render(graphics, (int)(this.x - this.world.cameraX + 22.0f), (int)(this.y - this.world.cameraY - 6.0f));
        }
        this.graphics.render(graphics, (int)(this.x - this.world.cameraX), (int)(this.y - this.world.cameraY));
        if (this.winged && (this.type == SpriteType.RED_KOOPA || this.type == SpriteType.GREEN_KOOPA || this.type == SpriteType.RED_KOOPA_WINGED || this.type == SpriteType.GREEN_KOOPA_WINGED)) {
            int n = -1;
            if (this.graphics.flipX) {
                n = 17;
            }
            this.wingGraphics.flipX = this.graphics.flipX;
            this.wingGraphics.render(graphics, (int)(this.x - this.world.cameraX + n), (int)(this.y - this.world.cameraY - 8.0f));
        }
    }
}
