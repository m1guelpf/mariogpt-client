package cloud.antony.mariogptclient.engine.core;

import java.awt.event.FocusEvent;
import java.awt.Graphics;
import java.awt.Image;
import cloud.antony.mariogptclient.engine.helper.Assets;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.event.FocusListener;
import javax.swing.JComponent;

public class MarioRender extends JComponent implements FocusListener
{
    private static final long serialVersionUID = 790878775993203817L;
    public static final int TICKS_PER_SECOND = 24;
    private float scale;
    private GraphicsConfiguration graphicsConfiguration;
    int frame;
    Thread animator;
    boolean focused;
    
    public MarioRender(final float scale) {
        this.setFocusable(true);
        this.setEnabled(true);
        this.scale = scale;
        final Dimension maximumSize = new Dimension((int)(256.0f * scale), (int)(240.0f * scale));
        this.setPreferredSize(maximumSize);
        this.setMinimumSize(maximumSize);
        this.setMaximumSize(maximumSize);
        this.setFocusable(true);
    }
    
    public void init() {
        Assets.init(this.graphicsConfiguration = this.getGraphicsConfiguration());
    }
    
    public void init(final String s) {
        Assets.init(this.graphicsConfiguration = this.getGraphicsConfiguration(), s);
    }
    
    public void renderWorld(final MarioWorld marioWorld, final Image image, final Graphics graphics, final Graphics graphics2) {
        graphics2.fillRect(0, 0, 256, 240);
        marioWorld.render(graphics2);
        this.drawStringDropShadow(graphics2, "Lives: " + marioWorld.lives, 0, 0, 7);
        this.drawStringDropShadow(graphics2, "Coins: " + marioWorld.coins, 11, 0, 7);
        this.drawStringDropShadow(graphics2, "Time: " + ((marioWorld.currentTimer == -1) ? "Inf" : Integer.valueOf((int)Math.ceil(marioWorld.currentTimer / 1000.0f))), 22, 0, 7);
        if (this.scale > 1.0f) {
            graphics.drawImage(image, 0, 0, (int)(256.0f * this.scale), (int)(240.0f * this.scale), null);
        }
        else {
            graphics.drawImage(image, 0, 0, null);
        }
    }
    
    public void drawStringDropShadow(final Graphics graphics, final String s, final int n, final int n2, final int n3) {
        this.drawString(graphics, s, n * 8 + 5, n2 * 8 + 5, 0);
        this.drawString(graphics, s, n * 8 + 4, n2 * 8 + 4, n3);
    }
    
    private void drawString(final Graphics graphics, final String s, final int n, final int n2, final int n3) {
        final char[] charArray = s.toCharArray();
        for (int i = 0; i < charArray.length; ++i) {
            graphics.drawImage(Assets.font[charArray[i] - ' '][n3], n + i * 8, n2, null);
        }
    }
    
    @Override
    public void focusGained(final FocusEvent focusEvent) {
        this.focused = true;
    }
    
    @Override
    public void focusLost(final FocusEvent focusEvent) {
        this.focused = false;
    }
}
