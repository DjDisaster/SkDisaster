package me.djdisaster.skDisaster.utils.maps;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

public class CustomMapRenderer extends MapRenderer {


    private MapData mapData;

    public CustomMapRenderer(MapData mapData) {
        this.mapData = mapData;
    }


    @Override
    public void render(@NotNull MapView mapView, @NotNull MapCanvas mapCanvas, @NotNull Player player) {
        mapData.draw(mapCanvas);
    }

    public MapData getMapData() {
        return mapData;
    }

}