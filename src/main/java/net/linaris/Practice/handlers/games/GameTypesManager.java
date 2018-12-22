package net.linaris.Practice.handlers.games;

import java.util.*;

import net.linaris.Practice.handlers.games.types.*;

public class GameTypesManager
{
    private static final LinkedHashMap<String, GameType> types;
    private static final LinkedHashMap<Integer, GameType> idToTypes;
    
    public static void init() {
        Types.BUILDUHC = registerGameType(new BuildUHCType());
        Types.POTIONS = registerGameType(new PotionsType());
        Types.ARCHER = registerGameType(new SnowType());
    }
    
    private static GameType registerGameType(final GameType type) {
        GameTypesManager.types.put(type.getName(), type);
        GameTypesManager.idToTypes.put(type.getId(), type);
        return type;
    }
    
    public static LinkedHashMap<String, GameType> getTypes() {
        return GameTypesManager.types;
    }
    
    public static LinkedHashMap<Integer, GameType> getIdToTypes() {
        return GameTypesManager.idToTypes;
    }
    
    static {
        types = new LinkedHashMap<String, GameType>();
        idToTypes = new LinkedHashMap<Integer, GameType>();
    }
    
    public static class Types
    {
        public static GameType BUILDUHC;
        public static GameType POTIONS;
        public static GameType ARCHER;
    }
}
