package net.linaris.Practice.handlers.kits.listeners;

import org.bukkit.*;
import org.bukkit.inventory.*;

import net.linaris.Practice.database.*;
import net.linaris.Practice.handlers.players.*;
import net.linaris.Practice.handlers.states.states.*;
import net.linaris.Practice.utils.*;

import org.bukkit.event.*;
import org.bukkit.event.player.*;

public class EditKitListener implements Listener
{
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerChat(final AsyncPlayerChatEvent e) {
        final OmegaPlayer player = OmegaPlayerManager.get().getPlayer(e.getPlayer());
        if (!(player.getState() instanceof EditKitState)) {
            return;
        }
        e.setCancelled(true);
        final EditKitState state = (EditKitState)player.getState();
        final String text = e.getMessage();
        if (text.startsWith("save")) {
            final ItemStack[] items = player.getInventory().getContents();
            final Integer[] itemsbuild = new Integer[36];
            for (int i = 0; i < 36; ++i) {
                final ItemStack item = items[i];
                if (item != null) {
                    if (item.getType() != Material.AIR) {
                        final NBTItem nbt = new NBTItem(item);
                        if (nbt.hasKey("build_id")) {
                            final int id = nbt.getInteger("build_id");
                            itemsbuild[i] = id;
                        }
                    }
                }
            }
            state.getBuild().setItems(itemsbuild);
            DatabaseConnector.getBuildsDao().save(state.getBuild());
            final OmegaPlayer player2 = OmegaPlayerManager.get().getPlayer(e.getPlayer());
            TaskManager.runTask(() -> player2.setState(new LobbyState(player2)));
            return;
        }
        if (!text.startsWith("rename")) {
            return;
        }
        if (player.getData().getRank().getVipLevel() > 1) {
            player.sendMessage("§cVous devez \u00eatre §dVIP");
            return;
        }
        final String[] args = text.split(" ");
        if (args.length != 2) {
            player.sendMessage("§cVous devez entrez un nom");
            return;
        }
        final String name = args[1].trim();
        if (name.length() < 3) {
            player.sendMessage("§cLe nouveau nom doit faire au moins 3 caract\u00e8res");
            return;
        }
        state.getBuild().setName(name);
        player.sendMessage("§aNouveau nom: " + name);
    }
    
    @EventHandler
    public void onDropItem(final PlayerDropItemEvent e) {
        final OmegaPlayer player = OmegaPlayerManager.get().getPlayer(e.getPlayer());
        if (!(player.getState() instanceof EditKitState)) {
            return;
        }
        e.setCancelled(true);
    }
    
    @EventHandler
    public void onInteractEvent(final PlayerInteractEvent e) {
        final OmegaPlayer player = OmegaPlayerManager.get().getPlayer(e.getPlayer());
        if (!(player.getState() instanceof EditKitState)) {
            return;
        }
        e.setCancelled(true);
    }
}
