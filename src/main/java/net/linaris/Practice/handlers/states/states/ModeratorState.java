package net.linaris.Practice.handlers.states.states;

import org.bukkit.scheduler.*;

import net.linaris.Practice.*;
import net.linaris.Practice.handlers.players.*;
import net.linaris.Practice.handlers.states.*;
import net.linaris.Practice.handlers.states.items.moderator.*;

import org.bukkit.plugin.*;
import org.bukkit.entity.*;

public class ModeratorState extends State
{
    private static final ModeratorLeaveItem leave_item;
    private BukkitTask task;
    private OmegaPlayer target;
    
    public ModeratorState(final OmegaPlayer player) {
        super("Moderator", player);
    }
    
    @Override
    public void onEnter() {
        this.getPlayer().reset();
        this.getPlayer().setVanish(true);
        this.getPlayer().setAllowFlight(true);
        this.getPlayer().getInventory().setItem(8, ModeratorState.leave_item.getStaticItem());
        this.task = new BukkitRunnable() {
            public void run() {
                if (ModeratorState.this.target == null) {
                    ModeratorState.this.getPlayer().sendActionBar("§eQue voulez-vous faire ?");
                    return;
                }
                if (!ModeratorState.this.target.isOnline()) {
                    ModeratorState.this.target = null;
                    return;
                }
                ModeratorState.this.getPlayer().sendActionBar("§eVous observez §b" + ModeratorState.this.target.getName());
            }
        }.runTaskTimer((Plugin)Practice.getInstance(), 20L, 20L);
    }
    
    public void lookAt(final OmegaPlayer target) {
        this.target = target;
        this.getPlayer().sendMessage("§eVous observez §b" + target.getName());
        this.getPlayer().sendMessage("");
        this.getPlayer().sendMessage("§3Son \u00e9tat actuel:§e " + target.getState().getName());
        this.getPlayer().sendMessage("§3Son rang:§e " + target.getData().getRank().getName());
        this.getPlayer().teleport((Entity)target.getPlayer());
    }
    
    @Override
    public void onExit() {
        this.task.cancel();
        this.getPlayer().setVanish(false);
    }
    
    static {
        leave_item = new ModeratorLeaveItem();
    }
}
