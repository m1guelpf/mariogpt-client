package cloud.antony.mariogptclient.engine.sprites;

import java.awt.Graphics;

import cloud.antony.mariogptclient.engine.core.MarioSprite;
import cloud.antony.mariogptclient.engine.core.MarioWorld;
import cloud.antony.mariogptclient.engine.graphics.MarioImage;
import cloud.antony.mariogptclient.engine.helper.MarioActions;
import cloud.antony.mariogptclient.engine.helper.EventType;
import cloud.antony.mariogptclient.engine.helper.TileFeature;
import cloud.antony.mariogptclient.engine.helper.Assets;
import cloud.antony.mariogptclient.engine.helper.SpriteType;

public class Mario extends MarioSprite
{
    public boolean isLarge;
    public boolean isFire;
    public boolean onGround;
    public boolean wasOnGround;
    public boolean isDucking;
    public boolean canShoot;
    public boolean mayJump;
    public boolean[] actions;
    public int jumpTime;
    private float xJumpSpeed;
    private float yJumpSpeed;
    private int invulnerableTime;
    private float marioFrameSpeed;
    private boolean oldLarge;
    private boolean oldFire;
    private MarioImage graphics;
    private float xJumpStart;
    private final float GROUND_INERTIA = 0.89f;
    private final float AIR_INERTIA = 0.89f;
    private final int POWERUP_TIME = 3;
    
    public Mario(final boolean b, final float n, final float n2) {
        super(n + 8.0f, n2 + 15.0f, SpriteType.MARIO);
        this.actions = null;
        this.jumpTime = 0;
        this.yJumpSpeed = 0.0f;
        this.invulnerableTime = 0;
        this.marioFrameSpeed = 0.0f;
        this.oldFire = false;
        this.graphics = null;
        this.xJumpStart = -100.0f;
        final boolean b2 = false;
        this.oldLarge = b2;
        this.isLarge = b2;
        final boolean b3 = false;
        this.oldFire = b3;
        this.isFire = b3;
        this.width = 4;
        this.height = 24;
        if (b) {
            this.graphics = new MarioImage(Assets.smallMario, 0);
        }
    }
    
    @Override
    public MarioSprite clone() {
        final Mario mario = new Mario(false, this.x - 8.0f, this.y - 15.0f);
        mario.xa = this.xa;
        mario.ya = this.ya;
        mario.initialCode = this.initialCode;
        mario.width = this.width;
        mario.height = this.height;
        mario.facing = this.facing;
        mario.isLarge = this.isLarge;
        mario.isFire = this.isFire;
        mario.wasOnGround = this.wasOnGround;
        mario.onGround = this.onGround;
        mario.isDucking = this.isDucking;
        mario.canShoot = this.canShoot;
        mario.mayJump = this.mayJump;
        mario.actions = new boolean[this.actions.length];
        for (int i = 0; i < this.actions.length; ++i) {
            mario.actions[i] = this.actions[i];
        }
        mario.xJumpSpeed = this.xJumpSpeed;
        mario.yJumpSpeed = this.yJumpSpeed;
        mario.invulnerableTime = this.invulnerableTime;
        mario.jumpTime = this.jumpTime;
        mario.xJumpStart = this.xJumpStart;
        return mario;
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
                this.jumpTime = 0;
                this.ya = 0.0f;
            }
            if (n2 > 0.0f) {
                this.y = (float)((int)((this.y - 1.0f) / 16.0f + 1.0f) * 16 - 1);
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
        final int block = this.world.level.getBlock(n5, n6);
        if (TileFeature.getTileType(block).contains(TileFeature.PICKABLE)) {
            this.world.addEvent(EventType.COLLECT, block);
            this.collectCoin();
            this.world.level.setBlock(n5, n6, 0);
        }
        if (blocking && n4 < 0.0f) {
            this.world.bump(n5, n6, this.isLarge);
        }
        return blocking;
    }
    
