package cloud.antony.mariogptclient.engine.graphics;

import java.awt.Graphics;
import java.awt.Image;

public class MarioImage extends MarioGraphics
{
    public Image[][] sheet;
    public int index;
    
    public MarioImage(final Image[][] sheet, final int index) {
        this.sheet = sheet;
        this.index = index;
    }
    
    @Override
    public void render(final Graphics graphics, final int n, final int n2) {
        if (!this.visible) {
            return;
        }
        graphics.drawImage(this.sheet[this.index % this.sheet.length][this.index / this.sheet.length], n - this.originX + (this.flipX ? this.width : 0), n2 - this.originY + (this.flipY ? this.height : 0), this.flipX ? (-this.width) : this.width, this.flipY ? (-this.height) : this.height, null);
    }
}
