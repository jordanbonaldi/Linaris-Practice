package net.linaris.Practice.handlers.kits;

import org.bukkit.inventory.*;
import org.bukkit.*;

public enum ItemType
{
    WEAPONS(0, "Armes", new ItemStack(Material.DIAMOND_SWORD)), 
    POTION(1, "Potions", new ItemStack(Material.POTION)), 
    MISC(2, "Misc", new ItemStack(Material.FISHING_ROD)), 
    FOOD(3, "Nourriture", new ItemStack(Material.COOKED_BEEF));
    
    private final int id;
    private final String name;
    private final ItemStack display;
    
    private ItemType(final int id, final String name, final ItemStack display) {
        this.id = id;
        this.name = name;
        this.display = display;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public ItemStack getDisplay() {
        return this.display;
    }
}
