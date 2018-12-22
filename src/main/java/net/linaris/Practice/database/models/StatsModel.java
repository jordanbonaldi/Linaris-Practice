package net.linaris.Practice.database.models;

import net.linaris.Practice.database.redis.orm.*;

@Model("game_stats")
public class StatsModel extends ModelObject<StatsModel>
{
    @Id
    private Long id;
    @Indexed
    @Attribute
    private Long userId;
    @Indexed
    @Attribute
    private int gametype;
    @Attribute
    private int elo;
    @Attribute
    private int playedRanked;
    @Attribute
    private int played;
    @Attribute
    private int winRanked;
    @Attribute
    private int win;
    @Attribute
    private int loseRanked;
    @Attribute
    private int lose;
    
    public StatsModel(final Long userId, final int gametype) {
        this.userId = userId;
        this.gametype = gametype;
        this.elo = 1000;
    }
    
    @Override
    public void save(final Nest<StatsModel> nest) {
        nest.cat("sorted_by_elo").cat(Integer.toString(this.gametype)).zadd(this.elo, Long.toString(this.userId));
        nest.cat("sorted_by_win").cat(Integer.toString(this.gametype)).zadd(this.win, Long.toString(this.userId));
        nest.cat("sorted_by_lose").cat(Integer.toString(this.gametype)).zadd(this.lose, Long.toString(this.userId));
    }
    
    public void addWin() {
        ++this.win;
    }
    
    public void addWinRanked() {
        ++this.winRanked;
    }
    
    public void addLose() {
        ++this.lose;
    }
    
    public void addLoseRanked() {
        ++this.loseRanked;
    }
    
    public void addPlayed() {
        ++this.played;
    }
    
    public void addPlayedRanked() {
        ++this.playedRanked;
    }
    
    public void setElo(final int elo) {
        this.elo = elo;
        if (this.elo < 0) {
            this.elo = 0;
        }
    }
    
    public StatsModel() {
    }
    
    public Long getId() {
        return this.id;
    }
    
    public Long getUserId() {
        return this.userId;
    }
    
    public int getGametype() {
        return this.gametype;
    }
    
    public int getElo() {
        return this.elo;
    }
    
    public int getPlayedRanked() {
        return this.playedRanked;
    }
    
    public void setPlayedRanked(final int playedRanked) {
        this.playedRanked = playedRanked;
    }
    
    public int getPlayed() {
        return this.played;
    }
    
    public void setPlayed(final int played) {
        this.played = played;
    }
    
    public int getWinRanked() {
        return this.winRanked;
    }
    
    public void setWinRanked(final int winRanked) {
        this.winRanked = winRanked;
    }
    
    public int getWin() {
        return this.win;
    }
    
    public void setWin(final int win) {
        this.win = win;
    }
    
    public int getLoseRanked() {
        return this.loseRanked;
    }
    
    public void setLoseRanked(final int loseRanked) {
        this.loseRanked = loseRanked;
    }
    
    public int getLose() {
        return this.lose;
    }
    
    public void setLose(final int lose) {
        this.lose = lose;
    }
}
