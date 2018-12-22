package net.linaris.Practice.utils;

import org.bukkit.*;
import org.bukkit.plugin.*;

import net.linaris.Practice.*;

import org.bukkit.event.*;

public class EventListener implements Listener
{
    private boolean registered;
    private Practice plugin;
    
    public EventListener(final Practice plugin) {
        this.registered = false;
        this.plugin = plugin;
    }
    
    public void register() {
        if (!this.registered) {
            this.registered = true;
            Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)this.plugin);
        }
    }
    
    public void unregister() {
        if (this.registered) {
            this.registered = false;
            HandlerList.unregisterAll((Listener)this);
        }
    }
}
