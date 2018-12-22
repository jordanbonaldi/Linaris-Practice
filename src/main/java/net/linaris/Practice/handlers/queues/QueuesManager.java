package net.linaris.Practice.handlers.queues;

import java.util.*;
import java.util.stream.*;

import net.linaris.Practice.*;
import net.linaris.Practice.handlers.games.*;
import net.linaris.Practice.handlers.maps.*;
import net.linaris.Practice.handlers.matchs.*;
import net.linaris.Practice.handlers.players.*;
import net.linaris.Practice.utils.*;

import java.util.function.*;

public class QueuesManager
{
    private static final Practice api;
    private static final LinkedHashMap<UUID, Queue> queues;
    
    public static void init() {
        TaskManager.scheduleSyncRepeatingTask("Queues", () -> {
            final ArrayList<UUID> removed;
            final Iterator<Queue> iterator;
            Queue queue;
            Queue finded;
            SoloMatch match;
            removed = new ArrayList<UUID>();
            iterator = new ArrayList<Queue>(QueuesManager.queues.values()).iterator();
            while (iterator.hasNext()) {
                queue = iterator.next();
                if (!queue.updateAvailable()) {
                    removed.add(queue.getUUID());
                }
                else if (!queue.isRanked()) {
                    continue;
                }
                else {
                    finded = findQueue(queue);
                    if (finded != null) {
                        removed.add(queue.getUUID());
                        removed.add(finded.getUUID());
                        match = MatchsManager.createSoloMatch(MapsManager.Maps.MAP_BUILDUHC, queue.getType(), queue.isRanked(), queue.getPlayer(), finded.getPlayer());
                        match.sendMessage("§cAdversaire trouv\u00e9 !");
                        match.sendMessage("§cPr\u00e9paration du match...");
                        match.start();
                    }
                    else {
                        queue.getPlayer().sendActionBar("§cEn attente: -" + queue.getMultiplier() + " " + queue.getMultiplier());
                    }
                }
            }
            removed.stream().forEach(uuid -> QueuesManager.queues.remove(uuid));
        }, 20, 20);
    }
    
    public static void addInQueue(final OmegaPlayer player, final GameType type, final boolean ranked) {
        final Queue queue = new Queue(player, type, ranked, ranked ? player.getStats(type.getId()).getElo() : 0);
        QueuesManager.queues.put(player.getUuid(), queue);
        player.sendMessage("§cVous entrez dans la file d'attente");
        tryFind(queue);
    }
    
    private static boolean tryFind(final Queue queue) {
        final Queue finded = findQueue(queue);
        if (finded != null) {
            QueuesManager.queues.remove(queue.getUUID());
            QueuesManager.queues.remove(finded.getUUID());
            final SoloMatch match = MatchsManager.createSoloMatch(Practice.choose_map(queue.getType().getName()), queue.getType(), queue.isRanked(), queue.getPlayer(), finded.getPlayer());
            match.sendMessage("§cAdversaire trouv\u00e9 !");
            match.sendMessage("§cPr\u00e9paration du match...");
            match.start();
            return true;
        }
        return false;
    }
    
    private static Queue findQueue(final Queue queue) {
        final List<Queue> available = getQueueOf(queue.getType(), queue.isRanked()).stream().filter(q -> !q.equals(queue) && q.inInterval(queue)).collect(Collectors.toList());
        if (available.isEmpty()) {
            return null;
        }
        return available.stream().findFirst().orElseGet(null);
    }
    
    public static List<Queue> getQueueOf(final GameType type) {
        removeOutdated();
        return QueuesManager.queues.values().stream().filter(queue -> queue.getType().equals(type) && queue.updateAvailable()).collect(Collectors.toList());
    }
    
    public static List<Queue> getQueueOf(final GameType type, final boolean ranked) {
        removeOutdated();
        return QueuesManager.queues.values().stream().filter(queue -> queue.getType().equals(type) && queue.isRanked() == ranked && queue.updateAvailable()).collect(Collectors.toList());
    }
    
    public static boolean inQueue(final OmegaPlayer player) {
        return inQueue(player.getUuid());
    }
    
    public static boolean inQueue(final UUID uuid) {
        return QueuesManager.queues.containsKey(uuid);
    }
    
    public static void removeQueue(final UUID uuid) {
        QueuesManager.queues.remove(uuid);
    }
    
    private static void removeOutdated() {
        for (final Queue queue : new ArrayList<Queue>(QueuesManager.queues.values())) {
            if (!queue.updateAvailable()) {
                QueuesManager.queues.remove(queue.getUUID());
            }
        }
    }
    
    static {
        api = Practice.getInstance();
        queues = new LinkedHashMap<UUID, Queue>();
    }
}
