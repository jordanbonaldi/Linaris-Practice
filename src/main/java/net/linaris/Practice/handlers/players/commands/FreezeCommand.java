package net.linaris.Practice.handlers.players.commands;

import org.bukkit.command.*;
import org.bukkit.entity.*;

import net.linaris.Practice.handlers.players.*;
import net.linaris.Practice.handlers.states.states.*;

public class FreezeCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        final OmegaPlayer pl = OmegaPlayerManager.get().getPlayer((Player)sender);
        if (!(pl.getState() instanceof ModeratorState)) {
            sender.sendMessage("§cVous ne pouvez pas executer cette commande");
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage("§c/freeze [joueur]");
            return true;
        }
        final OmegaPlayer target = OmegaPlayerManager.get().getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cCe joueur n'est pas en ligne");
            return true;
        }
        target.setFreeze(!target.isFreeze());
        return true;
    }
}
