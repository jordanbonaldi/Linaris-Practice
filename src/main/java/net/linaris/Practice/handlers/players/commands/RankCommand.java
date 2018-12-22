package net.linaris.Practice.handlers.players.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.linaris.Practice.handlers.players.OmegaPlayer;
import net.linaris.Practice.handlers.players.OmegaPlayerManager;
import net.linaris.Practice.handlers.players.Rank;

public class RankCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage("§cVous ne pouvez pas executer cette commande");
            return true;
        }
        if (args.length != 2) {
            sender.sendMessage("§c/rank [joueur] [rang]");
            return true;
        }
        final OmegaPlayer target = OmegaPlayerManager.get().getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cCe joueur n'est pas en ligne");
            return true;
        }
        final Rank rank = Rank.get(args[1]);
        if (rank == null) {
            sender.sendMessage("§cCe rang n'existe pas");
            return true;
        }
        target.setRank(rank);
        sender.sendMessage("§cIt's ok !");
        return true;
    }
}
