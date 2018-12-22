package net.linaris.Practice.utils.specialitems;

import org.bukkit.event.player.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;

class SpecialItemPlayerInteractListener implements Listener
{
    @EventHandler(ignoreCancelled = false, priority = EventPriority.HIGH)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final SpecialItem item = SpecialItem.getSpecialItem(player.getItemInHand());
        if (item == null) {
            return;
        }
        if (item.isCancelInteractEvent()) {
            event.setCancelled(true);
        }
        if (event.hasBlock() && item.getDisabledMaterialClick().contains(event.getClickedBlock().getType())) {
            return;
        }
        if (this.isRightClick(event.getAction()) && item.isRightClickable()) {
            item.rightClickEvent(event);
        }
        else if (this.isLeftClick(event.getAction()) && item.isLeftClickable()) {
            item.leftClickEvent(event);
        }
        player.updateInventory();
    }
    
    public boolean isRightClick(final Action action) {
        return action.equals((Object)Action.RIGHT_CLICK_AIR) || action.equals((Object)Action.RIGHT_CLICK_BLOCK);
    }
    
    public boolean isLeftClick(final Action action) {
        return action.equals((Object)Action.LEFT_CLICK_AIR) || action.equals((Object)Action.LEFT_CLICK_BLOCK);
    }
}
