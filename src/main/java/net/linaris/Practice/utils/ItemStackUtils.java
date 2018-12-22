package net.linaris.Practice.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.craftbukkit.v1_8_R3.inventory.*;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.inventory.meta.*;
import java.util.*;

public class ItemStackUtils
{
    public static ItemStack addGlow(final ItemStack item) {
        final net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = null;
        if (!nmsStack.hasTag()) {
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
        }
        if (tag == null) {
            tag = nmsStack.getTag();
        }
        final NBTTagList ench = new NBTTagList();
        tag.set("ench", (NBTBase)ench);
        nmsStack.setTag(tag);
        return (ItemStack)CraftItemStack.asCraftMirror(nmsStack);
    }
    
    public static ItemStack createItem(final Material leatherPiece, final String displayName, final Color color) {
        final ItemStack item = new ItemStack(leatherPiece);
        final LeatherArmorMeta meta = (LeatherArmorMeta)item.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setColor(Color.fromRGB(color.asRGB()));
        item.setItemMeta((ItemMeta)meta);
        return item;
    }
    
    public static Boolean hasDisplayName(final ItemStack itemStack) {
        if (!isValid(itemStack)) {
            return false;
        }
        if (!itemStack.hasItemMeta()) {
            return false;
        }
        if (!itemStack.getItemMeta().hasDisplayName()) {
            return false;
        }
        return true;
    }
    
    public static String getDisplayName(final ItemStack itemStack) {
        if (hasDisplayName(itemStack)) {
            return itemStack.getItemMeta().getDisplayName();
        }
        return null;
    }
    
    public static boolean isValid(final ItemStack itemStack) {
        return itemStack != null && itemStack.getType() != Material.AIR && itemStack.getItemMeta().hasDisplayName();
    }
    
    public static boolean isArmor(final ItemStack item) {
        final Material type = item.getType();
        final String name = type.name();
        return name.contains("CHESTPLATE") || name.contains("BOOTS") || name.contains("HELMET") || name.contains("LEGGINGS");
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static ItemStack create(final Material material, final int data, final int nb, final String name, final List<String> lore) {
        final ItemStack item = new ItemStack(material, nb, (short)data);
        final ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore((List)lore);
        item.setItemMeta(itemMeta);
        item.setAmount(nb);
        return item;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static ItemStack create(final int id, final int data, final int nb, final String name, final List<String> lore) {
        @SuppressWarnings("deprecation")
		final ItemStack item = new ItemStack(id, nb, (short)data);
        final ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore((List)lore);
        item.setItemMeta(itemMeta);
        item.setAmount(nb);
        return item;
    }
}
