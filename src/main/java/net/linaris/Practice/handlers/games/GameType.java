package net.linaris.Practice.handlers.games;

import org.bukkit.inventory.*;

import net.linaris.Practice.database.redis.orm.*;
import net.linaris.Practice.handlers.kits.*;
import net.linaris.Practice.handlers.maps.Map;
import redis.clients.jedis.*;

import java.util.*;

public class GameType
{
    private final int id;
    private final String name;
    private final boolean ranked;
    private final boolean buildable;
    private List<Map> availablesMaps;
    private ItemStack display;
    private DefaultBuild defaultBuild;
    private ItemStack[] armor;
    private LinkedHashMap<Integer, BuildItem> items;
    private final Class<? extends GameTypeListener> listener;
    
    public GameType(final int id, final String name, final boolean ranked, final boolean buildable, final Class<? extends GameTypeListener> listener) {
        this.id = id;
        this.name = name;
        this.ranked = ranked;
        this.buildable = buildable;
        this.availablesMaps = new ArrayList<Map>();
        this.armor = new ItemStack[4];
        this.items = new LinkedHashMap<Integer, BuildItem>();
        this.listener = listener;
    }
    
    public Tuple[] getTop(final int number) {
        final Nest<?> nest = new Nest<Object>("game_stats");
        nest.setPool(RedisHelper.getPool());
        nest.cat("sorted_by_elo");
        nest.cat(this.id);
        final Set<Tuple> tuples = nest.zrevrangewith(0, 4);
        final Tuple[] scores = new Tuple[5];
        int i = 0;
        for (final Tuple tuple : tuples) {
            scores[i] = tuple;
            ++i;
        }
        return scores;
    }
    
    public void setDefaultBuild(final DefaultBuild defaultBuild) {
        this.defaultBuild = defaultBuild;
        for (final BuildItem item : defaultBuild.getItems()) {
            this.registerBuildItem(item);
        }
    }
    
    private void registerBuildItem(final BuildItem type) {
        if (type == null) {
            return;
        }
        this.items.put(type.getId(), type);
    }
    
    public BuildItem getItem(final int id) {
        return this.items.get(id);
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean isRanked() {
        return this.ranked;
    }
    
    public boolean isBuildable() {
        return this.buildable;
    }
    
    public List<Map> getAvailablesMaps() {
        return this.availablesMaps;
    }
    
    public ItemStack getDisplay() {
        return this.display;
    }
    
    public void setDisplay(final ItemStack display) {
        this.display = display;
    }
    
    public DefaultBuild getDefaultBuild() {
        return this.defaultBuild;
    }
    
    public ItemStack[] getArmor() {
        return this.armor;
    }
    
    public void setArmor(final ItemStack[] armor) {
        this.armor = armor;
    }
    
    public LinkedHashMap<Integer, BuildItem> getItems() {
        return this.items;
    }
    
    public Class<? extends GameTypeListener> getListener() {
        return this.listener;
    }
}
