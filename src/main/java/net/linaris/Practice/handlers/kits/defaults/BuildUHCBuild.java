package net.linaris.Practice.handlers.kits.defaults;

import org.bukkit.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.*;

import net.linaris.Practice.handlers.kits.*;
import net.linaris.Practice.utils.*;

public class BuildUHCBuild extends DefaultBuild
{
    public BuildUHCBuild() {
        super("BuildUHC Defaut");
        this.getItems()[0] = new BuildItem(0, new ItemBuilder(Material.DIAMOND_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 3).build(), ItemType.WEAPONS);
        this.getItems()[1] = new BuildItem(1, new ItemStack(Material.FISHING_ROD, 1), ItemType.MISC);
        this.getItems()[2] = new BuildItem(2, new ItemBuilder(Material.BOW).addEnchantment(Enchantment.ARROW_DAMAGE, 3).build(), ItemType.MISC);
        this.getItems()[3] = new BuildItem(3, new ItemStack(Material.COBBLESTONE, 64), ItemType.MISC);
        this.getItems()[4] = new BuildItem(4, new ItemStack(Material.WOOD, 64), ItemType.MISC);
        this.getItems()[5] = new BuildItem(5, new ItemStack(Material.DIAMOND_AXE, 1), ItemType.MISC);
        this.getItems()[6] = new BuildItem(6, new ItemStack(Material.DIAMOND_PICKAXE, 1), ItemType.MISC);
        this.getItems()[7] = new BuildItem(7, new ItemStack(Material.GOLDEN_APPLE, 9), ItemType.FOOD);
        this.getItems()[8] = new BuildItem(8, new ItemStack(Material.COOKED_BEEF, 64), ItemType.FOOD);
        this.getItems()[9] = new BuildItem(9, new ItemStack(Material.LAVA_BUCKET, 1), ItemType.MISC);
        this.getItems()[10] = new BuildItem(9, new ItemStack(Material.LAVA_BUCKET, 1), ItemType.MISC);
        this.getItems()[11] = new BuildItem(10, new ItemStack(Material.WATER_BUCKET, 1), ItemType.MISC);
        this.getItems()[12] = new BuildItem(10, new ItemStack(Material.WATER_BUCKET, 1), ItemType.MISC);
        this.getItems()[27] = new BuildItem(11, new ItemStack(Material.ARROW, 64), ItemType.MISC);
    }
}
