package net.linaris.Practice.handlers.states.gui;

import org.bukkit.inventory.*;
import java.util.*;

import org.bukkit.inventory.meta.*;

import net.linaris.Practice.handlers.games.*;
import net.linaris.Practice.handlers.players.*;
import net.linaris.Practice.handlers.queues.*;
import net.linaris.Practice.handlers.states.states.*;
import net.linaris.Practice.utils.*;
import net.linaris.Practice.utils.gui.*;

import org.bukkit.event.inventory.*;
import org.bukkit.*;

public class QueueGui extends GuiScreen
{
    private boolean ranked;
    
    public QueueGui(final OmegaPlayer player, final boolean ranked) {
        super("Mode de jeu ?", 1, player, false);
        this.ranked = ranked;
        this.build();
    }
    
    @Override
    public void drawScreen() {
        int i = 0;
        for (final GameType type : GameTypesManager.getTypes().values()) {
            this.setItem(this.buildGameTypeItem(type), i);
            ++i;
        }
    }
    
    public ItemStack buildGameTypeItem(final GameType type) {
        final ItemStack item = type.getDisplay().clone();
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§a" + type.getName());
        final List<String> lore = new ArrayList<String>();
        lore.add("");
        lore.add("§e§oClique pour choisir ce mode");
        meta.setLore((List)lore);
        item.setItemMeta(meta);
        return new NBTItem(item).setString("gametype_id", type.getName()).getItem();
    }
    
    @Override
    public void onOpen() {
    }
    
    @Override
    public void onClick(final ItemStack item, final InventoryClickEvent event) {
        event.setCancelled(true);
        if (item == null || item.getType() == Material.AIR) {
            return;
        }
        final NBTItem nbt = new NBTItem(item);
        if (!nbt.hasKey("gametype_id")) {
            return;
        }
        final GameType type = GameTypesManager.getTypes().get(nbt.getString("gametype_id"));
        if (type == null) {
            return;
        }
        if (this.getPlayer().isAvailable()) {
            this.getPlayer().closeInventory();
            this.getPlayer().setState(new InQueueState(this.getPlayer()));
            QueuesManager.addInQueue(this.getPlayer(), type, this.ranked);
        }
        else {
            this.getPlayer().closeInventory();
            this.getPlayer().sendMessage("§cOops une erreur est survenue !");
        }
    }
    
    @Override
    public void onClose() {
    }
}
