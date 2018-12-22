package net.linaris.Practice.handlers.kits.items;

import org.bukkit.*;
import org.bukkit.inventory.*;

import net.linaris.Practice.database.models.*;
import net.linaris.Practice.handlers.games.*;
import net.linaris.Practice.handlers.players.*;
import net.linaris.Practice.handlers.states.*;
import net.linaris.Practice.handlers.states.states.*;
import net.linaris.Practice.utils.specialitems.*;

import org.bukkit.entity.*;

public class EditBuildItem extends MenuItem
{
    private GameType type;
    private BuildModel build;
    
    public EditBuildItem(final GameType type, final BuildModel build) {
        super("§a" + build.getName(), new ItemStack(Material.BOOK), new String[] { "", "§e§oEditer ce build" });
        this.type = type;
        this.build = build;
    }
    
    @Override
    public void inventoryClickEvent(final Player player) {
        player.closeInventory();
        final OmegaPlayer p = OmegaPlayerManager.get().getPlayer(player);
        p.setState(new EditKitState(p, this.build));
    }
}
