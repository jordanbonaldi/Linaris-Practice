package net.linaris.Practice.handlers.states.items.moderator;

import org.bukkit.*;
import org.bukkit.inventory.*;

import net.linaris.Practice.handlers.players.*;
import net.linaris.Practice.handlers.states.*;
import net.linaris.Practice.handlers.states.states.*;
import net.linaris.Practice.utils.specialitems.*;

import org.bukkit.event.player.*;

public class ModeratorItem extends SpecialItem
{
    public ModeratorItem() {
        super("§dMod\u00e9rateur Mode", new ItemStack(Material.NETHER_STAR), new String[] { "§7Active le mode pour", "§7la mod\u00e9ration" });
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
        pl.setState(new ModeratorState(pl));
    }
    
    @Override
    protected void leftClickEvent(final PlayerInteractEvent event) {
        event.getPlayer().sendMessage("§7Active le mode pour la mod\u00e9ration");
    }
}
