package net.linaris.Practice.handlers.states;

import net.linaris.Practice.*;
import net.linaris.Practice.handlers.players.*;
import net.linaris.Practice.handlers.states.states.*;

public class StatesManager
{
    private static final Practice api;
    
    public static void defaultState(final OmegaPlayer player) {
        player.setState(new LobbyState(player));
        player.teleport(StatesManager.api.getLobbyLocation());
    }
    
    static {
        api = Practice.getInstance();
    }
}
