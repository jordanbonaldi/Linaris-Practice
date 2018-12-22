package net.linaris.Practice.handlers.queues;

import java.util.*;

import net.linaris.Practice.handlers.games.*;
import net.linaris.Practice.handlers.players.*;
import net.linaris.Practice.handlers.states.states.*;

public class Queue
{
    private final OmegaPlayer player;
    private final GameType type;
    private final boolean ranked;
    private final int elo;
    private final long start;
    
    public Queue(final OmegaPlayer player, final GameType type, final boolean ranked, final int elo) {
        this.player = player;
        this.type = type;
        this.ranked = ranked;
        this.elo = elo;
        this.start = System.currentTimeMillis();
    }
    
    public boolean updateAvailable() {
        return this.player.isOnline() && this.player.getState() instanceof InQueueState;
    }
    
    public boolean inInterval(final Queue queue) {
        return (!queue.isRanked() && !this.isRanked()) || ((!queue.isRanked() || this.isRanked()) && (queue.isRanked() || !this.isRanked()) && this.contains(queue.getElo()) && queue.contains(this.elo));
    }
    
    public boolean contains(final int elo) {
        final int[] interval = this.getInterval();
        return elo >= interval[0] && elo <= interval[1];
    }
    
    public int[] getInterval() {
        final int multi = this.getMultiplier();
        return new int[] { this.elo - multi, this.elo + multi };
    }
    
    public int getMultiplier() {
        int multi = (int)((System.currentTimeMillis() - this.start) / 1000L / 5L * 50L + 100L);
        if (multi > 400) {
            multi = 400;
        }
        return multi;
    }
    
    public UUID getUUID() {
        return this.player.getUuid();
    }
    
    public OmegaPlayer getPlayer() {
        return this.player;
    }
    
    public GameType getType() {
        return this.type;
    }
    
    public boolean isRanked() {
        return this.ranked;
    }
    
    public int getElo() {
        return this.elo;
    }
    
    public long getStart() {
        return this.start;
    }
}
