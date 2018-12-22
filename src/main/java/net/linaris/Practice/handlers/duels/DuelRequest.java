package net.linaris.Practice.handlers.duels;

import java.util.*;

import net.linaris.Practice.handlers.games.*;
import net.linaris.Practice.handlers.players.*;

public class DuelRequest
{
    private UUID id;
    private OmegaPlayer player;
    private OmegaPlayer target;
    private GameType gameType;
    private long start;
    
    public DuelRequest(final OmegaPlayer player, final OmegaPlayer target, final GameType gameType) {
        this.id = UUID.randomUUID();
        this.player = player;
        this.target = target;
        this.gameType = gameType;
        this.start = System.currentTimeMillis();
    }
    
    public boolean isAvailable() {
        return this.player.isAvailable() && this.target.isAvailable();
    }
    
    public boolean isExpired() {
        return System.currentTimeMillis() - this.start > 60000L;
    }
    
    public UUID getId() {
        return this.id;
    }
    
    public OmegaPlayer getPlayer() {
        return this.player;
    }
    
    public OmegaPlayer getTarget() {
        return this.target;
    }
    
    public GameType getGameType() {
        return this.gameType;
    }
    
    public long getStart() {
        return this.start;
    }
}
