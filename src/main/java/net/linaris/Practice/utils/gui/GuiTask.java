package net.linaris.Practice.utils.gui;

import org.bukkit.scheduler.*;

import net.linaris.Practice.*;
import net.linaris.Practice.handlers.players.*;

import org.bukkit.event.*;

public class GuiTask extends BukkitRunnable
{
    private final OmegaPlayer p;
    private final GuiScreen gui;
    
    public GuiTask(final Practice plugin, final OmegaPlayer p, final GuiScreen gui) {
        this.p = p;
        (this.gui = gui).open();
    }
    
    public void run() {
        if (!this.gui.getInventory().getViewers().contains(this.p)) {
            this.cancel();
            return;
        }
        Practice.getInstance().getServer().getPluginManager().callEvent((Event)new GuiUpdateEvent(this.p, this.gui, false));
        this.gui.drawScreen();
    }
}
