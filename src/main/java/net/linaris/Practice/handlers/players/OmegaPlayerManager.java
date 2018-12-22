package net.linaris.Practice.handlers.players;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.procedure.TObjectProcedure;
import net.linaris.Practice.Practice;
import net.linaris.Practice.handlers.players.procedure.OmegaPlayerProcedure;
import net.linaris.Practice.utils.EventListener;

public class OmegaPlayerManager
implements Iterable<OmegaPlayer> {
    private static OmegaPlayerManager _instance;
    private Practice api;
    private TMap<String, OmegaPlayer> players;
    private TIntObjectMap<OmegaPlayer> fromPlayers;

    public static OmegaPlayerManager get() {
        return _instance;
    }

    public OmegaPlayerManager(Practice api) {
        _instance = this;
        this.api = api;
        this.players = new THashMap(32);
        this.fromPlayers = new TIntObjectHashMap();
        new PlayerListener(api).register();
    }

    public int numberOfPlayer() {
        return this.players.size();
    }

    public void kickAll(String reason) {
        this.forEachOnlinePlayer(player -> {
            player.kickPlayer(reason);
            return true;
        }
        );
    }

    public OmegaPlayer getPlayer(String name) {
        return (OmegaPlayer)this.players.get((Object)name.toLowerCase());
    }

    public OmegaPlayer getPlayer(int entityId) {
        return (OmegaPlayer)this.fromPlayers.get(entityId);
    }

    public OmegaPlayer getPlayer(Player player) {
        return player instanceof OmegaPlayer ? (OmegaPlayer)player : (OmegaPlayer)this.fromPlayers.get(player.getEntityId());
    }

    public boolean forEachOnlinePlayer(OmegaPlayerProcedure procedure) {
        return this.players.forEachValue((TObjectProcedure)procedure);
    }

    public  Iterable<OmegaPlayer> fromLegacy(final Player ... players) {
        return () -> new Iterator<OmegaPlayer>(){
            Iterator it;

            @Override
            public boolean hasNext() {
                return this.it.hasNext();
            }

            @Override
            public OmegaPlayer next() {
                return OmegaPlayerManager.this.getPlayer((Player)this.it.next());
            }
        };
    }

    @Override
    public Iterator<OmegaPlayer> iterator() {
        return this.players.values().iterator();
    }

    @Override
    public void forEach(Consumer<? super OmegaPlayer> action) {
        this.players.values().forEach(action);
    }

    @Override
    public Spliterator<OmegaPlayer> spliterator() {
        return this.players.values().spliterator();
    }

    public void addPlayer(Player player) {
        OmegaPlayer ep = new OmegaPlayer();
        ep.setBacked(player);
        this.players.put(player.getName().toLowerCase(), ep);
        this.fromPlayers.put(player.getEntityId(), ep);
    }

    public void removePlayer(Player player) {
        OmegaPlayer ep = (OmegaPlayer)this.players.remove((Object)player.getName().toLowerCase());
        this.fromPlayers.remove(player.getEntityId());
        if (ep != null) {
            ep.setBacked(null);
        }
    }

    public Practice getApi() {
        return this.api;
    }

    private class PlayerListener
    extends EventListener {
        public PlayerListener(Practice plugin) {
            super(plugin);
        }

        @EventHandler(priority=EventPriority.LOW)
        public void onPlayerJoin(PlayerJoinEvent event) {
            OmegaPlayerManager.this.addPlayer(event.getPlayer());
        }

        @EventHandler(priority=EventPriority.HIGHEST)
        public void onPlayerQuit(PlayerQuitEvent event) {
            OmegaPlayerManager.this.removePlayer(event.getPlayer());
        }

        @EventHandler(priority=EventPriority.HIGHEST)
        public void onPlayerQuit(PlayerKickEvent event) {
            OmegaPlayerManager.this.removePlayer(event.getPlayer());
        }
    }

}