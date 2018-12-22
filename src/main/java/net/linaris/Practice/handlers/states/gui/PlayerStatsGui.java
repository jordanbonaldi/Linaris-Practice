package net.linaris.Practice.handlers.states.gui;

import org.bukkit.inventory.*;
import org.bukkit.*;

import redis.clients.jedis.*;
import org.bukkit.inventory.meta.*;

import net.linaris.Practice.database.models.*;
import net.linaris.Practice.database.redis.orm.*;
import net.linaris.Practice.handlers.games.*;
import net.linaris.Practice.handlers.players.*;
import net.linaris.Practice.utils.*;
import net.linaris.Practice.utils.gui.*;

import java.util.*;
import org.bukkit.event.inventory.*;

public class PlayerStatsGui extends GuiScreen
{
    private ItemStack playerHead;
    private GameType gameType;
    private boolean first;
    
    public PlayerStatsGui(final OmegaPlayer player) {
        super("Statistiques", 5, player, false);
        this.playerHead = this.buildPlayerItem();
        this.first = true;
        this.build();
    }
    
    @Override
    public void drawScreen() {
        if (this.first) {
            this.setItemLine(this.playerHead, 4, 5);
            this.first = false;
        }
        else {
            for (int i = 10; i < 17; ++i) {
                this.setItem(null, i);
            }
        }
        if (this.gameType == null) {
            int i = 10;
            for (final GameType type : GameTypesManager.getTypes().values()) {
                this.setItem(this.buildGameTypeItem(type), i);
                ++i;
            }
        }
        else {
            final StatsModel model = this.getPlayer().getStats(this.gameType.getId());
            this.setItemLine(ItemStackUtils.create(Material.IRON_SWORD, 0, 1, 
            		"§aStatistiques d'Entrainement", Arrays.asList("", "§3Victoires: §e" + model.getWin(), 
            				"§3Défaites: §e" + model.getLose(), "§3Matchs joués: §e" + model.getPlayed())), 2, 2);
            this.setItemLine(ItemStackUtils.create(Material.GOLD_SWORD, 0, 1, "§cStatistiques Classé", Arrays.asList("", "§3Elo: §e" + model.getElo(), "§3Victoires: §e" + model.getWinRanked(), "§3Défaites: §e" + model.getLoseRanked(), "§3Matchs joués: §e" + model.getPlayedRanked())), 2, 4);
            this.setItemLine(ItemStackUtils.create(386, 0, 1, "§9Mes matchs", Arrays.asList("", "§cBientôt", "", "§e§oClique pour voir tes matchs")), 2, 6, 1);
            this.setItemLine(ItemStackUtils.create(Material.ARROW, 0, 1, "§7Retour", null), 2, 8, 0);
        }
    }
    
    public ItemStack buildGameTypeItem(final GameType type) {
        final ItemStack item = type.getDisplay().clone();
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§a" + type.getName());
        final List<String> lore = new ArrayList<String>();
        lore.add("");
        final Tuple[] tuples = type.getTop(5);
        for (int i = 0; i < 5; ++i) {
            final Tuple tuple = tuples[i];
            if (tuple == null) {
                lore.add("§6" + (i + 1) + ". §cAucun joueur");
            }
            else {
                lore.add("§6" + (i + 1) + ". §b" + PlayerUtils.getLastPlayerName(Long.parseLong(tuple.getElement())) + " §3" + (int)tuple.getScore() + " Elo");
            }
        }
        lore.add("");
        lore.add("§e§oClique pour voir les statistiques");
        meta.setLore((List<String>)lore);
        item.setItemMeta(meta);
        return new NBTItem(item).setString("gametype_id", type.getName()).getItem();
    }
    
    public ItemStack buildPlayerItem() {
        final ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        final SkullMeta meta = (SkullMeta)item.getItemMeta();
        meta.setOwner(this.getPlayer().getName());
        meta.setDisplayName("§a" + this.getPlayer().getDisplayName());
        final List<String> lore = new ArrayList<String>();
        lore.add("");
        lore.add("§3Temps de jeu: §e" + TimeUtils.getTime(this.getPlayer().getData().getTimePlayed()));
        if (this.getPlayer().getData().getRank().getVipLevel() >= 1) {
            lore.add("§3Match class\u00e9 restant: §eAucune limite");
        }
        else {
            lore.add("§3Match class\u00e9 restant: §e" + this.getPlayer().getData().getRankedRemaining());
        }
        lore.add("");
        final Tuple[] tuples = this.getTopTime(5);
        for (int i = 0; i < 5; ++i) {
            final Tuple tuple = tuples[i];
            if (tuple == null) {
                lore.add("§6" + (i + 1) + ". §cAucun joueur");
            }
            else {
                final Long timePlayed = (long)(tuple.getScore() * 1000.0);
                lore.add("§6" + (i + 1) + ". §b" + PlayerUtils.getLastPlayerName(Long.parseLong(tuple.getElement())) + " §3" + TimeUtils.getTime(timePlayed));
            }
        }
        meta.setLore((List<String>)lore);
        item.setItemMeta((ItemMeta)meta);
        return item;
    }
    
    public Tuple[] getTopTime(final int number) {
        final Nest<?> nest = new Nest<Object>("users");
        nest.setPool(RedisHelper.getPool());
        nest.cat("sorted_by_timeplayed");
        final Set<Tuple> tuples = nest.zrevrangewith(0, 4);
        final Tuple[] scores = new Tuple[5];
        int i = 0;
        for (final Tuple tuple : tuples) {
            scores[i] = tuple;
            ++i;
        }
        return scores;
    }
    
    @Override
    public void onOpen() {
    }
    
    @Override
    public void onClick(final ItemStack item, final InventoryClickEvent event) {
        event.setCancelled(true);
        if (this.getButtonId(item) == 0) {
            this.gameType = null;
            this.drawScreen();
            return;
        }
        if (this.getButtonId(item) == 1) {
            this.getPlayer().sendMessage("§cBient\u00f4t");
            return;
        }
        if (item == null || item.getType() == Material.AIR) {
            return;
        }
        final NBTItem nbt = new NBTItem(item);
        if (!nbt.hasKey("gametype_id")) {
            return;
        }
        final GameType type = GameTypesManager.getTypes().get(nbt.getString("gametype_id"));
        if (type == null) {
            return;
        }
        this.gameType = type;
        this.drawScreen();
    }
    
    @Override
    public void onClose() {
    }
}
