package net.linaris.Practice.handlers.kits.items;

import org.bukkit.*;
import org.bukkit.inventory.*;

import net.linaris.Practice.database.*;
import net.linaris.Practice.handlers.games.*;
import net.linaris.Practice.handlers.kits.guis.*;
import net.linaris.Practice.handlers.players.*;
import net.linaris.Practice.utils.specialitems.*;

import org.bukkit.entity.*;

public class AddBuildItem extends MenuItem
{
    private GameType type;
    
    public AddBuildItem(final GameType type) {
        super("§aAjouter un build", new ItemStack(Material.NETHER_STAR), new String[] { "", "§e§oCreer un nouveau build" });
        this.type = type;
    }
    
    @Override
    public void inventoryClickEvent(final Player player) {
        final OmegaPlayer pl = OmegaPlayerManager.get().getPlayer(player);
        DatabaseConnector.getBuildsDao().createBuild(pl.getData().getId(), "Build " + this.type.getName(), this.type.getId(), this.type.getDefaultBuild().getItemsId());
        new EditKitsGui(pl, this.type).open(player);
    }
}
