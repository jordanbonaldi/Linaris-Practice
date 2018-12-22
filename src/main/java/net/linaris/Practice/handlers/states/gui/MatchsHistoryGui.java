package net.linaris.Practice.handlers.states.gui;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.linaris.Practice.handlers.games.GameType;
import net.linaris.Practice.handlers.games.GameTypesManager;
import net.linaris.Practice.handlers.players.OmegaPlayer;
import net.linaris.Practice.utils.NBTItem;
import net.linaris.Practice.utils.gui.GuiScreen;

public class MatchsHistoryGui extends GuiScreen
{
    public MatchsHistoryGui(final OmegaPlayer player, final GameType type) {
        super("Mes matchs", 5, player, false);
        this.build();
    }
    
    @Override
    public void drawScreen() {
    }
    
    @Override
    public void onOpen() {
    }
    
    @Override
    public void onClick(final ItemStack item, final InventoryClickEvent event) {
        event.setCancelled(true);
        if (this.getButtonId(item) == 0) {
            this.drawScreen();
            return;
        }
        if (this.getButtonId(item) == 1) {
            this.drawScreen();
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
        this.drawScreen();
    }
    
    @Override
    public void onClose() {
    }
}
