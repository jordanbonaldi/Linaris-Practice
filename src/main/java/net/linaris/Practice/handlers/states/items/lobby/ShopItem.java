package net.linaris.Practice.handlers.states.items.lobby;

import org.bukkit.*;
import org.bukkit.inventory.*;

import net.linaris.Practice.handlers.kits.guis.*;
import net.linaris.Practice.handlers.players.OmegaPlayer;
import net.linaris.Practice.handlers.players.OmegaPlayerManager;
import net.linaris.Practice.handlers.states.gui.PlayerStatsGui;
import net.linaris.Practice.utils.gui.GuiManager;
import net.linaris.Practice.utils.specialitems.*;

import org.bukkit.event.player.*;

public class ShopItem extends SpecialItem
{
    public ShopItem() {
        super("§a§lBoutique en ligne", new ItemStack(Material.NETHER_STAR));
        this.setMovable(false);
        this.setRightClickable(true);
        this.setLeftClickable(true);
        this.setDroppable(false);
        this.setCancelInteractEvent(true);
        SpecialItem.registerItem(this);
    }
    
    @Override
    protected void rightClickEvent(final PlayerInteractEvent event) {
       Bukkit.dispatchCommand(event.getPlayer(), "shop"); 
    }
    
    @Override
    protected void leftClickEvent(final PlayerInteractEvent event) {
        Bukkit.dispatchCommand(event.getPlayer(), "shop");
    }
}
