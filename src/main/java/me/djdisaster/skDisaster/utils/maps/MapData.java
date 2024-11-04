package me.djdisaster.skDisaster.utils.maps;

import org.bukkit.map.MapCanvas;

import java.awt.*;

public class MapData {

    private Color[][] colors = new Color[128][128];

    public MapData() {
        setAllPixels(new Color(0, 0, 0, 0));
    }

    public void setAllPixels(Color color) {
        for (int i = 0; i < 128; i++) {
            for (int n = 0; n < 128; n++) {
                colors[i][n] = color;
            }
        }
    }

    public void draw(MapCanvas canvas) {
        for (int i = 0; i < 128; i++) {
            for (int n = 0; n < 128; n++) {
                Color color = colors[i][n];
                if (canvas.getPixelColor(i, n) != color) {
                    canvas.setPixelColor(i, n, color);
                }
            }
        }
    }

    public void setPixel(int x, int y, Color color) {
        colors[x][y] = color;
    }

}
