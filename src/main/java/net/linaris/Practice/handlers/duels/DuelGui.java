package net.linaris.Practice.handlers.duels;

import org.bukkit.inventory.*;
import java.util.*;

import org.bukkit.inventory.meta.*;

import net.linaris.Practice.handlers.games.*;
import net.linaris.Practice.handlers.players.*;
import net.linaris.Practice.utils.*;
import net.linaris.Practice.utils.gui.*;

import org.bukkit.event.inventory.*;
import org.bukkit.*;

public class DuelGui extends GuiScreen
{
    private OmegaPlayer target;
    
    public DuelGui(final OmegaPlayer player, final OmegaPlayer target) {
        super("Mode de jeu ?", 1, player, false);
        this.target = target;
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
        if (this.target.isAvailable() && this.getPlayer().isAvailable()) {
            this.getPlayer().closeInventory();
            DuelsManager.addRequest(this.getPlayer(), this.target, type);
        }
        else {
            this.getPlayer().closeInventory();
            this.getPlayer().sendMessage("§cCe joueur n'est pas disponible pour le moment !");
        }
    }
    
    @Override
    public void onClose() {
    }
}
