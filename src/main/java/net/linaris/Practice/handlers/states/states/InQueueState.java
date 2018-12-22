package net.linaris.Practice.handlers.states.states;

import net.linaris.Practice.handlers.players.*;
import net.linaris.Practice.handlers.states.*;
import net.linaris.Practice.handlers.states.items.queue.*;

public class InQueueState extends State
{
    private static final QuitQueueItem quitqueue_item;
    
    public InQueueState(final OmegaPlayer player) {
        super("InQueue", player);
    }
    
    @Override
    public void onEnter() {
        this.getPlayer().reset();
        this.getPlayer().getInventory().setItem(0, InQueueState.quitqueue_item.getStaticItem());
        this.getPlayer().getInventory().setItem(1, InQueueState.quitqueue_item.getStaticItem());
        this.getPlayer().getInventory().setItem(7, InQueueState.quitqueue_item.getStaticItem());
        this.getPlayer().getInventory().setItem(8, InQueueState.quitqueue_item.getStaticItem());
    }
    
    @Override
    public void onExit() {
    }
    
    static {
        quitqueue_item = new QuitQueueItem();
    }
}
