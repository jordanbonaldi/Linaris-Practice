package net.linaris.Practice.utils.specialitems;

import org.bukkit.inventory.meta.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.plugin.*;

import net.linaris.Practice.*;

import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import java.util.*;

public abstract class SpecialItem
{
    private static HashMap<Integer, SpecialItem> registeredItems;
    private static boolean enabled;
    private static int autoIncrement;
    private ItemStack itemStack;
    private String name;
    private List<String> lore;
    private int id;
    private boolean rightClickable;
    private boolean leftClickable;
    private boolean droppable;
    private boolean inventoryClickable;
    private boolean movable;
    private List<Material> disabledMaterialClick;
    private boolean cancelInteractEvent;
    
    public SpecialItem(final String name, final String[] lore, final ItemStack itemStack) {
        this.rightClickable = false;
        this.leftClickable = false;
        this.droppable = true;
        this.inventoryClickable = false;
        this.movable = true;
        this.disabledMaterialClick = new ArrayList<Material>();
        this.cancelInteractEvent = true;
        this.name = name;
        this.lore = ((lore != null) ? Arrays.asList(lore) : null);
        this.itemStack = itemStack;
        this.buildItem();
    }
    
    public SpecialItem(final String name, final ItemStack itemStack, final String... lore) {
        this.rightClickable = false;
        this.leftClickable = false;
        this.droppable = true;
        this.inventoryClickable = false;
        this.movable = true;
        this.disabledMaterialClick = new ArrayList<Material>();
        this.cancelInteractEvent = true;
        this.name = name;
        this.lore = ((lore != null) ? Arrays.asList(lore) : null);
        this.itemStack = itemStack;
        this.buildItem();
    }
    
    public static void rename(final ItemStack itemStack, final String newName) {
        final SpecialItem specialItem = getSpecialItem(itemStack);
        if (specialItem != null) {
            final ItemMeta im = itemStack.getItemMeta();
            im.setDisplayName(specialItem.encodeInName(newName, specialItem.id));
            itemStack.setItemMeta(im);
        }
    }
    
    protected String getEncodedNewName(final String newName) {
        return this.encodeInName(newName, this.id);
    }
    
    public boolean isTheSame(final ItemStack itemStack) {
        final SpecialItem item = getSpecialItem(itemStack);
        return item != null && this.id == item.id;
    }
    
    public boolean isItemInHand(final Player player) {
        return this.isTheSame(player.getItemInHand());
    }
    
    public List<String> getLore() {
        if (this.itemStack.getItemMeta().getLore() == null) {
            return new ArrayList<String>();
        }
        return (List<String>)this.itemStack.getItemMeta().getLore();
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void buildItem() {
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.setDisplayName(this.name);
        if (this.lore != null) {
            itemMeta.setLore((List)this.lore);
        }
        this.itemStack.setItemMeta(itemMeta);
    }
    
    public static SpecialItem get(final Integer id) {
        return SpecialItem.registeredItems.get(id);
    }
    
    public static int registerItem(final SpecialItem item) {
        if (!SpecialItem.enabled) {
            registerListeners();
        }
        item.encodeItemStack();
        SpecialItem.registeredItems.put(item.getId(), item);
        return item.getId();
    }
    
    public void remove() {
        SpecialItem.registeredItems.remove(this.getId());
    }
    
    private static void registerListeners() {
        final PluginManager pm = Practice.getInstance().getServer().getPluginManager();
        pm.registerEvents((Listener)new SpecialItemInventoryClickListener(), (Plugin)Practice.getInstance());
        pm.registerEvents((Listener)new SpecialItemInventoryMoveItemListener(), (Plugin)Practice.getInstance());
        pm.registerEvents((Listener)new SpecialItemPlayerInteractListener(), (Plugin)Practice.getInstance());
        pm.registerEvents((Listener)new SpecialItemPlayerInteractEntityListener(), (Plugin)Practice.getInstance());
        pm.registerEvents((Listener)new SpecialItemPlayerDropItemListener(), (Plugin)Practice.getInstance());
        pm.registerEvents((Listener)new SpecialItemPlayerItemConsumeListener(), (Plugin)Practice.getInstance());
        SpecialItem.enabled = true;
    }
    
    public static SpecialItem getSpecialItem(final ItemStack itemStack) {
        if (itemStack != null && itemStack.getType() != Material.AIR && itemStack.getItemMeta().hasDisplayName()) {
            return SpecialItem.registeredItems.get(decodeId(itemStack.getItemMeta().getDisplayName()));
        }
        return null;
    }
    
    protected void rightClickEvent(final PlayerInteractEvent event) {
    }
    
    protected void rightClickEvent(final InventoryClickEvent event) {
    }
    
    protected void leftClickEvent(final PlayerInteractEvent event) {
    }
    
    protected void leftClickEvent(final InventoryClickEvent event) {
    }
    
    public void middleClickEvent(final InventoryClickEvent event) {
    }
    
    public void inventoryClickEvent(final Player player) {
    }
    
    protected void inventoryClickEvent(final InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            this.inventoryClickEvent((Player)event.getWhoClicked());
        }
    }
    
    private SpecialItem encodeItemStack() {
        final ItemMeta itemMeta2;
        final ItemMeta itemMeta = itemMeta2 = this.itemStack.getItemMeta();
        final String displayName = itemMeta.getDisplayName();
        final int n = SpecialItem.autoIncrement++;
        this.id = n;
        itemMeta2.setDisplayName(this.encodeInName(displayName, n));
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }
    
