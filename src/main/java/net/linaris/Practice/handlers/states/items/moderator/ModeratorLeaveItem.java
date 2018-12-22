package net.linaris.Practice.handlers.states.items.moderator;

import org.bukkit.*;
import org.bukkit.inventory.*;

import net.linaris.Practice.*;
import net.linaris.Practice.handlers.players.*;
import net.linaris.Practice.handlers.states.*;
import net.linaris.Practice.handlers.states.states.*;
import net.linaris.Practice.utils.specialitems.*;

import org.bukkit.event.player.*;

public class ModeratorLeaveItem extends SpecialItem
{
    public ModeratorLeaveItem() {
        super("§dRedevenir joueur", new ItemStack(Material.ARROW), new String[] { "§7D\u00e9sactive le mode pour", "§7la mod\u00e9ration" });
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
        pl.teleport(Practice.getInstance().getLobbyLocation());
        pl.setState(new LobbyState(pl));
    }
    
    @Override
    protected void leftClickEvent(final PlayerInteractEvent event) {
        event.getPlayer().sendMessage("§7D\u00e9sactive le mode pour la mod\u00e9ration");
    }
}
