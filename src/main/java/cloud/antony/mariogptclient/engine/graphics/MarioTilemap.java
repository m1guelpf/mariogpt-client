package cloud.antony.mariogptclient.engine.graphics;

import cloud.antony.mariogptclient.engine.helper.TileFeature;
import java.awt.Graphics;
import java.awt.Image;

public class MarioTilemap extends MarioGraphics
{
    public Image[][] sheet;
    public int[][] currentIndeces;
    public int[][] indexShift;
    public float[][] moveShift;
    public int animationIndex;
    
    public MarioTilemap(final Image[][] sheet, final int[][] currentIndeces) {
        this.sheet = sheet;
        this.currentIndeces = currentIndeces;
        this.indexShift = new int[currentIndeces.length][currentIndeces[0].length];
        this.moveShift = new float[currentIndeces.length][currentIndeces[0].length];
        this.animationIndex = 0;
    }
    
    @Override
    public void render(final Graphics graphics, final int n, final int n2) {
        this.animationIndex = (this.animationIndex + 1) % 5;
        final int n3 = n / 16 - 1;
        final int n4 = n2 / 16 - 1;
        final int n5 = (n + 256) / 16 + 1;
        final int n6 = (n2 + 256) / 16 + 1;
        for (int i = n3; i <= n5; ++i) {
            for (int j = n4; j <= n6; ++j) {
                if (i >= 0 && j >= 0 && i < this.currentIndeces.length) {
                    if (j < this.currentIndeces[0].length) {
                        if (this.moveShift[i][j] > 0.0f) {
                            final float[] array = this.moveShift[i];
                            final int n7 = j;
                            --array[n7];
                            if (this.moveShift[i][j] < 0.0f) {
                                this.moveShift[i][j] = 0.0f;
                            }
                        }
                        if (TileFeature.getTileType(this.currentIndeces[i][j]).contains(TileFeature.ANIMATED)) {
                            if (this.animationIndex == 0) {
                                this.indexShift[i][j] = (this.indexShift[i][j] + 1) % 3;
                            }
                        }
                        else {
                            this.indexShift[i][j] = 0;
                        }
                        final int n8 = this.currentIndeces[i][j] + this.indexShift[i][j];
                        graphics.drawImage(this.sheet[n8 % 8][n8 / 8], i * 16 - n, j * 16 - n2 - (int)this.moveShift[i][j], null);
                    }
                }
            }
        }
    }
}
