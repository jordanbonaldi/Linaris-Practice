package net.linaris.Practice.listeners;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import net.linaris.Practice.Practice;
import net.linaris.Practice.handlers.matchs.Match;
import net.linaris.Practice.handlers.players.OmegaPlayer;
import net.linaris.Practice.handlers.players.OmegaPlayerManager;
import net.linaris.Practice.handlers.players.Rank;
import net.linaris.Practice.handlers.states.StatesManager;
import net.linaris.Practice.handlers.states.states.InGameState;
import net.linaris.Practice.handlers.states.states.ModeratorState;
import net.linaris.Practice.utils.ScoreboardSign;

public class PlayerListener
implements Listener {
    private Practice api;

    public PlayerListener(Practice api) {
        this.api = api;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        OmegaPlayer pl = this.api.getPlayerManager().getPlayer(e.getPlayer());
        ScoreBoard.playing.put(pl, false);
        StatesManager.defaultState(pl);
        e.setJoinMessage("");
        for (OmegaPlayer player : OmegaPlayerManager.get()) {        	
            if (!player.isVanish() || pl.getData().getRank().getModerationLevel() >= player.getData().getRank().getModerationLevel()) continue;
            pl.hidePlayer(player.getPlayer());
        }
    }

    @EventHandler(priority=EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent e) {
        e.setQuitMessage("");
    	ScoreboardSign bar = ScoreboardSign.get(e.getPlayer());
    	if (bar != null) {
    		bar.destroy();
    	}
    }

    @EventHandler(priority=EventPriority.NORMAL)
    public void onPlayerKick(PlayerKickEvent e) {
        e.setLeaveMessage("");
    	ScoreboardSign bar = ScoreboardSign.get(e.getPlayer());
    	if (bar != null) {
    		bar.destroy();
    	}
    }

    @EventHandler(priority=EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent e) {
        e.setDeathMessage("");
        e.getDrops().clear();
    }

    @EventHandler(priority=EventPriority.NORMAL)
    public void onPlayerDamage(EntityDamageEvent e) {
        e.setCancelled(true);
    }

    @EventHandler(priority=EventPriority.NORMAL)
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler(priority=EventPriority.NORMAL)
    public void onEntityChange(EntitySpawnEvent e) {
        if (e.getEntity() instanceof LivingEntity) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.NORMAL)
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        e.setRespawnLocation(this.api.getLobbyLocation());
    }

    @EventHandler(priority=EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getPlayer().isOp()) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler(priority=EventPriority.NORMAL)
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.getPlayer().isOp()) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onEntityRightClick(PlayerInteractAtEntityEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        OmegaPlayer pl = this.api.getPlayerManager().getPlayer(e.getPlayer());       	
        if (e.getTo().getY() <= 2.0 && pl.getState() instanceof InGameState) {
            if (ScoreBoard.playing.get(pl) == false)
            	e.getPlayer().teleport(this.api.getLobbyLocation());
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        OmegaPlayer player = OmegaPlayerManager.get().getPlayer(e.getPlayer());
        Rank rank = player.getData().getRank();
        if (player.getState() instanceof InGameState) {
            Match match = ((InGameState)player.getState()).getMatch();
            String format = rank.getPrefix() + rank.getColor() + player.getName() + ": " + e.getMessage();
            for (OmegaPlayer target22 : match.getIngames().values()) {
                target22.sendMessage(format);
            }
            for (OmegaPlayer target22 : match.getSpectators().values()) {
                target22.sendMessage(format);
            }
            for (OmegaPlayer target22 : OmegaPlayerManager.get()) {
                if (!(target22.getState() instanceof ModeratorState)) continue;
                target22.sendMessage(format);
            }
        } else if (player.getState() instanceof ModeratorState) {
            String format = "\u00a76\u00a7l[Moderation Linaris] \u00a76" + player.getName() + ": " + e.getMessage();
            for (OmegaPlayer target : OmegaPlayerManager.get()) {
                target.sendMessage(format);
            }
        } else {
            String format = rank.getPrefix() + rank.getColor() + player.getName() + ": " + e.getMessage();
            for (OmegaPlayer target : OmegaPlayerManager.get()) {
                if (target.getState() instanceof InGameState) continue;
                target.sendMessage(format);
            }
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e) {
        e.setCancelled(true);
    }
}