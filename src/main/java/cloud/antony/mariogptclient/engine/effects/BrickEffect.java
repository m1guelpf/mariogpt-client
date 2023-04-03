package cloud.antony.mariogptclient.engine.effects;

import java.awt.Graphics;

import cloud.antony.mariogptclient.engine.core.MarioEffect;

public class BrickEffect extends MarioEffect
{
    public BrickEffect(final float n, final float n2, final float n3, final float n4) {
        super(n, n2, n3, n4, 0.0f, 3.0f, 16, 10);
    }
    
    @Override
    public void render(final Graphics graphics, final float n, final float n2) {
        this.graphics.index = this.startingIndex + this.life % 4;
        this.ya *= 0.95f;
        super.render(graphics, n, n2);
    }
}
