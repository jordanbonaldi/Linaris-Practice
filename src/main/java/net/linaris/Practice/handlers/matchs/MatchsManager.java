package net.linaris.Practice.handlers.matchs;

import java.util.*;
import org.bukkit.*;
import org.bukkit.generator.*;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.*;
import com.sk89q.worldedit.math.transform.*;

import net.linaris.Practice.*;
import net.linaris.Practice.database.models.*;
import net.linaris.Practice.handlers.games.*;
import net.linaris.Practice.handlers.maps.*;
import net.linaris.Practice.handlers.players.*;
import net.linaris.Practice.utils.*;

import com.boydti.fawe.object.schematic.*;

public class MatchsManager
{
    private static final Practice api;
    private static final LinkedHashMap<UUID, Match> matchs;
    private static int NEXT_X_ORIGIN;
    private static int NEXT_Z_ORIGIN;
    private static World world;
    
    public static LinkedHashMap<UUID, Match> getMatchs() {
        return MatchsManager.matchs;
    }
    
    public static void init() {
        final WorldCreator creator = new WorldCreator("matchs");
        creator.generator((ChunkGenerator)new VoidWorldGenerator());
        (MatchsManager.world = MatchsManager.api.getServer().createWorld(creator)).setGameRuleValue("doDaylightCycle", "false");
        MatchsManager.world.setTime(1200L);
    }
    
    public static SoloMatch createSoloMatch(final Map map, final GameType type, final boolean ranked, final OmegaPlayer playerA, final OmegaPlayer playerB) {
        try {
            final SoloMatch match = new SoloMatch(MatchsManager.world, type, map, ranked, playerA, playerB);
            registerMatch(match);
            createMap(match);
            return match;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void registerMatch(final Match match) {
        match.setOrigin(new WrappedLocation(MatchsManager.NEXT_X_ORIGIN, 20.0, MatchsManager.NEXT_Z_ORIGIN, 0.0f, 0.0f));
        MatchsManager.matchs.put(match.getId(), match);
        updateNextOrigin();
    }
    
    public static WrappedLocation registerArena() {
        final WrappedLocation location = new WrappedLocation(MatchsManager.NEXT_X_ORIGIN, 20.0, MatchsManager.NEXT_Z_ORIGIN, 0.0f, 0.0f);
        updateNextOrigin();
        return location;
    }
    
    public static void createMap(final Match match) {
        final Vector location = new Vector(match.getOrigin().getX(), match.getOrigin().getY(), match.getOrigin().getZ());
        final Schematic schem = match.getMap().getSchematic();
        schem.paste((com.sk89q.worldedit.world.World)BukkitUtil.getLocalWorld(MatchsManager.world), location, false, false, (Transform)null);
    }
    
    public static MatchModel getModel(final UUID uuid) {
        if (!MatchsManager.matchs.containsKey(uuid)) {
            return null;
        }
        return MatchsManager.matchs.get(uuid).getModel();
    }
    
    public static void updateNextOrigin() {
        if (MatchsManager.NEXT_X_ORIGIN >= 3000000) {
            MatchsManager.NEXT_Z_ORIGIN += 600;
            MatchsManager.NEXT_X_ORIGIN = 0;
        }
        else {
            MatchsManager.NEXT_X_ORIGIN += 600;
        }
        System.out.println(MatchsManager.NEXT_X_ORIGIN + " : " + MatchsManager.NEXT_Z_ORIGIN);
    }
    
    static {
        api = Practice.getInstance();
        matchs = new LinkedHashMap<UUID, Match>();
        MatchsManager.NEXT_X_ORIGIN = 0;
        MatchsManager.NEXT_Z_ORIGIN = 0;
    }
}
