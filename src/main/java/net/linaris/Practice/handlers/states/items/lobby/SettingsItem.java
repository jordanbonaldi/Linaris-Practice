package net.linaris.Practice.handlers.states.items.lobby;

import org.bukkit.*;
import org.bukkit.inventory.*;

import net.linaris.Practice.handlers.players.*;
import net.linaris.Practice.utils.*;
import net.linaris.Practice.utils.specialitems.*;

import org.bukkit.event.player.*;

public class SettingsItem extends SpecialItem
{
    public SettingsItem() {
        super("§7Pr\u00e9f\u00e9rences", new ItemStack(Material.TRIPWIRE_HOOK), new String[] { "§7C'est ici que vous pouvez", "§7d\u00e9finir vos pr\u00e9f\u00e9rences !" });
        this.setMovable(false);
        this.setRightClickable(true);
        this.setLeftClickable(true);
        this.setDroppable(false);
        this.setCancelInteractEvent(true);
        SpecialItem.registerItem(this);
    }
    
    @Override
    protected void rightClickEvent(final PlayerInteractEvent event) {
        event.getPlayer().sendMessage("§cTest... " + TimeUtils.getTime(OmegaPlayerManager.get().getPlayer(event.getPlayer()).getData().getTimePlayed()));
    }
    
    @Override
    protected void leftClickEvent(final PlayerInteractEvent event) {
        event.getPlayer().sendMessage("§cTest...");
    }
}
