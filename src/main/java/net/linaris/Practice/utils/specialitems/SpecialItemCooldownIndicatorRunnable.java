package net.linaris.Practice.utils.specialitems;

import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.inventory.*;

import net.linaris.Practice.utils.*;

public class SpecialItemCooldownIndicatorRunnable implements Runnable
{
    private Player player;
    private SpecialItem special;
    private String taskName;
    private int cooldown;
    private String cdPrefix;
    private ChatColor cdColor;
    private String cdSuffix;
    private Runnable reloadingAction;
    private Runnable readyAction;
    
    public SpecialItemCooldownIndicatorRunnable(final Player player, final SpecialItem specialItem, final int cooldown) {
        this.cooldown = 0;
        this.cdPrefix = "§7(";
        this.cdColor = ChatColor.AQUA;
        this.cdSuffix = "§7)";
        this.player = player;
        this.special = specialItem;
        this.cooldown = cooldown;
    }
    
    public SpecialItemCooldownIndicatorRunnable(final Player player, final SpecialItem specialItem, final int cooldown, final String cdPrefix, final ChatColor cdColor, final String cdSuffix) {
        this(player, specialItem, cooldown);
        if (cdPrefix != null) {
            this.cdPrefix = cdPrefix;
        }
        if (cdColor != null) {
            this.cdColor = cdColor;
        }
        if (cdSuffix != null) {
            this.cdSuffix = cdSuffix;
        }
    }
    
    public SpecialItemCooldownIndicatorRunnable(final Player player, final SpecialItem specialItem, final int cooldown, final String cdPrefix, final ChatColor cdColor, final String cdSuffix, final Runnable reloadingAction, final Runnable readyAction) {
        this(player, specialItem, cooldown, cdPrefix, cdColor, cdSuffix);
        if (reloadingAction != null) {
            this.reloadingAction = reloadingAction;
        }
        if (readyAction != null) {
            this.readyAction = readyAction;
        }
    }
    
    public void start() {
        TaskManager.scheduleSyncRepeatingTask(this.taskName = "SpecialItemCooldownIndicatorRunnable" + this.player.getName() + this.special.getName(), this, 0, 20);
    }
    
    @Override
    public void run() {
        if (!this.player.isOnline()) {
            TaskManager.cancelTaskByName(this.taskName);
            return;
        }
        final ItemStack item = this.special.getFirstInInventory((Inventory)this.player.getInventory());
        if (item == null) {
            TaskManager.cancelTaskByName(this.taskName);
            return;
        }
        final StringBuilder itemName = new StringBuilder();
        itemName.append(this.special.getName());
        if (this.cooldown > 0) {
            itemName.append(" ").append(this.cdPrefix).append(this.cdColor).append(this.cooldown).append(" seconde");
            if (this.cooldown > 1) {
                itemName.append("s");
            }
            itemName.append(this.cdSuffix);
            if (this.reloadingAction != null) {
                this.reloadingAction.run();
            }
        }
        SpecialItem.rename(item, itemName.toString());
        if (this.cooldown <= 0) {
            if (this.readyAction != null) {
                this.readyAction.run();
            }
            TaskManager.cancelTaskByName(this.taskName);
            return;
        }
        --this.cooldown;
    }
}
