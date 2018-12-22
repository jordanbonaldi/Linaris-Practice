package net.linaris.Practice.utils.gui;

import org.bukkit.event.*;

import net.linaris.Practice.handlers.players.*;

public class GuiUpdateEvent extends Event
{
    private static final HandlerList handlers;
    private OmegaPlayer player;
    private GuiScreen gui;
    
    public GuiUpdateEvent(final OmegaPlayer player, final GuiScreen gui, final boolean who) {
        super(who);
        this.player = player;
        this.gui = gui;
    }
    
    public GuiScreen getGui() {
        return this.gui;
    }
    
    public OmegaPlayer getPlayer() {
        return this.player;
    }
    
    public HandlerList getHandlers() {
        return GuiUpdateEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return GuiUpdateEvent.handlers;
    }
    
    static {
        handlers = new HandlerList();
    }
}
