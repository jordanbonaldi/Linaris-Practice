package net.linaris.Practice.utils.specialitems;

import org.bukkit.event.player.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

class SpecialItemPlayerInteractEntityListener implements Listener
{
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlayerInteractEntity(final PlayerInteractEntityEvent event) {
        final Player player = event.getPlayer();
        final Entity entity = event.getRightClicked();
        final SpecialItem item = SpecialItem.getSpecialItem(player.getItemInHand());
        if (item == null) {
            return;
        }
        if (!item.isDroppable() && entity instanceof ItemFrame) {
            event.setCancelled(true);
        }
    }
}
