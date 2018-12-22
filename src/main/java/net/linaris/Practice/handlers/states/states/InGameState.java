package net.linaris.Practice.handlers.states.states;

import net.linaris.Practice.handlers.matchs.*;
import net.linaris.Practice.handlers.players.*;
import net.linaris.Practice.handlers.states.*;

public class InGameState extends State
{
    private Match match;
    
    public InGameState(final OmegaPlayer player, final Match match) {
        super("InGame", player);
        this.match = match;
    }
    
    @Override
    public void onEnter() {
    }
    
    @Override
    public void onExit() {
    }
    
    public Match getMatch() {
        return this.match;
    }
}
