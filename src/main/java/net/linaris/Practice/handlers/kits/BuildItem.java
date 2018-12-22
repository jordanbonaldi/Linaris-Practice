package net.linaris.Practice.handlers.kits;

import org.bukkit.inventory.*;

public class BuildItem
{
    private final int id;
    private final ItemStack item;
    private final ItemType tab;
    
    public BuildItem(final int id, final ItemStack item, final ItemType tab) {
        this.id = id;
        this.item = item;
        this.tab = tab;
    }
    
    public int getId() {
        return this.id;
    }
    
    public ItemStack getItem() {
        return this.item;
    }
    
    public ItemType getTab() {
        return this.tab;
    }
}
