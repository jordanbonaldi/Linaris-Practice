package net.linaris.Practice.handlers.games;

import org.bukkit.event.*;

import net.linaris.Practice.handlers.matchs.*;

public class GameTypeListener implements Listener
{
    protected Match match;
    
    public GameTypeListener(final Match match) {
        this.match = match;
    }
}
