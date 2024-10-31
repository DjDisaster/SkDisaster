package me.djdisaster.testAddon.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.*;

public class CustomMap {

    private boolean isClientSide;
    private MapRenderer renderer;
    private int id;
    private ItemStack mapItem;
    public CustomMap(boolean isClientSide) {

        if (isClientSide) {
            renderer = new CustomMapRendererClientSide();
        } else {
            renderer = new CustomMapRenderer(new MapData());
        }

        mapItem = new ItemStack(Material.FILLED_MAP);
        MapMeta mapMeta = (MapMeta) mapItem.getItemMeta();
        MapView mapView = Bukkit.getServer().createMap(Bukkit.getWorlds().getFirst());
        mapView.getRenderers().clear();
        mapView.addRenderer(renderer);
        mapMeta.setMapView(mapView);
        mapItem.setItemMeta(mapMeta);


    }
    public ItemStack getItem() {
        return mapItem;
    }

    // Should not be called with client side maps.
    public void setPixel(int x, int y, int r, int g, int b) {
        if (!isClientSide) {
            ((CustomMapRenderer) renderer).getMapData().setPixel(x, y, new Color(r,g,b));
        }
    }
    public void setAllPixels(int r, int g, int b) {
        if (!isClientSide) {
            ((CustomMapRenderer) renderer).getMapData().setAllPixels(new Color(r,g,b));
        }
    }



    // Should only be called with client side maps.
    public void setPixel(Player player, int x, int y, int r, int g, int b) {
        if (!isClientSide) {
            ((CustomMapRenderer) renderer).getMapData().setPixel(x, y, new Color(r,g,b));
        }
    }

}
