package cloud.antony.mariogptclient.engine.effects;

import cloud.antony.mariogptclient.engine.core.MarioEffect;

public class DeathEffect extends MarioEffect
{
    public DeathEffect(final float n, final float n2, final boolean flipX, final int n3, final float n4) {
        super(n, n2, 0.0f, n4, 0.0f, 1.0f, n3, 30);
        this.graphics.flipX = flipX;
    }
}
