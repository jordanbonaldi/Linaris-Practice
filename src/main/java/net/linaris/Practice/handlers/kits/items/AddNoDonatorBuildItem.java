package net.linaris.Practice.handlers.kits.items;

import org.bukkit.*;
import org.bukkit.inventory.*;

import net.linaris.Practice.handlers.games.*;
import net.linaris.Practice.utils.specialitems.*;

import org.bukkit.entity.*;

public class AddNoDonatorBuildItem extends MenuItem
{
    private GameType type;
    
    public AddNoDonatorBuildItem(final GameType type) {
        super("§cAjouter un build", new ItemStack(Material.NETHER_STAR), new String[] { "", "§c§oDevenez VIP pour", "§c§oobtenir jusqu'\u00e0 5 builds" });
        this.type = type;
    }
    
    @Override
    public void inventoryClickEvent(final Player player) {
        player.closeInventory();
        player.sendMessage("§cVous devez être §dVIP §cpour pouvoir avoir jusqu'a 5 builds");
    }
}