    private String encodeInName(final String itemName, final int itemID) {
        final String id = Integer.toString(itemID);
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < id.length(); ++i) {
            builder.append("§").append(id.charAt(i));
        }
        final String result = (Object)builder + "§S" + itemName;
        return result;
    }
    
    private static int decodeId(String itemName) {
        int intId = -1;
        if (itemName.contains("§S")) {
            final String[] stringID = itemName.split("§S");
            if (stringID.length > 0) {
                itemName = stringID[0].replaceAll("§", "");
                try {
                    intId = Integer.parseInt(itemName);
                }
                catch (NumberFormatException ex) {}
            }
        }
        return intId;
    }
    
    protected void dropEvent(final PlayerDropItemEvent e) {
    }
    
    public ItemStack getStaticItem() {
        return this.itemStack;
    }
    
    public ItemStack getClonedItem() {
        return this.itemStack.clone();
    }
    
    public ItemStack getClonedItem(final int amount, final short durability) {
        final ItemStack item = this.itemStack.clone();
        if (amount > 1) {
            item.setAmount(amount);
        }
        if (durability > 0) {
            item.setDurability(durability);
        }
        return item;
    }
    
    public void setItem(final ItemStack item) {
        this.itemStack = item;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getUnEncodedName() {
        String result = this.name;
        if (result.contains("§S")) {
            final String[] stringID = result.split("§S");
            if (stringID.length > 1) {
                result = stringID[1];
            }
        }
        return result;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public List<String> getItemLore() {
        return (this.getStaticItem().getItemMeta().getLore() == null) ? new ArrayList<String>() : this.getStaticItem().getItemMeta().getLore();
    }
    
    public List<String> getCustomLore() {
        return this.lore;
    }
    
    public void setLore(final List<String> lore) {
        this.lore = lore;
    }
    
    public boolean isRightClickable() {
        return this.rightClickable;
    }
    
    protected void setRightClickable(final boolean rightClickable) {
        this.rightClickable = rightClickable;
    }
    
    public boolean isLeftClickable() {
        return this.leftClickable;
    }
    
    protected void setLeftClickable(final boolean leftClickable) {
        this.leftClickable = leftClickable;
    }
    
    public boolean isDroppable() {
        return this.droppable;
    }
    
    public void setDroppable(final boolean droppable) {
        this.droppable = droppable;
    }
    
    public boolean isInventoryClickable() {
        return this.inventoryClickable;
    }
    
    protected void setInventoryClickable(final boolean inventoryClickable) {
        this.inventoryClickable = inventoryClickable;
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setMovable(final boolean movable) {
        this.movable = movable;
    }
    
    public boolean isMovable() {
        return this.movable;
    }
    
    public List<Material> getDisabledMaterialClick() {
        return this.disabledMaterialClick;
    }
    
    public void addDisabledMaterialClick(final Material material) {
        if (!this.disabledMaterialClick.contains(material)) {
            this.disabledMaterialClick.add(material);
        }
    }
    
    public void removeDisabledMaterialClick(final Material material) {
        this.disabledMaterialClick.remove(material);
    }
    
    public static void addDefaultDisabledMaterials(final SpecialItem item) {
        item.addDisabledMaterialClick(Material.WOODEN_DOOR);
        item.addDisabledMaterialClick(Material.TRAPPED_CHEST);
        item.addDisabledMaterialClick(Material.TRAP_DOOR);
        item.addDisabledMaterialClick(Material.CHEST);
        item.addDisabledMaterialClick(Material.WOOD_BUTTON);
        item.addDisabledMaterialClick(Material.STONE_BUTTON);
        item.addDisabledMaterialClick(Material.WORKBENCH);
        item.addDisabledMaterialClick(Material.ENDER_CHEST);
        item.addDisabledMaterialClick(Material.BREWING_STAND);
        item.addDisabledMaterialClick(Material.LEVER);
    }
    
    public void runCooldown(final Player player, final int cooldown) {
        new SpecialItemCooldownIndicatorRunnable(player, this, cooldown).start();
    }
    
    public void runCooldown(final Player player, final int cooldown, final String cdPrefixe, final ChatColor cdColor, final String cdSuffixe) {
        new SpecialItemCooldownIndicatorRunnable(player, this, cooldown, cdPrefixe, cdColor, cdSuffixe).start();
    }
    
    public void runCooldown(final Player player, final int cooldown, final String cdPrefixe, final ChatColor cdColor, final String cdSuffixe, final Runnable reloadingRunnable, final Runnable readyRunnable) {
        new SpecialItemCooldownIndicatorRunnable(player, this, cooldown, cdPrefixe, cdColor, cdSuffixe, reloadingRunnable, readyRunnable).start();
    }
    
    public ItemStack getFirstInInventory(final Inventory inventory) {
        for (final ItemStack item : inventory) {
            if (this.isTheSame(item)) {
                return item;
            }
        }
        return null;
    }
    
    public static ItemStack getFirstInInventory(final Inventory inventory, final SpecialItem specialItem) {
        for (final ItemStack item : inventory) {
            if (specialItem.isTheSame(item)) {
                return item;
            }
        }
        return null;
    }
    
    public boolean isCancelInteractEvent() {
        return this.cancelInteractEvent;
    }
    
    public void setCancelInteractEvent(final boolean cancelInteractEvent) {
        this.cancelInteractEvent = cancelInteractEvent;
    }
    
    static {
        SpecialItem.registeredItems = new HashMap<Integer, SpecialItem>();
        SpecialItem.enabled = false;
    }
}
