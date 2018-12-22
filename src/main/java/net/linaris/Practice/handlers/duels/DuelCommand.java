package net.linaris.Practice.handlers.duels;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import net.linaris.Practice.Practice;
import net.linaris.Practice.handlers.maps.*;
import net.linaris.Practice.handlers.matchs.*;
import net.linaris.Practice.handlers.players.*;
import net.linaris.Practice.utils.gui.*;

import java.util.*;

public class DuelCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        final Player p = (Player)sender;
        final OmegaPlayer player = OmegaPlayerManager.get().getPlayer(p);
        if (!player.isAvailable()) {
            p.sendMessage("§cVous ne pouvez pas utiliser cette commande pour le moment (secret story)");
        }
        if (args.length == 1) {
            final OmegaPlayer target = OmegaPlayerManager.get().getPlayer(args[0]);
            if (target == null) {
                p.sendMessage("§cCe joueur n'est pas en ligne");
                return true;
            }
            if (target.equals(player)) {
                p.sendMessage("§cCe joueur n'est pas en ligne");
                return true;
            }
            GuiManager.openGui(new DuelGui(player, target));
        }
        else if (args.length == 2 && args[0].equals("accept")) {
            final String id = args[1];
            try {
                final UUID uuid = UUID.fromString(id);
                final DuelRequest request = DuelsManager.getRequest(uuid);
                if (request == null) {
                    p.sendMessage("§cDemande introuvable");
                    return true;
                }
                if (!request.getTarget().equals(player)) {
                    p.sendMessage("§cDemande introuvable");
                    return true;
                }
                DuelsManager.removeRequest(uuid);
                if (!request.isAvailable()) {
                    p.sendMessage("§cCette demande n'est plus valide");
                    return true;
                }
                if (request.isExpired()) {
                    p.sendMessage("§cCette demande a expir\u00e9");
                    return true;
                }
                final SoloMatch match = MatchsManager.createSoloMatch(Practice.choose_map(request.getGameType().getName()), 
                		request.getGameType(), false, request.getTarget(), request.getPlayer());
                match.start();
            }
            catch (Exception e) {
                p.sendMessage("§cDemande introuvable");
            }
        }
        else {
            p.sendMessage("§cFaites /duel [joueur] pour d\u00e9fier une personne en duel !");
        }
        return true;
    }
}
