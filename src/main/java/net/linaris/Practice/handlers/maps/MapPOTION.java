package net.linaris.Practice.handlers.maps;

import net.linaris.Practice.handlers.games.*;

public class MapPOTION extends Map
{
    public MapPOTION() {
        super(2, "Map Potion", 0, 0, 0);
        this.addSpawn(new WrappedLocation(33, 5.0, 21, 0.3f, 0.5f));
        this.addSpawn(new WrappedLocation(33, 5.0, 96, 180.0f, -1.5f));
    }
}
