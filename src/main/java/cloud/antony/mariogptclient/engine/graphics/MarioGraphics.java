package cloud.antony.mariogptclient.engine.graphics;

import java.awt.Graphics;

public abstract class MarioGraphics
{
    public boolean visible;
    public float alpha;
    public int originX;
    public int originY;
    public boolean flipX;
    public boolean flipY;
    public int width;
    public int height;
    
    public MarioGraphics() {
        this.visible = true;
        this.alpha = 1.0f;
        final int n = 0;
        this.originY = n;
        this.originX = n;
        final boolean b = false;
        this.flipY = b;
        this.flipX = b;
        final int n2 = 32;
        this.height = n2;
        this.width = n2;
    }
    
    public abstract void render(final Graphics p0, final int p1, final int p2);
}
