package net.linaris.Practice.handlers.games.types;

import org.bukkit.*;
import org.bukkit.inventory.*;

import net.linaris.Practice.handlers.games.*;
import net.linaris.Practice.handlers.kits.*;
import net.linaris.Practice.handlers.kits.defaults.*;
import net.linaris.Practice.handlers.maps.*;
import net.linaris.Practice.utils.*;

import org.bukkit.enchantments.*;

public class PotionsType extends GameType
{
    public PotionsType() {
        super(1, "Potions", true, true, PotionsListener.class);
        this.setDisplay(new ItemStack(Material.POTION));
        this.getAvailablesMaps().add(new MapPOTION());
        this.setDefaultBuild(new PotionBuild());
        this.getArmor()[3] = new ItemBuilder(Material.DIAMOND_HELMET).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchantment(Enchantment.DURABILITY, 3).build();
        this.getArmor()[2] = new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchantment(Enchantment.DURABILITY, 3).build();
        this.getArmor()[1] = new ItemBuilder(Material.DIAMOND_LEGGINGS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchantment(Enchantment.DURABILITY, 3).build();
        this.getArmor()[0] = new ItemBuilder(Material.DIAMOND_BOOTS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchantment(Enchantment.DURABILITY, 3).build();
    }
}
