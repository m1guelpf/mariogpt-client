package cloud.antony.mariogptclient.engine.sprites;

import cloud.antony.mariogptclient.engine.core.MarioSprite;
import cloud.antony.mariogptclient.engine.graphics.MarioImage;
import cloud.antony.mariogptclient.engine.helper.EventType;
import cloud.antony.mariogptclient.engine.effects.DeathEffect;
import java.awt.Graphics;
import cloud.antony.mariogptclient.engine.helper.Assets;
import cloud.antony.mariogptclient.engine.helper.SpriteType;

public class Shell extends MarioSprite
{
    private static final float GROUND_INERTIA = 0.89f;
    private static final float AIR_INERTIA = 0.89f;
    private int shellType;
    private boolean onGround;
    private MarioImage graphics;
    
    public Shell(final boolean b, final float n, final float n2, final int shellType, final String initialCode) {
        super(n, n2, SpriteType.SHELL);
        this.shellType = 0;
        this.onGround = false;
        this.width = 4;
        this.height = 12;
        this.facing = 0;
        this.ya = -5.0f;
        this.shellType = shellType;
        this.initialCode = initialCode;
        if (b) {
            this.graphics = new MarioImage(Assets.enemies, shellType * 8 + 3);
            this.graphics.originX = 8;
            this.graphics.originY = 31;
            this.graphics.width = 16;
        }
    }
    
    @Override
    public MarioSprite clone() {
        final Shell shell = new Shell(false, this.x, this.y, this.shellType, this.initialCode);
        shell.xa = this.xa;
        shell.ya = this.ya;
        shell.width = this.width;
        shell.height = this.height;
        shell.facing = this.facing;
        shell.onGround = this.onGround;
        return shell;
    }
    
    @Override
    public void update() {
        if (!this.alive) {
            return;
        }
        super.update();
        final float n = 11.0f;
        if (this.xa > 2.0f) {
            this.facing = 1;
        }
        if (this.xa < -2.0f) {
            this.facing = -1;
        }
        this.xa = this.facing * n;
        if (this.facing != 0) {
            this.world.checkShellCollide(this);
        }
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
    public boolean fireballCollideCheck(final Fireball fireball) {
        if (!this.alive) {
            return false;
        }
        final float n = fireball.x - this.x;
        final float n2 = fireball.y - this.y;
        if (n <= -16.0f || n >= 16.0f || n2 <= -this.height || n2 >= fireball.height) {
            return false;
        }
        if (this.facing != 0) {
            return true;
        }
        this.xa = (float)(fireball.facing * 2);
        this.ya = -5.0f;
        if (this.graphics != null) {
            this.world.addEffect(new DeathEffect(this.x, this.y, this.graphics.flipX, 41 + this.shellType, -5.0f));
        }
        this.world.removeSprite(this);
        return true;
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
                if (this.facing != 0) {
                    this.xa = 0.0f;
                    this.facing = 0;
                }
                else {
                    this.facing = this.world.mario.facing;
                }
            }
            else if (this.facing != 0) {
                this.world.addEvent(EventType.HURT, this.type.getValue());
                this.world.mario.getHurt();
            }
            else {
                this.world.addEvent(EventType.KICK, this.type.getValue());
                this.world.mario.kick(this);
                this.facing = this.world.mario.facing;
            }
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
    
    private boolean isBlocking(final float n, final float n2, final float n3, final float n4) {
        final int n5 = (int)(n / 16.0f);
        final int n6 = (int)(n2 / 16.0f);
        if (n5 == (int)(this.x / 16.0f) && n6 == (int)(this.y / 16.0f)) {
            return false;
        }
        final boolean blocking = this.world.level.isBlocking(n5, n6, n3, n4);
        if (blocking && n4 == 0.0f && n3 != 0.0f) {
            this.world.bump(n5, n6, true);
        }
        return blocking;
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
    
    @Override
    public boolean shellCollideCheck(final Shell shell) {
        if (!this.alive) {
            return false;
        }
        final float n = shell.x - this.x;
        final float n2 = shell.y - this.y;
        if (n > -16.0f && n < 16.0f && n2 > -this.height && n2 < shell.height) {
            this.world.addEvent(EventType.SHELL_KILL, this.type.getValue());
            if (this != shell) {
                this.world.removeSprite(shell);
            }
            this.world.removeSprite(this);
            return true;
        }
        return false;
    }
}
