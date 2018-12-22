package net.linaris.Practice.utils.specialitems;

import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.event.*;

class SpecialItemInventoryMoveItemListener implements Listener
{
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onInventoryMoveItem(final InventoryMoveItemEvent event) {
        final SpecialItem item = SpecialItem.getSpecialItem(event.getItem());
        if (item == null) {
            return;
        }
        final Inventory init = event.getInitiator();
        final Inventory dest = event.getDestination();
        if (!item.isMovable()) {
            event.setCancelled(true);
        }
        else if (!item.isDroppable() && init.getType() == InventoryType.PLAYER && dest.getType() != InventoryType.PLAYER) {
            event.setCancelled(true);
        }
    }
}
