package net.linaris.Practice.handlers.kits.defaults;

import org.bukkit.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.*;

import net.linaris.Practice.handlers.kits.*;
import net.linaris.Practice.utils.*;

public class PotionBuild extends DefaultBuild
{
    public PotionBuild() {
        super("Potions Defaut");
        this.getItems()[0] = new BuildItem(0, new ItemBuilder(Material.DIAMOND_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 1).addEnchantment(Enchantment.DURABILITY, 3).addEnchantment(Enchantment.FIRE_ASPECT, 2).build(), ItemType.WEAPONS);
        final BuildItem healPotion = new BuildItem(1, new ItemStack(373, 1, (short)16421), ItemType.POTION);
        for (int i = 1; i < 36; ++i) {
            this.getItems()[i] = healPotion;
        }
        final BuildItem speedPotion = new BuildItem(2, new ItemStack(373, 1, (short)8226), ItemType.POTION);
        this.getItems()[5] = speedPotion;
        this.getItems()[6] = new BuildItem(3, new ItemStack(373, 1, (short)8259), ItemType.POTION);
        this.getItems()[7] = new BuildItem(4, new ItemStack(Material.ENDER_PEARL, 16), ItemType.POTION);
        this.getItems()[8] = new BuildItem(5, new ItemStack(Material.COOKED_BEEF, 64), ItemType.FOOD);
        this.getItems()[9] = speedPotion;
    }
}
