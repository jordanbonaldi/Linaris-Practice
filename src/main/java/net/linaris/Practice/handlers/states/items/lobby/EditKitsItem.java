package net.linaris.Practice.handlers.states.items.lobby;

import org.bukkit.*;
import org.bukkit.inventory.*;

import net.linaris.Practice.handlers.kits.guis.*;
import net.linaris.Practice.utils.specialitems.*;

import org.bukkit.event.player.*;

public class EditKitsItem extends SpecialItem
{
    public EditKitsItem() {
        super("§6Edition des kits", new ItemStack(Material.CHEST), new String[] { "§7Il est temps de faire votre", "§7propre disposition des kits !" });
        this.setMovable(false);
        this.setRightClickable(true);
        this.setLeftClickable(true);
        this.setDroppable(false);
        this.setCancelInteractEvent(true);
        SpecialItem.registerItem(this);
    }
    
    @Override
    protected void rightClickEvent(final PlayerInteractEvent event) {
        new EditTypeGui().open(event.getPlayer());
    }
    
    @Override
    protected void leftClickEvent(final PlayerInteractEvent event) {
        event.getPlayer().sendMessage("§7Il est temps de faire votre propre disposition des kits !");
    }
}
