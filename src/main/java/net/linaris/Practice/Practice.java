package net.linaris.Practice;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.linaris.Practice.database.Credentials;
import net.linaris.Practice.database.DatabaseConnector;
import net.linaris.Practice.handlers.duels.DuelCommand;
import net.linaris.Practice.handlers.games.GameTypesManager;
import net.linaris.Practice.handlers.kits.listeners.EditKitListener;
import net.linaris.Practice.handlers.maps.Map;
import net.linaris.Practice.handlers.maps.MapsManager;
import net.linaris.Practice.handlers.matchs.MatchsManager;
import net.linaris.Practice.handlers.matchs.commands.InventoryShowCommand;
import net.linaris.Practice.handlers.players.OmegaPlayer;
import net.linaris.Practice.handlers.players.OmegaPlayerManager;
import net.linaris.Practice.handlers.players.commands.FreezeCommand;
import net.linaris.Practice.handlers.players.commands.RankCommand;
import net.linaris.Practice.handlers.players.commands.SeekCommand;
import net.linaris.Practice.handlers.queues.QueuesManager;
import net.linaris.Practice.listeners.FreezeListener;
import net.linaris.Practice.listeners.PlayerListener;
import net.linaris.Practice.listeners.ScoreBoard;

public class Practice extends JavaPlugin
{
    private static Practice plugin;
    private Location lobbyLocation;
    private OmegaPlayerManager playerManager;
    
    public Practice() {
        Practice.plugin = this;
    }    
    
    public void onEnable() {
        final Credentials redisCrenditals = new Credentials("127.0.0.1", "unmmdp", 0);
        try {
            DatabaseConnector.setupConnection(redisCrenditals);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        final World world = Bukkit.getWorlds().get(0);
        (this.lobbyLocation = new Location(world, 107.514, 27, 14.573)).setYaw(24.9f);
        world.setGameRuleValue("doDaylightCycle", "false");
        world.setTime(6000L);
        world.setDifficulty(Difficulty.PEACEFUL);
        this.playerManager = new OmegaPlayerManager(this);
        GameTypesManager.init();
        MapsManager.init();
        MatchsManager.init();
        QueuesManager.init();
        this.getCommand("inventorybackup").setExecutor((CommandExecutor)new InventoryShowCommand());
        this.getCommand("duel").setExecutor((CommandExecutor)new DuelCommand());
        this.getCommand("rank").setExecutor((CommandExecutor)new RankCommand());
        this.getCommand("seek").setExecutor((CommandExecutor)new SeekCommand());
        this.getCommand("freeze").setExecutor((CommandExecutor)new FreezeCommand());
        this.getServer().getPluginManager().registerEvents((Listener)new PlayerListener(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new EditKitListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new FreezeListener(this), (Plugin)this);
		new ScoreBoard(this);
    }
    
    public void onDisable() {
        for (final OmegaPlayer player : this.playerManager) {
            player.save();
        }
        Bukkit.getScheduler().cancelAllTasks();
    }
    
    public static Practice getInstance() {
        return Practice.plugin;
    }
    
    public static void registerListener(final Listener listener) {
        Practice.plugin.getServer().getPluginManager().registerEvents(listener, (Plugin)Practice.plugin);
    }
    
    public Location getLobbyLocation() {
        return this.lobbyLocation;
    }
    
    public OmegaPlayerManager getPlayerManager() {
        return this.playerManager;
    }
    
    public static Map choose_map(String s)
    {
    	return (s.equalsIgnoreCase("builduhc") ? MapsManager.Maps.MAP_BUILDUHC : s.equalsIgnoreCase("potions") ? MapsManager.Maps.MAP_POTION  
    		: s.equalsIgnoreCase("archer") ? MapsManager.Maps.MAP_SNOWS : MapsManager.Maps.MAP_BUILDUHC);
    }
}
