package net.linaris.Practice.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.linaris.Practice.Practice;
import net.linaris.Practice.handlers.players.OmegaPlayer;
import net.linaris.Practice.handlers.players.OmegaPlayerManager;

public class FreezeListener implements Listener
{
    private Practice api;
    
    public FreezeListener(final Practice api) {
        this.api = api;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDamage(final EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        final OmegaPlayer player = this.api.getPlayerManager().getPlayer((Player)e.getEntity());
        if (player == null) {
            e.setCancelled(true);
            return;
        }
        if (player.isFreeze()) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDamage(final EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) {
            return;
        }
        final OmegaPlayer player = this.api.getPlayerManager().getPlayer((Player)e.getDamager());
        if (player == null) {
            e.setCancelled(true);
            return;
        }
        if (player.isFreeze()) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(final PlayerMoveEvent e) {
        final OmegaPlayer player = this.api.getPlayerManager().getPlayer(e.getPlayer());
        if (player == null) {
            e.setCancelled(true);
            return;
        }
        if (player.isFreeze()) {
            final Location from = e.getFrom();
            final Location to = e.getTo();
            if (from.getBlockX() != to.getBlockX() || from.getBlockZ() != to.getBlockZ()) {
                player.teleport(from);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerHeldItem(final PlayerItemHeldEvent e) {
        final OmegaPlayer player = this.api.getPlayerManager().getPlayer(e.getPlayer());
        if (player.isFreeze()) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDropItem(final PlayerDropItemEvent e) {
        final OmegaPlayer player = this.api.getPlayerManager().getPlayer(e.getPlayer());
        if (player.isFreeze()) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(final PlayerInteractEvent e) {
        final OmegaPlayer player = this.api.getPlayerManager().getPlayer(e.getPlayer());
        if (player.isFreeze()) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerClick(final InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        final OmegaPlayer player = this.api.getPlayerManager().getPlayer((Player)e.getWhoClicked());
        if (player.isFreeze()) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(final PlayerQuitEvent e) {
        final OmegaPlayer player = this.api.getPlayerManager().getPlayer(e.getPlayer());
        if (player == null) {
            return;
        }
        if (player.isFreeze()) {
            for (final OmegaPlayer target : OmegaPlayerManager.get()) {
                if (target.getData().getRank().getModerationLevel() >= 2) {
                    target.sendMessage("§b" + player.getName() + "§c a d\u00e9connect\u00e9 alors qu'il \u00e9tait freeze !");
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(final PlayerPickupItemEvent e) {
        final OmegaPlayer player = this.api.getPlayerManager().getPlayer(e.getPlayer());
        if (player == null) {
            e.setCancelled(true);
            return;
        }
        if (player.isFreeze() || player.isVanish()) {
            e.setCancelled(true);
        }
    }
}
