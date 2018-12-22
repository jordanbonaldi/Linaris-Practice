package net.linaris.Practice.handlers.states.states;

import net.linaris.Practice.handlers.players.*;
import net.linaris.Practice.handlers.states.*;
import net.linaris.Practice.handlers.states.items.lobby.*;
import net.linaris.Practice.handlers.states.items.moderator.*;
import net.linaris.Practice.utils.*;

public class LobbyState extends State
{
    private static final UnrankedItem unranked_item;
    private static final RankedItem ranked_item;
    private static final ModeratorItem moderator_item;
    private static final EditKitsItem editkits_item;
    private static final ShopItem shopitem;
    private static final StatsItem stats_item;
    
    public LobbyState(final OmegaPlayer player) {
        super("Lobby", player);
    }
    
    @Override
    public void onEnter() {
        if (!this.getPlayer().isDead()) {
            this.getPlayer().reset();
            if (this.getPlayer().getData().getRank().getVipLevel() >= 1) {
                this.getPlayer().setAllowFlight(true);
                this.getPlayer().setFlying(true);
            }
            this.getPlayer().getInventory().setItem(0, LobbyState.unranked_item.getStaticItem());
            this.getPlayer().getInventory().setItem(1, LobbyState.ranked_item.getStaticItem());
            if (this.getPlayer().getData().getRank().getModerationLevel() >= 1)
                this.getPlayer().getInventory().setItem(5, LobbyState.moderator_item.getStaticItem());
            this.getPlayer().getInventory().setItem(4, LobbyState.shopitem.getStaticItem());
            this.getPlayer().getInventory().setItem(7, LobbyState.editkits_item.getStaticItem());
            this.getPlayer().getInventory().setItem(8, LobbyState.stats_item.getStaticItem());
        }
        else {
            this.getPlayer().reset();
            TaskManager.runTaskLater(() -> {
                if (this.getPlayer().getData().getRank().getVipLevel() >= 1) {
                    this.getPlayer().setAllowFlight(true);
                    this.getPlayer().setFlying(true);
                }
                this.getPlayer().getInventory().setItem(0, LobbyState.unranked_item.getStaticItem());
                this.getPlayer().getInventory().setItem(1, LobbyState.ranked_item.getStaticItem());
                if (this.getPlayer().getData().getRank().getModerationLevel() >= 1)
                    this.getPlayer().getInventory().setItem(5, LobbyState.moderator_item.getStaticItem());
                this.getPlayer().getInventory().setItem(4, LobbyState.shopitem.getStaticItem());
                this.getPlayer().getInventory().setItem(7, LobbyState.editkits_item.getStaticItem());
                this.getPlayer().getInventory().setItem(8, LobbyState.stats_item.getStaticItem());
            }, 2);
        }
    }
    
    @Override
    public void onExit() {
    }
    
    static {
        unranked_item = new UnrankedItem();
        shopitem = new ShopItem();
        ranked_item = new RankedItem();
        moderator_item = new ModeratorItem();
        editkits_item = new EditKitsItem();
        stats_item = new StatsItem();
    }
}
