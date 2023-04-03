package cloud.antony.mariogptclient.engine.effects;

import java.awt.Graphics;

import cloud.antony.mariogptclient.engine.core.MarioEffect;

public class FireballEffect extends MarioEffect
{
    public FireballEffect(final float n, final float n2) {
        super(n, n2, 0.0f, 0.0f, 0.0f, 0.0f, 32, 8);
    }
    
    @Override
    public void render(final Graphics graphics, final float n, final float n2) {
        this.graphics.index = this.startingIndex + (8 - this.life);
        super.render(graphics, n, n2);
    }
}
