package net.linaris.Practice.handlers.matchs;

import org.bukkit.*;

import java.lang.reflect.*;

import org.bukkit.plugin.*;
import org.bukkit.event.*;

import java.util.*;

import net.linaris.Practice.*;
import net.linaris.Practice.database.models.*;
import net.linaris.Practice.handlers.games.*;
import net.linaris.Practice.handlers.maps.*;
import net.linaris.Practice.handlers.players.*;
import net.linaris.Practice.handlers.states.*;
import net.linaris.Practice.handlers.states.states.*;
import net.md_5.bungee.api.chat.*;
import org.bukkit.entity.*;

public abstract class Match implements Listener
{
    private UUID id;
    private boolean ranked;
    private WrappedLocation origin;
    private World world;
    private GameType type;
    private GameTypeListener listener;
    private Map map;
    private LinkedHashMap<String, OmegaPlayer> ingames;
    private LinkedHashMap<String, OmegaPlayer> spectators;
    private long startTime;
    protected MatchModel model;
    private int taskId;
    
    public Match(final World world, final GameType type, final Map map, final boolean ranked) throws Exception {
        this.id = UUID.randomUUID();
        while (MatchsManager.getMatchs().containsKey(this.id)) {
            this.id = UUID.randomUUID();
        }
        this.world = world;
        this.type = type;
        this.map = map;
        this.ranked = ranked;
        this.ingames = new LinkedHashMap<String, OmegaPlayer>();
        this.spectators = new LinkedHashMap<String, OmegaPlayer>();
        final Constructor<? extends GameTypeListener> constructor = type.getListener().getConstructor(Match.class);
        this.listener = (GameTypeListener)constructor.newInstance(this);
    }
    
    public void start() {
        Practice.registerListener((Listener)this);
        Practice.registerListener((Listener)this.listener);
        this.taskId = Practice.getInstance().getServer().getScheduler().runTaskTimer((Plugin)Practice.getInstance(), this::onUpdate, 0L, 20L).getTaskId();
        this.startTime = System.currentTimeMillis();
        this.onStart();
    }
    
    public void finish() {
        this.onFinish();
        Practice.getInstance().getServer().getScheduler().cancelTask(this.taskId);
        HandlerList.unregisterAll((Listener)this);
        HandlerList.unregisterAll((Listener)this.listener);
    }
    
    public void addPlayer(final OmegaPlayer player) {
        this.ingames.put(player.getName(), player);
        player.setState(new InGameState(player, this));
    }
    
    public boolean removePlayer(final String player) {
        if (!this.isInGame(player)) {
            return false;
        }
        this.ingames.remove(player);
        return true;
    }
    
    public boolean isInGame(final String player) {
        return this.ingames.containsKey(player);
    }
    
    public boolean isInGame(final OmegaPlayer player) {
        return this.isInGame(player.getName());
    }
    
    public void addSpectator(final OmegaPlayer player) {
        this.spectators.put(player.getName(), player);
        player.setState(new InGameState(player, this));
    }
    
    public boolean removeSpectator(final String player) {
        if (!this.isSpectator(player)) {
            return false;
        }
        this.spectators.remove(player);
        return true;
    }
    
    public boolean isSpectator(final String player) {
        return this.spectators.containsKey(player);
    }
    
    public boolean isSpectator(final OmegaPlayer player) {
        return this.isInGame(player.getName());
    }
    
    public abstract void onPlayerSpectator(final OmegaPlayer p0);
    
    public void sendMessage(final String message) {
        for (final OmegaPlayer target : this.ingames.values()) {
            if (target.isOnline()) {
                target.sendMessage(message);
            }
        }
        for (final OmegaPlayer target : this.spectators.values()) {
            if (target.isOnline()) {
                target.sendMessage(message);
            }
        }
    }
    
    public void sendMessage(final TextComponent message) {
        for (final OmegaPlayer target : this.ingames.values()) {
            if (target.isOnline()) {
                target.sendMessage(message);
            }
        }
        for (final OmegaPlayer target : this.spectators.values()) {
            if (target.isOnline()) {
                target.sendMessage(message);
            }
        }
    }
    
    public WrappedLocation getLocation(final WrappedLocation loc) {
        return loc.getClone(this.origin);
    }
    
    public void teleportTo(final Entity entity, final WrappedLocation loc) {
        final WrappedLocation clone = this.getLocation(loc);
        entity.teleport(clone.getLocation(this.world));
    }
    
    public long getDuration() {
        return System.currentTimeMillis() - this.startTime;
    }
    
    public abstract void onStart();
    
    public abstract void onUpdate();
    
    public abstract void onFinish();
    
    public UUID getId() {
        return this.id;
    }
    
    public boolean isRanked() {
        return this.ranked;
    }
    
    public WrappedLocation getOrigin() {
        return this.origin;
    }
    
    public void setOrigin(final WrappedLocation origin) {
        this.origin = origin;
    }
    
    public World getWorld() {
        return this.world;
    }
    
    public GameType getType() {
        return this.type;
    }
    
    public GameTypeListener getListener() {
        return this.listener;
    }
    
    public Map getMap() {
        return this.map;
    }
    
    public LinkedHashMap<String, OmegaPlayer> getIngames() {
        return this.ingames;
    }
    
    public LinkedHashMap<String, OmegaPlayer> getSpectators() {
        return this.spectators;
    }
    
    public long getStartTime() {
        return this.startTime;
    }
    
    public MatchModel getModel() {
        return this.model;
    }
}
