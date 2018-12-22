package net.linaris.Practice.handlers.states.items.lobby;

import org.bukkit.*;
import org.bukkit.inventory.*;

import net.linaris.Practice.handlers.players.*;
import net.linaris.Practice.handlers.states.gui.*;
import net.linaris.Practice.utils.gui.*;
import net.linaris.Practice.utils.specialitems.*;

import org.bukkit.event.player.*;

public class UnrankedItem extends SpecialItem
{
    public UnrankedItem() {
        super("§aMatch d'Entrainement", new ItemStack(Material.IRON_SWORD), new String[] { "§7Avant de partir \u00e0 l'aventure ", "§7nous devons nous entrainer !" });
        this.setMovable(false);
        this.setRightClickable(true);
        this.setLeftClickable(true);
        this.setDroppable(false);
        this.setCancelInteractEvent(true);
        SpecialItem.registerItem(this);
    }
    
    @Override
    protected void rightClickEvent(final PlayerInteractEvent event) {
        final OmegaPlayer pl = OmegaPlayerManager.get().getPlayer(event.getPlayer());
        GuiManager.openGui(new QueueGui(pl, false));
    }
    
    @Override
    protected void leftClickEvent(final PlayerInteractEvent event) {
        event.getPlayer().sendMessage("§7Avant de partir \u00e0 l'aventure nous devons nous entrainer !");
    }
}
