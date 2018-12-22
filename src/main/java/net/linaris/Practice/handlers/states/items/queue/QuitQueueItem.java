package net.linaris.Practice.handlers.states.items.queue;

import org.bukkit.*;
import org.bukkit.inventory.*;

import net.linaris.Practice.handlers.players.*;
import net.linaris.Practice.handlers.queues.*;
import net.linaris.Practice.handlers.states.*;
import net.linaris.Practice.handlers.states.states.*;
import net.linaris.Practice.utils.specialitems.*;

import org.bukkit.event.player.*;

public class QuitQueueItem extends SpecialItem
{
    public QuitQueueItem() {
        super("§cQuitter la file d'attente", new ItemStack(Material.BARRIER), new String[] { "§7Quitter la file d'attente..." });
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
        QueuesManager.removeQueue(pl.getUuid());
        pl.setState(new LobbyState(pl));
        pl.sendMessage("§cVous quittez la file d'attente !");
    }
    
    @Override
    protected void leftClickEvent(final PlayerInteractEvent event) {
    }
}
