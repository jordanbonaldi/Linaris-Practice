package net.linaris.Practice.handlers.matchs.gui;

import org.bukkit.*;

import java.util.*;
import java.io.*;
import org.bukkit.inventory.*;

import net.linaris.Practice.database.models.*;
import net.linaris.Practice.handlers.players.*;
import net.linaris.Practice.utils.*;
import net.linaris.Practice.utils.gui.*;

import org.bukkit.event.inventory.*;

public class PlayerSnapshotGui extends GuiScreen
{
    private PlayerSnapshot snapshot;
    
    public PlayerSnapshotGui(final OmegaPlayer player, final PlayerSnapshot snapshot) {
        super(PlayerUtils.getLastPlayerName(snapshot.getUserId()), 6, player, false);
        this.snapshot = snapshot;
        this.build();
    }
    
    @Override
    public void drawScreen() {
        try {
            int i = 0;
            for (final ItemStack item : InventoryStringDeSerializer.stacksFromBase64(this.snapshot.getInventory())) {
                this.setItem(item, i);
                ++i;
            }
            this.setItemLine(ItemStackUtils.create(Material.SPECKLED_MELON, 0, this.snapshot.getHealth(), "§cVie restante", null), 6, 4);
            this.setItemLine(ItemStackUtils.create(Material.COOKED_BEEF, 0, this.snapshot.getFood(), "§cNourriture restante", null), 6, 6);
        }
        catch (IOException e) {
            this.getPlayer().closeInventory();
            this.getPlayer().sendMessage("§cErreur lors du chargement de l'inventaire");
            e.printStackTrace();
        }
    }
    
    @Override
    public void onOpen() {
    }
    
    @Override
    public void onClick(final ItemStack item, final InventoryClickEvent event) {
        event.setCancelled(true);
    }
    
    @Override
    public void onClose() {
    }
}
