package me.djdisaster.skDisaster.utils.maps;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CustomMap {

    private boolean isClientSide;
    private MapRenderer renderer;
    private int id;
    private ItemStack mapItem;
    private MapView mapView;
    public CustomMap(boolean isClientSide) {

        if (isClientSide) {
            renderer = new CustomMapRendererClientSide();
        } else {
            renderer = new CustomMapRenderer(new MapData());
        }

        mapItem = new ItemStack(Material.FILLED_MAP);
        MapMeta mapMeta = (MapMeta) mapItem.getItemMeta();
        mapView = Bukkit.getServer().createMap(Bukkit.getWorlds().getFirst());
        mapView.getRenderers().clear();
        mapView.addRenderer(renderer);
        mapMeta.setMapView(mapView);
        mapItem.setItemMeta(mapMeta);


    }

    public MapView getView() {
        return mapView;
    }

    public ItemStack getItem() {
        return mapItem;
    }

    // Should not be called with client side maps.
    public void setPixel(int x, int y, int r, int g, int b, int alpha) {
        if (!isClientSide) {
            ((CustomMapRenderer) renderer).getMapData().setPixel(x, y, new Color(r, g, b, alpha));
        }
    }

    public void setAllPixels(int r, int g, int b, int alpha) {
        if (!isClientSide) {
            ((CustomMapRenderer) renderer).getMapData().setAllPixels(new Color(r, g, b, alpha));
        }
    }


    // Should only be called with client side maps.
    public void setPixel(Player player, int x, int y, int r, int g, int b, int alpha) {
        if (!isClientSide) {
            ((CustomMapRenderer) renderer).getMapData().setPixel(x, y, new Color(r, g, b, alpha));
        }
    }

    public void drawLine(int x1, int y1, int x2, int y2, int r, int g, int b, int alpha) {
        List<Integer> xPositions = new ArrayList<>();
        List<Integer> yPositions = new ArrayList<>();

        int dx = Math.abs(x1 - x2);
        int dy = Math.abs(y1 - y2);

        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;

        int err = dx - dy;

        if (
                x1 >= 0 && x1 < 128 &&
                x2 >= 0 && x2 < 128 &&
                y1 >= 0 && y1 < 128 &&
                y2 >= 0 && y2 < 128
        ) {
            for (int i = 0; i < 128; i++) {
                xPositions.add(x1);
                yPositions.add(y1);

                if (x1 == x2 && y1 == y2) {
                    break;
                }

                int e2 = err * 2;
                if (e2 > -dy) {
                    err -= dy;
                    x1 += sx;
                }
                if (e2 < dx) {
                    err += dx;
                    y1 += sy;
                }
            }
        }

        for (int i = 0; i < xPositions.size(); i++) {
            int x = xPositions.get(i);
            int y = yPositions.get(i);
            setPixel(x, y,r,g,b,alpha);
        }
    }

    public void drawSquareSolid(int x1, int y1, int x2, int y2, int r, int g, int b, int alpha) {
        for (int x = x1; x < x2; x++) {
            for (int y = y1; y < y2; y++) {
                setPixel(x,y,r,g,b,alpha);
            }
        }
    }

    public void drawSquare(int x1, int y1, int x2, int y2, int r, int g, int b, int alpha, boolean solid) {

        if (
                x1 < 0 || x1 >= 128 ||
                x2 < 0 || x2 >= 128 ||
                y1 < 0 || y1 >= 128 ||
                y2 < 0 || y2 >= 128
        ) {
            return;
        }

        if (solid) {
            drawSquareSolid(x1,y1,x2,y2,r,g,b,alpha);
            return;
        }

        // Top Left -> Top Right
        drawLine(x1, y1, x2, y1,r,g,b,alpha);
        // Top Right to Bottom Right
        drawLine(x2, y1, x2, y2,r,g,b,alpha);
        // Bottom Right to Bottom Left
        drawLine(x2, y2, x1, y2,r,g,b,alpha);
        // Bottom Left to Top Left
        drawLine(x1, y2, x1, y1,r,g,b,alpha);


    }

}
