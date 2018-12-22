package net.linaris.Practice.utils.specialitems;

import org.bukkit.event.player.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.event.*;

class SpecialItemPlayerItemConsumeListener implements Listener
{
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onItemConsumeEvent(final PlayerItemConsumeEvent event) {
        final Player player = event.getPlayer();
        final ItemStack itemStack = event.getItem();
        if (itemStack == null || itemStack.getType().equals((Object)Material.AIR)) {
            return;
        }
        final SpecialItem specialItem = SpecialItem.getSpecialItem(itemStack);
        if (!(specialItem instanceof SpecialItemConsumable)) {
            return;
        }
        ((SpecialItemConsumable)specialItem).consumeItemEvent(player, itemStack);
        event.setCancelled(true);
    }
}
