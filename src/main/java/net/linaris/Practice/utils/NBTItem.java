package net.linaris.Practice.utils;

import org.bukkit.inventory.*;

public class NBTItem
{
    private ItemStack bukkititem;
    
    public NBTItem(final ItemStack Item) {
        this.bukkititem = Item.clone();
    }
    
    public ItemStack getItem() {
        return this.bukkititem;
    }
    
    public NBTItem setString(final String Key, final String Text) {
        this.bukkititem = NBTReflectionutil.setString(this.bukkititem, Key, Text);
        return this;
    }
    
    public String getString(final String Key) {
        return NBTReflectionutil.getString(this.bukkititem, Key);
    }
    
    public NBTItem setInteger(final String key, final Integer Int) {
        this.bukkititem = NBTReflectionutil.setInt(this.bukkititem, key, Int);
        return this;
    }
    
    public Integer getInteger(final String key) {
        return NBTReflectionutil.getInt(this.bukkititem, key);
    }
    
    public NBTItem setDouble(final String key, final Double d) {
        this.bukkititem = NBTReflectionutil.setDouble(this.bukkititem, key, d);
        return this;
    }
    
    public Double getDouble(final String key) {
        return NBTReflectionutil.getDouble(this.bukkititem, key);
    }
    
    public NBTItem setBoolean(final String key, final Boolean b) {
        this.bukkititem = NBTReflectionutil.setBoolean(this.bukkititem, key, b);
        return this;
    }
    
    public Boolean getBoolean(final String key) {
        return NBTReflectionutil.getBoolean(this.bukkititem, key);
    }
    
    public Boolean hasKey(final String key) {
        return NBTReflectionutil.hasKey(this.bukkititem, key);
    }
}
