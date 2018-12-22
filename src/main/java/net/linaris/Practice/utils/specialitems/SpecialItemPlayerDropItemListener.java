package net.linaris.Practice.utils.specialitems;

import org.bukkit.event.player.*;
import org.bukkit.event.*;

class SpecialItemPlayerDropItemListener implements Listener
{
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlayerDropItem(final PlayerDropItemEvent event) {
        final SpecialItem item = SpecialItem.getSpecialItem(event.getItemDrop().getItemStack());
        if (item == null) {
            return;
        }
        if (!item.isDroppable()) {
            event.setCancelled(true);
        }
        item.dropEvent(event);
    }
}
