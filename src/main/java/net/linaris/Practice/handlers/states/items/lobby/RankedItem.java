package net.linaris.Practice.handlers.states.items.lobby;

import org.bukkit.*;
import org.bukkit.inventory.*;

import net.linaris.Practice.handlers.players.*;
import net.linaris.Practice.handlers.states.gui.*;
import net.linaris.Practice.utils.gui.*;
import net.linaris.Practice.utils.specialitems.*;

import org.bukkit.event.player.*;

public class RankedItem extends SpecialItem
{
    public RankedItem() {
        super("§cMatch Class\u00e9", new ItemStack(Material.GOLD_SWORD), new String[] { "§7En route vers la premi\u00e8re", "§7place du classement !" });
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
        if (pl.getData().getPlayed() < 20 && !pl.isDonator()) {
            pl.sendMessage("§cVous devez au moins faire 20 matchs avant de pouvoir faire un §c§lMatch class\u00e9§c !");
            return;
        }
        if (pl.getData().getRankedRemaining() <= 0 && !pl.isDonator()) {
            pl.sendMessage("§cVous n'avez plus de §c§lMatch class\u00e9§c pour aujourd'hui achetez d\u00e8s maintenant le grade §dVIP§c pour en avoir \u00e0 §bl'infini");
        }
        else {
            GuiManager.openGui(new QueueGui(pl, true));
        }
    }
    
    @Override
    protected void leftClickEvent(final PlayerInteractEvent event) {
        event.getPlayer().sendMessage("§7En route vers la premi\u00e8re place du classement !");
    }
}
