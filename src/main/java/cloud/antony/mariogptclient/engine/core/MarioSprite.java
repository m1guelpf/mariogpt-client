package cloud.antony.mariogptclient.engine.core;

import cloud.antony.mariogptclient.engine.sprites.Fireball;
import cloud.antony.mariogptclient.engine.sprites.Mario;
import cloud.antony.mariogptclient.engine.sprites.Shell;
import java.awt.Graphics;
import cloud.antony.mariogptclient.engine.helper.SpriteType;

public abstract class MarioSprite
{
    public SpriteType type;
    public String initialCode;
    public float x;
    public float y;
    public float xa;
    public float ya;
    public int width;
    public int height;
    public int facing;
    public boolean alive;
    public MarioWorld world;
    
    public MarioSprite(final float x, final float y, final SpriteType type) {
        this.type = SpriteType.UNDEF;
        this.initialCode = "";
        this.x = x;
        this.y = y;
        this.xa = 0.0f;
        this.ya = 0.0f;
        this.facing = 1;
        this.alive = true;
        this.world = null;
        this.width = 16;
        this.height = 16;
        this.type = type;
    }
    
    public MarioSprite clone() {
        return null;
    }
    
    public void added() {
    }
    
    public void removed() {
    }
    
    public int getMapX() {
        return (int)(this.x / 16.0f);
    }
    
    public int getMapY() {
        return (int)(this.y / 16.0f);
    }
    
    public void render(final Graphics graphics) {
    }
    
    public void update() {
    }
    
    public void collideCheck() {
    }
    
    public void bumpCheck(final int n, final int n2) {
    }
    
    public boolean shellCollideCheck(final Shell shell) {
        return false;
    }
    
    public void release(final Mario mario) {
    }
    
    public boolean fireballCollideCheck(final Fireball fireball) {
        return false;
    }
}
