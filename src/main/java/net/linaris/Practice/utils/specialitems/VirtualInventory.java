package net.linaris.Practice.utils.specialitems;

import org.bukkit.inventory.*;
import java.util.*;
import org.bukkit.entity.*;

public abstract class VirtualInventory implements InventoryHolder
{
    private static HashMap<Integer, VirtualInventory> registeredVirtualInventories;
    private static int autoIncrement;
    protected Inventory inventory;
    private SpecialItem inventorySpecialItem;
    protected String title;
    protected int id;
    public int row;
    public Map<Integer, SpecialItem> myItems;
    
    public VirtualInventory(final String title, final int row) {
        this.myItems = new HashMap<Integer, SpecialItem>();
        this.title = title;
        this.row = row;
    }
    
    @Deprecated
    public Map<Integer, SpecialItem> getMyItems() {
        return this.myItems;
    }
    
    public static VirtualInventory get(final int id) {
        return VirtualInventory.registeredVirtualInventories.get(id);
    }
    
    public static int registerVirtualInventory(final VirtualInventory virtualInventory) {
        final HashMap<Integer, VirtualInventory> registeredVirtualInventories = VirtualInventory.registeredVirtualInventories;
        final int id = VirtualInventory.autoIncrement++;
        virtualInventory.id = id;
        registeredVirtualInventories.put(id, virtualInventory);
        return virtualInventory.getId();
    }
    
    private Integer getId() {
        return this.id;
    }
    
    public Inventory getInventory() {
        return this.inventory;
    }
    
    public abstract void open(final Player p0);
    
    public abstract void openWithExtraText(final Player p0, final String p1);
    
    public String getTitle() {
        return this.trimTitle(this.title);
    }
    
    public String getTitleWithExtraText(final String string) {
        return this.trimTitle(this.title + " " + string);
    }
    
    private String trimTitle(String title) {
        if (title.length() > 32) {
            title = title.substring(0, 31);
        }
        return title;
    }
    
    public void addItem(final SpecialItem specialItem, final int slot) {
        this.myItems.put(slot, specialItem);
    }
    
    public void addItem(final int specialItemId, final int slot) {
        this.myItems.put(slot, SpecialItem.get(specialItemId));
    }
    
    public int addAndRegisterItem(final SpecialItem specialItem, final int slot) {
        final int id = SpecialItem.registerItem(specialItem);
        this.myItems.put(slot, specialItem);
        return id;
    }
    
    public int getIventorySlot() {
        return this.row * 9 - 1;
    }
    
    public void addItemOnLastSlot(final SpecialItem specialItem) {
        this.addItem(specialItem, this.getIventorySlot());
    }
    
    public void addItemOnLastSlot(final int specialItemId) {
        this.addItem(SpecialItem.get(specialItemId), this.getIventorySlot());
    }
    
    protected int setAndRegisterInventorySpecialItem(final SpecialItem specialItem) {
        final int id = SpecialItem.registerItem(specialItem);
        this.inventorySpecialItem = specialItem;
        return id;
    }
    
    public SpecialItem getInventorySpecialItem() {
        return this.inventorySpecialItem;
    }
    
    public int getPos(final int line, final int pos) {
        final int result = (line - 1) * 9 + (pos - 1);
        return result;
    }
    
    static {
        VirtualInventory.registeredVirtualInventories = new HashMap<Integer, VirtualInventory>();
    }
}
