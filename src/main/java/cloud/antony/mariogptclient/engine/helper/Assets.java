package cloud.antony.mariogptclient.engine.helper;

import java.io.IOException;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import javax.imageio.ImageReader;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.GraphicsConfiguration;
import java.awt.Image;

public class Assets
{
    public static Image[][] mario;
    public static Image[][] smallMario;
    public static Image[][] fireMario;
    public static Image[][] enemies;
    public static Image[][] items;
    public static Image[][] level;
    public static Image[][] particles;
    public static Image[][] font;
    public static Image[][] map;
    static final String curDir;
    static final String img;
    
    public static void init(final GraphicsConfiguration graphicsConfiguration) {
        System.out.println("Using default img dir!");
        init(graphicsConfiguration, Assets.img);
    }
    
    public static void init(final GraphicsConfiguration graphicsConfiguration, final String str) {
        System.out.println("Using img directory: " + str);
        try {
            Assets.mario = cutImage(graphicsConfiguration, "mariosheet.png", 32, 32, str);
            Assets.smallMario = cutImage(graphicsConfiguration, "smallmariosheet.png", 16, 16, str);
            Assets.fireMario = cutImage(graphicsConfiguration, "firemariosheet.png", 32, 32, str);
            Assets.enemies = cutImage(graphicsConfiguration, "enemysheet.png", 16, 32, str);
            Assets.items = cutImage(graphicsConfiguration, "itemsheet.png", 16, 16, str);
            Assets.level = cutImage(graphicsConfiguration, "mapsheet.png", 16, 16, str);
            Assets.particles = cutImage(graphicsConfiguration, "particlesheet.png", 16, 16, str);
            Assets.font = cutImage(graphicsConfiguration, "font.gif", 8, 8, str);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private static Image getImage(final GraphicsConfiguration graphicsConfiguration, String string, final String str) throws IOException {
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(Assets.class.getResourceAsStream(string));
        }
        catch (Exception ex) {}
        if (bufferedImage == null) {
            string = str + string;
            bufferedImage = ImageIO.read(new File(string));
        }
        if (bufferedImage == null) {
            final ImageInputStream imageInputStream = ImageIO.createImageInputStream(new File(string));
            final ImageReader imageReader = ImageIO.getImageReadersBySuffix(string.substring(string.length() - 3)).next();
            imageReader.setInput(imageInputStream, true);
            bufferedImage = imageReader.read(0);
        }
        final BufferedImage compatibleImage = graphicsConfiguration.createCompatibleImage(bufferedImage.getWidth(), bufferedImage.getHeight(), 2);
        final Graphics2D graphics2D = (Graphics2D)compatibleImage.getGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        graphics2D.drawImage(bufferedImage, 0, 0, null);
        graphics2D.dispose();
        return compatibleImage;
    }
    
    private static Image[][] cutImage(final GraphicsConfiguration graphicsConfiguration, final String s, final int width, final int height, final String s2) throws IOException {
        final Image image = getImage(graphicsConfiguration, s, s2);
        final Image[][] array = new Image[image.getWidth(null) / width][image.getHeight(null) / height];
        for (int i = 0; i < image.getWidth(null) / width; ++i) {
            for (int j = 0; j < image.getHeight(null) / height; ++j) {
                final BufferedImage compatibleImage = graphicsConfiguration.createCompatibleImage(width, height, 2);
                final Graphics2D graphics2D = (Graphics2D)compatibleImage.getGraphics();
                graphics2D.setComposite(AlphaComposite.Src);
                graphics2D.drawImage(image, -i * width, -j * height, null);
                graphics2D.dispose();
                array[i][j] = compatibleImage;
            }
        }
        return array;
    }
    
    static {
        curDir = System.getProperty("user.dir");
        img = Assets.curDir + "/img/";
    }
}
