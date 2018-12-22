package net.linaris.Practice.utils;

import org.bukkit.enchantments.*;
import org.bukkit.inventory.*;
import org.bukkit.*;
import org.bukkit.inventory.meta.*;
import java.util.*;

public class ItemBuilder
{
    private Material material;
    private short durability;
    private int amount;
    private List<String> lore;
    private String name;
    private Map<Enchantment, Integer> enchantments;
    private Color color;
    private ItemMeta itemMeta;
    private List<ItemFlag> itemFlags;
    
    public ItemBuilder(final Material material) {
        this.durability = 0;
        this.amount = 1;
        this.lore = new ArrayList<String>();
        this.enchantments = new HashMap<Enchantment, Integer>();
        this.itemFlags = new ArrayList<ItemFlag>();
        this.material = material;
    }
    
    public ItemBuilder(final Material material, final short durability) {
        this.durability = 0;
        this.amount = 1;
        this.lore = new ArrayList<String>();
        this.enchantments = new HashMap<Enchantment, Integer>();
        this.itemFlags = new ArrayList<ItemFlag>();
        this.material = material;
        this.durability = durability;
    }
    
    public static ItemBuilder fromItemStack(final ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }
        final ItemBuilder itemBuilder = new ItemBuilder(itemStack.getType());
        itemBuilder.setDurability(itemStack.getDurability());
        itemBuilder.setAmount(itemStack.getAmount());
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemBuilder.setItemMeta(itemMeta);
        itemBuilder.setLore(itemMeta.getLore());
        itemBuilder.setName(itemMeta.getDisplayName());
        return itemBuilder;
    }
    
    public ItemBuilder addLore(final String line) {
        this.getLore().add(line);
        return this;
    }
    
    public ItemBuilder addLore(final String... lines) {
        final List<String> line = Arrays.asList(lines);
        this.lore.addAll(line);
        return this;
    }
    
    public ItemBuilder clearLore() {
        this.getLore().clear();
        return this;
    }
    
    public List<String> getLore() {
        if (this.lore == null) {
            return this.lore = new ArrayList<String>();
        }
        return this.lore;
    }
    
    public ItemBuilder setLore(final List<String> lore) {
        this.lore = lore;
        return this;
    }
    
    public ItemBuilder setItemMeta(final ItemMeta itemMeta) {
        this.itemMeta = itemMeta;
        return this;
    }
    
    public ItemBuilder setMaterial(final Material material) {
        this.material = material;
        return this;
    }
    
    public ItemBuilder setDurability(final short durability) {
        this.durability = durability;
        return this;
    }
    
    public ItemBuilder setAmount(final int amount) {
        this.amount = amount;
        return this;
    }
    
    public ItemBuilder setName(final String name) {
        this.name = name;
        return this;
    }
    
    public ItemBuilder setColor(final Color color) {
        this.color = color;
        return this;
    }
    
    public ItemBuilder setEnchantments(final Map<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
        return this;
    }
    
    public ItemBuilder addEnchantment(final Enchantment ench, final int level) {
        this.enchantments.put(ench, level);
        return this;
    }
    
    public ItemBuilder removeEnchantment(final Enchantment ench) {
        this.enchantments.remove(ench);
        return this;
    }
    
    public ItemBuilder addItemFlag(final ItemFlag itemFlag) {
        this.itemFlags.add(itemFlag);
        return this;
    }
    
    public ItemBuilder removeItemFlag(final ItemFlag itemFlag) {
        this.itemFlags.remove(itemFlag);
        return this;
    }
    
    public ItemBuilder clearItemFlags() {
        this.itemFlags.clear();
        return this;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public ItemStack build() {
        final ItemStack itemStack = new ItemStack(this.material);
        itemStack.setAmount(this.amount);
        itemStack.setDurability(this.durability);
        final ItemMeta itemMeta = (this.itemMeta == null) ? itemStack.getItemMeta() : this.itemMeta;
        if (this.lore != null && !this.lore.isEmpty()) {
            itemMeta.setLore((List)this.lore);
        }
        if (this.name != null) {
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.name));
        }
        if (!this.enchantments.isEmpty()) {
            for (final Map.Entry<Enchantment, Integer> entry : this.enchantments.entrySet()) {
                itemMeta.addEnchant((Enchantment)entry.getKey(), (int)entry.getValue(), true);
            }
        }
        if (this.color != null && itemMeta instanceof LeatherArmorMeta) {
            ((LeatherArmorMeta)itemMeta).setColor(this.color);
        }
        if (!this.itemFlags.isEmpty()) {
            final ItemFlag[] flags = this.itemFlags.toArray(new ItemFlag[this.itemFlags.size()]);
            itemMeta.addItemFlags(flags);
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
    public ItemBuilder cloneItemBuilder() {
        final ItemBuilder itemBuilder = new ItemBuilder(this.material);
        itemBuilder.setDurability(this.durability);
        itemBuilder.setAmount(this.amount);
        final List<String> lore = new ArrayList<String>();
        if (lore.size() > 0) {
            lore.addAll(this.lore);
        }
        itemBuilder.setLore(lore);
        itemBuilder.setName(this.name);
        itemBuilder.setEnchantments(this.enchantments);
        itemBuilder.setColor(this.color);
        return itemBuilder;
    }
    
    public int getAmount() {
        return this.amount;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Map<Enchantment, Integer> getEnchantments() {
        return this.enchantments;
    }
    
    public Color getColor() {
        return this.color;
    }
    
    public ItemMeta getItemMeta() {
        return this.itemMeta;
    }
    
    public List<ItemFlag> getItemFlags() {
        return this.itemFlags;
    }
}
