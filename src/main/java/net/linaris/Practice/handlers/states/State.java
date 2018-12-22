package net.linaris.Practice.handlers.states;

import net.linaris.Practice.handlers.players.*;

public abstract class State
{
    private String name;
    private OmegaPlayer player;
    
    public State(final String name, final OmegaPlayer player) {
        this.name = name;
        this.player = player;
    }
    
    public void enter() {
        this.onEnter();
    }
    
    public void exit() {
        this.onExit();
    }
    
    public abstract void onEnter();
    
    public abstract void onExit();
    
    public String getName() {
        return this.name;
    }
    
    public OmegaPlayer getPlayer() {
        return this.player;
    }
}
