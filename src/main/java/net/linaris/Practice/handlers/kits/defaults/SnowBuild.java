package net.linaris.Practice.handlers.kits.defaults;

import org.bukkit.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.*;

import net.linaris.Practice.handlers.kits.*;
import net.linaris.Practice.utils.*;

public class SnowBuild extends DefaultBuild
{
    public SnowBuild() {
        super("Archer Defaut");
        this.getItems()[0] = new BuildItem(0, new ItemBuilder(Material.BOW).addEnchantment(Enchantment.ARROW_KNOCKBACK, 2)
        		.addEnchantment(Enchantment.ARROW_INFINITE, 1).build(), ItemType.WEAPONS);
        this.getItems()[1] = new BuildItem(1, new ItemStack(Material.ARROW, 1), ItemType.MISC);
        this.getItems()[2] = new BuildItem(3, new ItemStack(Material.ENDER_PEARL, 3), ItemType.MISC);
        this.getItems()[3] = new BuildItem(4, new ItemStack(Material.COOKED_BEEF, 64), ItemType.FOOD);
    }
}