    public void updateGraphics() {
        if (!this.alive) {
            return;
        }
        boolean isLarge;
        boolean isFire;
        if (this.world.pauseTimer > 0) {
            isLarge = ((this.world.pauseTimer / 2 % 2 == 0) ? this.oldLarge : this.isLarge);
            isFire = ((this.world.pauseTimer / 2 % 2 == 0) ? this.oldFire : this.isFire);
        }
        else {
            isLarge = this.isLarge;
            isFire = this.isFire;
        }
        if (isLarge) {
            this.graphics.sheet = Assets.mario;
            if (isFire) {
                this.graphics.sheet = Assets.fireMario;
            }
            this.graphics.originX = 16;
            this.graphics.originY = 31;
            final MarioImage graphics = this.graphics;
            final MarioImage graphics2 = this.graphics;
            final int n = 32;
            graphics2.height = n;
            graphics.width = n;
        }
        else {
            this.graphics.sheet = Assets.smallMario;
            this.graphics.originX = 8;
            this.graphics.originY = 15;
            final MarioImage graphics3 = this.graphics;
            final MarioImage graphics4 = this.graphics;
            final int n2 = 16;
            graphics4.height = n2;
            graphics3.width = n2;
        }
        this.marioFrameSpeed += Math.abs(this.xa) + 5.0f;
        if (Math.abs(this.xa) < 0.5f) {
            this.marioFrameSpeed = 0.0f;
        }
        this.graphics.visible = ((this.invulnerableTime / 2 & 0x1) == 0x0);
        this.graphics.flipX = (this.facing == -1);
        int index;
        if (isLarge) {
            index = (int)(this.marioFrameSpeed / 20.0f) % 4;
            if (index == 3) {
                index = 1;
            }
            if (Math.abs(this.xa) > 10.0f) {
                index += 3;
            }
            if (!this.onGround) {
                if (Math.abs(this.xa) > 10.0f) {
                    index = 6;
                }
                else {
                    index = 5;
                }
            }
        }
        else {
            index = (int)(this.marioFrameSpeed / 20.0f) % 2;
            if (Math.abs(this.xa) > 10.0f) {
                index += 2;
            }
            if (!this.onGround) {
                if (Math.abs(this.xa) > 10.0f) {
                    index = 5;
                }
                else {
                    index = 4;
                }
            }
        }
        if (this.onGround && ((this.facing == -1 && this.xa > 0.0f) || (this.facing == 1 && this.xa < 0.0f)) && (this.xa > 1.0f || this.xa < -1.0f)) {
            index = (isLarge ? 8 : 7);
        }
        if (isLarge && this.isDucking) {
            index = 13;
        }
        this.graphics.index = index;
    }
    
    @Override
    public void update() {
        if (!this.alive) {
            return;
        }
        if (this.invulnerableTime > 0) {
            --this.invulnerableTime;
        }
        this.wasOnGround = this.onGround;
        final float n = this.actions[MarioActions.SPEED.getValue()] ? 1.2f : 0.6f;
        if (this.onGround) {
            this.isDucking = (this.actions[MarioActions.DOWN.getValue()] && this.isLarge);
        }
        if (this.isLarge) {
            this.height = (this.isDucking ? 12 : 24);
        }
        else {
            this.height = 12;
        }
        if (this.xa > 2.0f) {
            this.facing = 1;
        }
        if (this.xa < -2.0f) {
            this.facing = -1;
        }
        if (this.actions[MarioActions.JUMP.getValue()] || (this.jumpTime < 0 && !this.onGround)) {
            if (this.jumpTime < 0) {
                this.xa = this.xJumpSpeed;
                this.ya = -this.jumpTime * this.yJumpSpeed;
                ++this.jumpTime;
            }
            else if (this.onGround && this.mayJump) {
                this.xJumpSpeed = 0.0f;
                this.yJumpSpeed = -1.9f;
                this.jumpTime = 7;
                this.ya = this.jumpTime * this.yJumpSpeed;
                this.onGround = false;
                if (!this.isBlocking(this.x, this.y - 4.0f - this.height, 0.0f, -4.0f) && !this.isBlocking(this.x - this.width, this.y - 4.0f - this.height, 0.0f, -4.0f) && !this.isBlocking(this.x + this.width, this.y - 4.0f - this.height, 0.0f, -4.0f)) {
                    this.xJumpStart = this.x;
                    this.world.addEvent(EventType.JUMP, 0);
                }
            }
            else if (this.jumpTime > 0) {
                this.xa += this.xJumpSpeed;
                this.ya = this.jumpTime * this.yJumpSpeed;
                --this.jumpTime;
            }
        }
        else {
            this.jumpTime = 0;
        }
        if (this.actions[MarioActions.LEFT.getValue()] && !this.isDucking) {
            this.xa -= n;
            if (this.jumpTime >= 0) {
                this.facing = -1;
            }
        }
        if (this.actions[MarioActions.RIGHT.getValue()] && !this.isDucking) {
            this.xa += n;
            if (this.jumpTime >= 0) {
                this.facing = 1;
            }
        }
        if (this.actions[MarioActions.SPEED.getValue()] && this.canShoot && this.isFire && this.world.fireballsOnScreen < 2) {
            this.world.addSprite(new Fireball(this.graphics != null, this.x + this.facing * 6, this.y - 20.0f, this.facing));
        }
        this.canShoot = !this.actions[MarioActions.SPEED.getValue()];
        this.mayJump = (this.onGround && !this.actions[MarioActions.JUMP.getValue()]);
        if (Math.abs(this.xa) < 0.5f) {
            this.xa = 0.0f;
        }
        this.onGround = false;
        this.move(this.xa, 0.0f);
        this.move(0.0f, this.ya);
        if (!this.wasOnGround && this.onGround && this.xJumpStart >= 0.0f) {
            this.world.addEvent(EventType.LAND, 0);
            this.xJumpStart = -100.0f;
        }
        if (this.x < 0.0f) {
            this.x = 0.0f;
            this.xa = 0.0f;
        }
        if (this.x > this.world.level.exitTileX * 16) {
            this.x = (float)(this.world.level.exitTileX * 16);
            this.xa = 0.0f;
            this.world.win();
        }
        this.ya *= 0.85f;
        if (this.onGround) {
            this.xa *= 0.89f;
        }
        else {
            this.xa *= 0.89f;
        }
        if (!this.onGround) {
            this.ya += 3.0f;
        }
        if (this.graphics != null) {
            this.updateGraphics();
        }
    }
    
