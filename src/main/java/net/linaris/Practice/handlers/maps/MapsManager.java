package net.linaris.Practice.handlers.maps;

import java.util.*;

public class MapsManager
{
    private static final LinkedHashMap<String, Map> maps;
    
    public static void init() {
        Maps.MAP_BUILDUHC = registerMap(new MapBUILDUHC());
        Maps.MAP_POTION = registerMap(new MapPOTION());
        Maps.MAP_SNOWS = registerMap(new MapSnow());

    }
    
    private static Map registerMap(final Map map) {
        MapsManager.maps.put(map.getName(), map);
        return map;
    }
    
    public static Map randomMap() {
        return MapsManager.maps.get(new Random().nextInt(MapsManager.maps.size()));
    }
    
    static {
        maps = new LinkedHashMap<String, Map>();
    }
    
    public static class Maps
    {
        public static Map MAP_BUILDUHC;
        public static Map MAP_POTION;
        public static Map MAP_SNOWS;
    }
}
