package net.linaris.Practice.database.models;

import net.linaris.Practice.database.redis.orm.*;

@Model("builds")
public class BuildModel
{
    @Id
    private Long id;
    @Indexed
    @Attribute
    private Long userId;
    @Attribute
    private String name;
    @Indexed
    @Attribute
    private int gametype;
    @Array(of = Integer.class, length = 36)
    private Integer[] items;
    
    public BuildModel(final Long userId, final String name, final int gametype) {
        this.userId = userId;
        this.name = name;
        this.gametype = gametype;
        this.items = new Integer[36];
    }
    
    public BuildModel() {
    }
    
    public Long getId() {
        return this.id;
    }
    
    public Long getUserId() {
        return this.userId;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public int getGametype() {
        return this.gametype;
    }
    
    public Integer[] getItems() {
        return this.items;
    }
    
    public void setItems(final Integer[] items) {
        this.items = items;
    }
}
