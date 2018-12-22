package net.linaris.Practice.utils.specialitems;

import org.bukkit.inventory.*;
import org.bukkit.event.inventory.*;

public abstract class MenuItem extends SpecialItem
{
    public MenuItem(final String name, final ItemStack itemStack, final String... lore) {
        super(name, lore, itemStack);
        this.setRightClickable(true);
        this.setInventoryClickable(true);
        this.setDroppable(false);
        this.setMovable(false);
    }
    
    @Override
    protected void leftClickEvent(final InventoryClickEvent event) {
        this.inventoryClickEvent(event);
    }
    
    @Override
    protected void rightClickEvent(final InventoryClickEvent event) {
        this.inventoryClickEvent(event);
    }
    
    @Override
    public void middleClickEvent(final InventoryClickEvent event) {
        this.inventoryClickEvent(event);
    }
}
