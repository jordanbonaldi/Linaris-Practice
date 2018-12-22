package net.linaris.Practice.handlers.duels;

import java.util.*;

import net.linaris.Practice.handlers.games.*;
import net.linaris.Practice.handlers.players.*;
import net.md_5.bungee.api.chat.*;

public class DuelsManager
{
    private static HashMap<UUID, DuelRequest> request;
    
    public static void addRequest(final OmegaPlayer player, final OmegaPlayer target, final GameType gameType) {
        final DuelRequest backRequest = getRequest(player, target);
        if (backRequest != null) {
            if (!backRequest.isExpired()) {
                player.sendMessage("§aVous avez d\u00e9j\u00e0 d\u00e9fi\u00e9 ce joueur r\u00e9cemment, vous devez attendre avant de pouvoir recommencer");
                return;
            }
            removeRequest(backRequest.getId());
        }
        final DuelRequest duelRequest = new DuelRequest(player, target, gameType);
        DuelsManager.request.put(duelRequest.getId(), duelRequest);
        player.sendMessage("§aVous avez d\u00e9fi\u00e9 §b" + target.getName() + " §aen duel !");
        final TextComponent finalMessage = new TextComponent("§b" + player.getName() + " §avous d\u00e9fie en duel !");
        final TextComponent joinMessage = new TextComponent("§e§oClique ici pour accepter l'invitation !");
        joinMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duel accept " + duelRequest.getId()));
        target.sendMessage(finalMessage);
        target.sendMessage(joinMessage);
    }
    
    public static DuelRequest getRequest(final UUID uuid) {
        if (!DuelsManager.request.containsKey(uuid)) {
            return null;
        }
        return DuelsManager.request.get(uuid);
    }
    
    public static void removeRequest(final UUID uuid) {
        DuelsManager.request.remove(uuid);
    }
    
    public static DuelRequest getRequest(final OmegaPlayer player, final OmegaPlayer target) {
        return DuelsManager.request.values().stream().filter(d -> d.getPlayer().equals(player) && d.getTarget().equals(target)).findFirst().orElse(null);
    }
    
    static {
        DuelsManager.request = new HashMap<UUID, DuelRequest>();
    }
}
