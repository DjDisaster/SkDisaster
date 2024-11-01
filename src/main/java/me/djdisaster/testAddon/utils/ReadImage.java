package me.djdisaster.testAddon.utils;

import org.bukkit.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ReadImage {

    private Color[][] colors;
    private boolean valid = false;
    public ReadImage(String path) {
        BufferedImage image;
        try {
            image = ImageIO.read(new File(path));
        } catch (Exception ignored) {
            return;
        }
        int width = image.getWidth();
        int height = image.getHeight();
        colors = new Color[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                java.awt.Color color = new java.awt.Color(rgb);
                colors[y][x] = Color.fromRGB(color.getRed(), color.getBlue(), color.getGreen());
            }
        }
        valid = true;
    }

    public boolean isValid() {
        return valid;
    }

    public Color getPixelAt(int x, int y) {
        if (!valid) {
            return null;
        }
        if (x >= 0 && x <= colors.length) {
            if (y >= 0 && y <= colors[0].length) {
                return colors[x][y];
            }
        }
        return null;
    }

}
