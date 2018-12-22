package net.linaris.Practice.database.models;

import net.linaris.Practice.database.redis.orm.Attribute;
import net.linaris.Practice.database.redis.orm.Id;
import net.linaris.Practice.database.redis.orm.Indexed;
import net.linaris.Practice.database.redis.orm.Model;
import net.linaris.Practice.database.redis.orm.ModelObject;
import net.linaris.Practice.database.redis.orm.Nest;
import net.linaris.Practice.handlers.kits.WeaponName;
import net.linaris.Practice.handlers.players.Rank;

@Model("users")
public class UserModel extends ModelObject<UserModel>
{
    @Id
    private Long id;
    @Indexed
    @Attribute
    private String uuid;
    @Attribute
    private String lastName;
    @Attribute
    private WeaponName weaponName;
    @Attribute
    private Rank rank;
    @Attribute
    private Long lastConnection;
    @Attribute
    private int rankedRemaining;
    @Attribute
    private int played;
    @Attribute
    private Long timePlayed;
    
    public UserModel(final String uuid, final String lastName) {
        this.uuid = uuid;
        this.lastName = lastName;
        this.timePlayed = 0L;
        this.lastConnection = 0L;
        this.rank = Rank.PLAYER;
    }
    
    public UserModel() {
        this.rank = Rank.PLAYER;
    }
    
    public Rank getRank() {
        if (this.rank == null) {
            this.rank = Rank.PLAYER;
        }
        return this.rank;
    }
    
    @Override
    public void save(final Nest<UserModel> nest) {
        nest.cat("sorted_by_timeplayed").zadd(this.timePlayed / 1000L, Long.toString(this.getId()));
    }
    
    public void removeRanked() {
        if (this.rank.getVipLevel() < 1) {
            --this.rankedRemaining;
        }
    }
    
    public void addPlayed() {
        ++this.played;
    }
    
    public Long getId() {
        return this.id;
    }
    
    public String getUuid() {
        return this.uuid;
    }
    
    public String getLastName() {
        return this.lastName;
    }
    
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }
    
    public WeaponName getWeaponName() {
        return this.weaponName;
    }
    
    public void setWeaponName(final WeaponName weaponName) {
        this.weaponName = weaponName;
    }
    
    public void setRank(final Rank rank) {
        this.rank = rank;
    }
    
    public Long getLastConnection() {
        return this.lastConnection;
    }
    
    public void setLastConnection(final Long lastConnection) {
        this.lastConnection = lastConnection;
    }
    
    public int getRankedRemaining() {
        return this.rankedRemaining;
    }
    
    public void setRankedRemaining(final int rankedRemaining) {
        this.rankedRemaining = rankedRemaining;
    }
    
    public int getPlayed() {
        return this.played;
    }
    
    public void setPlayed(final int played) {
        this.played = played;
    }
    
    public Long getTimePlayed() {
        return this.timePlayed;
    }
    
    public void setTimePlayed(final Long timePlayed) {
        this.timePlayed = timePlayed;
    }
}
