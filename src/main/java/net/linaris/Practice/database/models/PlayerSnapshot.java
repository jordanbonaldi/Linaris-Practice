package net.linaris.Practice.database.models;

import net.linaris.Practice.database.redis.orm.Attribute;
import net.linaris.Practice.database.redis.orm.Id;
import net.linaris.Practice.database.redis.orm.Model;

@Model("playersnapshots")
public class PlayerSnapshot
{
    @Id
    private Long id;
    @Attribute
    private Long userId;
    @Attribute
    private String inventory;
    @Attribute
    private int health;
    @Attribute
    private int food;
    @Attribute
    private boolean winner;
    
    public PlayerSnapshot(final Long userId, final String inventory, final int health, final int food, final boolean winner) {
        this.userId = userId;
        this.inventory = inventory;
        this.health = health;
        this.food = food;
        this.winner = winner;
    }
    
    public PlayerSnapshot() {
    }
    
    public Long getId() {
        return this.id;
    }
    
    public Long getUserId() {
        return this.userId;
    }
    
    public String getInventory() {
        return this.inventory;
    }
    
    public int getHealth() {
        return this.health;
    }
    
    public int getFood() {
        return this.food;
    }
    
    public boolean isWinner() {
        return this.winner;
    }
}
