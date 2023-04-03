package cloud.antony.mariogptclient.engine.core;

import java.awt.Graphics;
import cloud.antony.mariogptclient.engine.helper.Assets;
import cloud.antony.mariogptclient.engine.graphics.MarioImage;

public abstract class MarioEffect
{
    public float x;
    public float y;
    public float xv;
    public float yv;
    public float xa;
    public float ya;
    public int life;
    public int startingIndex;
    protected MarioImage graphics;
    
    public MarioEffect(final float x, final float y, final float xv, final float yv, final float xa, final float ya, final int startingIndex, final int life) {
        this.x = x;
        this.y = y;
        this.xv = xv;
        this.yv = yv;
        this.xa = xa;
        this.ya = ya;
        this.life = life;
        this.graphics = new MarioImage(Assets.particles, startingIndex);
        this.graphics.width = 16;
        this.graphics.height = 16;
        this.graphics.originX = 8;
        this.graphics.originY = 8;
        this.startingIndex = startingIndex;
    }
    
    public void render(final Graphics graphics, final float n, final float n2) {
        if (this.life <= 0) {
            return;
        }
        --this.life;
        this.x += this.xv;
        this.y += this.yv;
        this.xv += this.xa;
        this.yv += this.ya;
        this.graphics.render(graphics, (int)(this.x - n), (int)(this.y - n2));
    }
}
