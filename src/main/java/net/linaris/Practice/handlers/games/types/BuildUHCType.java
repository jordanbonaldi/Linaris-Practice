package net.linaris.Practice.handlers.games.types;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import net.linaris.Practice.handlers.games.GameType;
import net.linaris.Practice.handlers.kits.defaults.BuildUHCBuild;
import net.linaris.Practice.handlers.maps.Map16;
import net.linaris.Practice.handlers.maps.MapBUILDUHC;
import net.linaris.Practice.utils.ItemBuilder;

public class BuildUHCType extends GameType
{
    public BuildUHCType() {
        super(0, "BuildUHC", true, true, BuildUHCListener.class);
        this.setDisplay(new ItemStack(Material.GOLDEN_APPLE));
        this.getAvailablesMaps().add(new MapBUILDUHC());
        this.setDefaultBuild(new BuildUHCBuild());
        this.getArmor()[3] = new ItemBuilder(Material.DIAMOND_HELMET).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 2).build();
        this.getArmor()[2] = new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build();
        this.getArmor()[1] = new ItemBuilder(Material.DIAMOND_LEGGINGS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build();
        this.getArmor()[0] = new ItemBuilder(Material.DIAMOND_BOOTS).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 2).build();
    }
}
