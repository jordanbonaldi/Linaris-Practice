package net.linaris.Practice.utils.specialitems;

import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import java.util.*;

public abstract class VirtualMenu extends VirtualInventory
{
    public VirtualMenu(final String title, final int row) {
        super(title, row);
    }
    
    @Override
    public void open(final Player player) {
        this.fill(player, "");
    }
    
    @Override
    public void openWithExtraText(final Player player, final String extraText) {
        this.fill(player, extraText);
    }
    
    public void fill(final Player player, final String extraText) {
        this.inventory = Bukkit.createInventory((InventoryHolder)this, this.row * 9, this.getTitleWithExtraText(extraText));
        for (final Map.Entry<Integer, SpecialItem> entry : this.myItems.entrySet()) {
            final SpecialItem specialItem = entry.getValue();
            this.inventory.setItem((int)entry.getKey(), specialItem.getStaticItem());
        }
        player.openInventory(this.inventory);
    }
}
