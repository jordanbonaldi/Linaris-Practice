package net.linaris.Practice.handlers.states.items.lobby;

import org.bukkit.*;
import org.bukkit.inventory.*;

import net.linaris.Practice.handlers.players.*;
import net.linaris.Practice.handlers.states.gui.*;
import net.linaris.Practice.utils.gui.*;
import net.linaris.Practice.utils.specialitems.*;

import org.bukkit.event.player.*;

public class StatsItem extends SpecialItem
{
    public StatsItem() {
        super("§bStatistiques", new ItemStack(Material.BOOK), new String[] { "§7Pour conna\u00eetre vos", "§7statistique comme votre", "§7\"elo\" c'est ici !" });
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
        GuiManager.openGui(new PlayerStatsGui(pl));
    }
    
    @Override
    protected void leftClickEvent(final PlayerInteractEvent event) {
        event.getPlayer().sendMessage("§7Pour conna\u00eetre vos statistique comme votre \"elo\" c'est ici !");
    }
}
