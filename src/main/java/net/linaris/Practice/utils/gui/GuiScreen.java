package net.linaris.Practice.utils.gui;

import org.bukkit.plugin.*;

import net.linaris.Practice.*;
import net.linaris.Practice.handlers.players.*;
import net.linaris.Practice.utils.*;

import org.bukkit.inventory.*;
import org.bukkit.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.*;

public abstract class GuiScreen implements Listener
{
    Inventory inv;
    OmegaPlayer p;
    boolean update;
    String name;
    int size;
    
    public GuiScreen(final String name, final int size, final OmegaPlayer p, final boolean update) {
        this.name = name;
        this.size = size;
        this.p = p;
        this.update = update;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void build() {
        this.inv = Practice.getInstance().getServer().createInventory((InventoryHolder)null, this.size * 9, this.name);
    }
    
    public OmegaPlayer getPlayer() {
        return this.p;
    }
    
    public boolean isUpdate() {
        return this.update;
    }
    
    public abstract void drawScreen();
    
    @SuppressWarnings("deprecation")
	public void open() {
        this.p.openInventory(this.inv);
        this.drawScreen();
        this.p.updateInventory();
        Practice.getInstance().getServer().getPluginManager().registerEvents((Listener)this, (Plugin)Practice.getInstance());
        this.onOpen();
    }
    
    public void close() {
        this.p.closeInventory();
    }
    
    public Inventory getInventory() {
        return this.inv;
    }
    
    public void setItem(final ItemStack item, final int slot) {
        this.inv.setItem(slot, item);
    }
    
    public void setItem(final ItemStack item, final int slot, final int buttonId) {
        this.inv.setItem(slot, new NBTItem(item).setInteger("button_id", buttonId).getItem());
    }
    
    public void addItem(final ItemStack item) {
        this.inv.addItem(new ItemStack[] { item });
    }
    
    public void setItemLine(final ItemStack item, final int line, final int slot) {
        this.setItem(item, line * 9 - 9 + slot - 1);
    }
    
    public void setItemLine(final ItemStack item, final int line, final int slot, final int buttonId) {
        this.setItem(item, line * 9 - 9 + slot - 1, buttonId);
    }
    
    public int getButtonId(final ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return -1;
        }
        final NBTItem nbt = new NBTItem(item);
        if (!nbt.hasKey("button_id")) {
            return -1;
        }
        return nbt.getInteger("button_id");
    }
    
    public void clearInventory() {
        this.inv.clear();
    }
    
    public void setFont(final ItemStack item) {
        for (int i = 0; i < this.inv.getSize(); ++i) {
            this.setItem(item, i);
        }
    }
    
    @EventHandler
    public void onPlayerInventory(final InventoryClickEvent e) {
        if (e.getClickedInventory() == null) {
            return;
        }
        if (e.getClickedInventory().equals(this.inv)) {
            this.onClick(e.getCurrentItem(), e);
        }
    }
    
    public abstract void onOpen();
    
    public abstract void onClick(final ItemStack p0, final InventoryClickEvent p1);
    
    public abstract void onClose();
    
    @EventHandler
    public void onPlayerInventory(final InventoryCloseEvent e) {
        this.onClose();
        if (!GuiManager.isOpened(this.getClass())) {
            HandlerList.unregisterAll((Listener)this);
        }
    }
}
