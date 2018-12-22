package net.linaris.Practice.handlers.games.types;

import org.bukkit.*;
import org.bukkit.inventory.*;

import net.linaris.Practice.handlers.games.*;
import net.linaris.Practice.handlers.kits.*;
import net.linaris.Practice.handlers.kits.defaults.*;
import net.linaris.Practice.handlers.maps.*;
import net.linaris.Practice.utils.*;

import org.bukkit.enchantments.*;

public class SnowType extends GameType
{
    public SnowType() {
        super(2, "Archer", true, true, SnowListener.class);
        this.setDisplay(new ItemStack(Material.BOW));
        this.getAvailablesMaps().add(new MapSnow());
        this.setDefaultBuild(new SnowBuild());
        this.getArmor()[3] = new ItemBuilder(Material.LEATHER_HELMET).build();
        this.getArmor()[2] = new ItemBuilder(Material.LEATHER_CHESTPLATE).build();
        this.getArmor()[1] = new ItemBuilder(Material.LEATHER_LEGGINGS).build();
        this.getArmor()[0] = new ItemBuilder(Material.LEATHER_BOOTS).build();
    }
}
