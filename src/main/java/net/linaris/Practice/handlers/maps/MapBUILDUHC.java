package net.linaris.Practice.handlers.maps;

import net.linaris.Practice.handlers.games.*;

public class MapBUILDUHC extends Map
{
    public MapBUILDUHC() {
        super(0, "Map Sand", 60, 21, 60);
        this.addSpawn(new WrappedLocation(2.5, 5.0, 57.5, -135.0f, 0.0f));
        this.addSpawn(new WrappedLocation(57.5, 5.0, 2.5, 45.0f, 0.0f));
    }
}
