package net.linaris.Practice.handlers.maps;

import net.linaris.Practice.handlers.games.*;

public class Map16 extends Map
{
    public Map16() {
        super(0, "Map 16", 0, 0, 0);
        this.addSpawn(new WrappedLocation(3.5, 3.0, 3.5, -55.0f, 0.0f));
        this.addSpawn(new WrappedLocation(96.5, 3.0, 71.5, 125.0f, 0.0f));
    }
}
