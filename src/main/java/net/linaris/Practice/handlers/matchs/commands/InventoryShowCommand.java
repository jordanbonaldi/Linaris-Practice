package net.linaris.Practice.handlers.matchs.commands;

import org.bukkit.command.*;
import org.bukkit.entity.*;

import net.linaris.Practice.database.models.*;
import net.linaris.Practice.handlers.matchs.*;
import net.linaris.Practice.handlers.matchs.gui.*;
import net.linaris.Practice.handlers.players.*;
import net.linaris.Practice.utils.gui.*;

import java.util.*;

public class InventoryShowCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        final Player p = (Player)sender;
        if (args.length != 2) {
            return true;
        }
        final String matchId = args[0];
        final String userId = args[1];
        try {
            final MatchModel model = MatchsManager.getModel(UUID.fromString(matchId));
            final PlayerSnapshot snapshot = model.getPlayers().get(Long.parseLong(userId));
            GuiManager.openGui(new PlayerSnapshotGui(OmegaPlayerManager.get().getPlayer(p), snapshot));
        }
        catch (Exception e) {
            p.sendMessage("§cErreur lors du chargement du match");
            e.printStackTrace();
        }
        return false;
    }
}
