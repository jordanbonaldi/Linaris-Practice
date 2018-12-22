package net.linaris.Practice.utils.specialitems;

import org.bukkit.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.event.*;

class SpecialItemInventoryClickListener implements Listener
{
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onInventoryClickEvent(final InventoryClickEvent event) {
        final InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof VirtualInventory || holder instanceof Player) {
            if (holder instanceof VirtualInventory) {
                event.setCancelled(true);
            }
            final ItemStack itemStack = event.getCurrentItem();
            final SpecialItem item = SpecialItem.getSpecialItem(itemStack);
            if (item != null && item.isInventoryClickable()) {
                event.setCancelled(true);
                final ClickType click = event.getClick();
                if (click.equals((Object)ClickType.RIGHT)) {
                    item.rightClickEvent(event);
                }
                else if (click.equals((Object)ClickType.LEFT)) {
                    item.leftClickEvent(event);
                }
                else if (click.equals((Object)ClickType.MIDDLE)) {
                    item.middleClickEvent(event);
                }
                else {
                    item.inventoryClickEvent(event);
                }
            }
        }
        if (event.isCancelled()) {
            return;
        }
        final ItemStack itemStack = event.getCurrentItem();
        final SpecialItem item = SpecialItem.getSpecialItem(itemStack);
        if (item != null && !item.isMovable()) {
            event.setCancelled(true);
        }
    }
}