    public void stomp(final Enemy enemy) {
        if (!this.alive) {
            return;
        }
        this.move(0.0f, enemy.y - enemy.height / 2 - this.y);
        this.xJumpSpeed = 0.0f;
        this.yJumpSpeed = -1.9f;
        this.jumpTime = 8;
        this.ya = this.jumpTime * this.yJumpSpeed;
        this.onGround = false;
        this.invulnerableTime = 1;
    }
    
    public void stomp(final Shell shell) {
        if (!this.alive) {
            return;
        }
        this.move(0.0f, shell.y - shell.height / 2 - this.y);
        this.xJumpSpeed = 0.0f;
        this.yJumpSpeed = -1.9f;
        this.jumpTime = 8;
        this.ya = this.jumpTime * this.yJumpSpeed;
        this.onGround = false;
        this.invulnerableTime = 1;
    }
    
    public void getHurt() {
        if (this.invulnerableTime > 0 || !this.alive) {
            return;
        }
        if (this.isLarge) {
            this.world.pauseTimer = 9;
            this.oldLarge = this.isLarge;
            this.oldFire = this.isFire;
            if (this.isFire) {
                this.isFire = false;
            }
            else {
                this.isLarge = false;
            }
            this.invulnerableTime = 32;
        }
        else if (this.world != null) {
            this.world.lose();
        }
    }
    
    public void getFlower() {
        if (!this.alive) {
            return;
        }
        if (!this.isFire) {
            this.world.pauseTimer = 9;
            this.oldFire = this.isFire;
            this.oldLarge = this.isLarge;
            this.isFire = true;
            this.isLarge = true;
        }
        else {
            this.collectCoin();
        }
    }
    
    public void getMushroom() {
        if (!this.alive) {
            return;
        }
        if (!this.isLarge) {
            this.world.pauseTimer = 9;
            this.oldFire = this.isFire;
            this.oldLarge = this.isLarge;
            this.isLarge = true;
        }
        else {
            this.collectCoin();
        }
    }
    
    public void kick(final Shell shell) {
        if (!this.alive) {
            return;
        }
        this.invulnerableTime = 1;
    }
    
    public void stomp(final BulletBill bulletBill) {
        if (!this.alive) {
            return;
        }
        this.move(0.0f, bulletBill.y - bulletBill.height / 2 - this.y);
        this.xJumpSpeed = 0.0f;
        this.yJumpSpeed = -1.9f;
        this.jumpTime = 8;
        this.ya = this.jumpTime * this.yJumpSpeed;
        this.onGround = false;
        this.invulnerableTime = 1;
    }
    
    public String getMarioType() {
        if (this.isFire) {
            return "fire";
        }
        if (this.isLarge) {
            return "large";
        }
        return "small";
    }
    
    public void collect1Up() {
        if (!this.alive) {
            return;
        }
        final MarioWorld world = this.world;
        ++world.lives;
    }
    
    public void collectCoin() {
        if (!this.alive) {
            return;
        }
        final MarioWorld world = this.world;
        ++world.coins;
        if (this.world.coins % 100 == 0) {
            this.collect1Up();
        }
    }
    
    @Override
    public void render(final Graphics graphics) {
        super.render(graphics);
        this.graphics.render(graphics, (int)(this.x - this.world.cameraX), (int)(this.y - this.world.cameraY));
    }
}
