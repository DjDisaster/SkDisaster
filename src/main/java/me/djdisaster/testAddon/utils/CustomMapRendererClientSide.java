package me.djdisaster.testAddon.utils;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class CustomMapRendererClientSide extends MapRenderer {


    private HashMap<Player, MapData> mapDataMap = new HashMap<>();


    @Override
    public void render(@NotNull MapView mapView, @NotNull MapCanvas mapCanvas, @NotNull Player player) {
        if (mapDataMap.containsKey(player)) {
            MapData mapData = mapDataMap.get(player);
            mapData.draw(mapCanvas);
        }
    }

    public MapData getMapData(Player player) {
        return mapDataMap.getOrDefault(player, new MapData());
    }

}