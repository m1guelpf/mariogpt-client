package cloud.antony.mariogptclient.engine.graphics;

import java.awt.Graphics;

import cloud.antony.mariogptclient.engine.helper.Assets;
import java.awt.Color;
import java.awt.AlphaComposite;
import java.awt.GraphicsConfiguration;
import java.awt.Graphics2D;
import java.awt.Image;

public class MarioBackground extends MarioGraphics
{
    private Image image;
    private Graphics2D g;
    private int screenWidth;
    
    public MarioBackground(final GraphicsConfiguration graphicsConfiguration, final int screenWidth, final int[][] array) {
        this.width = array[0].length * 16;
        this.height = array.length * 16;
        this.screenWidth = screenWidth;
        this.image = graphicsConfiguration.createCompatibleImage(this.width, this.height, 2);
        (this.g = (Graphics2D)this.image.getGraphics()).setComposite(AlphaComposite.Src);
        this.updateArea(array);
    }
    
    private void updateArea(final int[][] array) {
        this.g.setBackground(new Color(0, 0, 0, 0));
        this.g.clearRect(0, 0, this.width, this.height);
        for (int i = 0; i < array[0].length; ++i) {
            for (int j = 0; j < array.length; ++j) {
                this.g.drawImage(Assets.level[array[j][i] % 8][array[j][i] / 8], i * 16, j * 16, 16, 16, null);
            }
        }
    }
    
    @Override
    public void render(final Graphics graphics, final int n, final int n2) {
        final int n3 = n % this.width;
        for (int i = -1; i < this.screenWidth / this.width + 1; ++i) {
            graphics.drawImage(this.image, -n3 + i * this.width, 0, null);
        }
    }
}
