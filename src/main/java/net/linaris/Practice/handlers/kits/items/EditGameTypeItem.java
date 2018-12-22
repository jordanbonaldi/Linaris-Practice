package net.linaris.Practice.handlers.kits.items;

import org.bukkit.entity.*;

import net.linaris.Practice.handlers.games.*;
import net.linaris.Practice.handlers.kits.guis.*;
import net.linaris.Practice.handlers.players.*;
import net.linaris.Practice.utils.specialitems.*;

public class EditGameTypeItem extends MenuItem
{
    private GameType type;
    
    public EditGameTypeItem(final GameType type) {
        super("§a" + type.getName(), type.getDisplay(), new String[] { "", "§e§oEditer ce kit" });
        this.type = type;
    }
    
    @Override
    public void inventoryClickEvent(final Player player) {
        new EditKitsGui(OmegaPlayerManager.get().getPlayer(player), this.type).open(player);
    }
}
