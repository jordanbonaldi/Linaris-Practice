package net.linaris.Practice.handlers.maps;

import net.linaris.Practice.handlers.games.*;

public class MapSnow extends Map
{
    public MapSnow() {
        super(3, "Map Archer", 0, 0, 0);
        this.addSpawn(new WrappedLocation(35, 3.0, 11, -0.3f, -1.1f));
        this.addSpawn(new WrappedLocation(35, 3.0, 93 - 11, 179.0f, 0.4f));
    }
}
