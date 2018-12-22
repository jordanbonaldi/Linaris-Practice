package net.linaris.Practice.database.models;

import java.util.*;

import net.linaris.Practice.database.redis.orm.*;

@Model("matchs")
public class MatchModel
{
    @Id
    private Long id;
    @Attribute
    private int map;
    @Attribute
    private int gametype;
    @Attribute
    private boolean ranked;
    @Indexed
    @CollectionMap(key = Long.class, value = PlayerSnapshot.class)
    private Map<Long, PlayerSnapshot> players;
    
    public MatchModel(final int map, final int gametype, final boolean ranked) {
        this.map = map;
        this.gametype = gametype;
        this.ranked = ranked;
    }
    
    public void addPlayer(final PlayerSnapshot snapshot) {
        this.players.put(snapshot.getUserId(), snapshot);
    }
    
    public MatchModel() {
    }
    
    public Long getId() {
        return this.id;
    }
    
    public int getMap() {
        return this.map;
    }
    
    public int getGametype() {
        return this.gametype;
    }
    
    public void setGametype(final int gametype) {
        this.gametype = gametype;
    }
    
    public boolean isRanked() {
        return this.ranked;
    }
    
    public void setRanked(final boolean ranked) {
        this.ranked = ranked;
    }
    
    public Map<Long, PlayerSnapshot> getPlayers() {
        return this.players;
    }
    
    public void setPlayers(final Map<Long, PlayerSnapshot> players) {
        this.players = players;
    }
}
