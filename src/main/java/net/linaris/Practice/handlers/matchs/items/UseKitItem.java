package net.linaris.Practice.handlers.matchs.items;

import org.bukkit.*;
import org.bukkit.inventory.*;

import net.linaris.Practice.database.models.*;
import net.linaris.Practice.handlers.kits.*;
import net.linaris.Practice.utils.specialitems.*;

import org.bukkit.event.player.*;

public class UseKitItem extends SpecialItem
{
    private BuildModel build;
    
    public UseKitItem(final BuildModel build) {
        super("§aUtiliser: " + build.getName(), new ItemStack(Material.BOOK), new String[0]);
        this.build = build;
        this.setMovable(false);
        this.setRightClickable(true);
        this.setLeftClickable(true);
        this.setDroppable(false);
        this.setCancelInteractEvent(true);
        SpecialItem.registerItem(this);
    }
    
    @Override
    protected void rightClickEvent(final PlayerInteractEvent event) {
        final BuildItem[] items = BuildUtils.buildToItem(this.build);
        for (int i = 0; i < 36; ++i) {
            final BuildItem item = items[i];
            if (item != null) {
                event.getPlayer().getInventory().setItem(i, item.getItem());
            }
        }
        event.getPlayer().sendMessage("§aVous utilisez le build: " + this.build.getName());
    }
}
