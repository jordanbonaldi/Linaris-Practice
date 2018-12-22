package net.linaris.Practice.handlers.games.types;

import org.bukkit.event.entity.*;

import net.linaris.Practice.handlers.games.*;
import net.linaris.Practice.handlers.matchs.*;

import org.bukkit.entity.*;
import org.bukkit.event.*;

public class BuildUHCListener extends GameTypeListener
{
    public BuildUHCListener(final Match match) {
        super(match);
    }
    
    @EventHandler
    public void onPlayerRegen(final EntityRegainHealthEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        final Player p = (Player)e.getEntity();
        if (this.match.isInGame(p.getName()) && e.getRegainReason() != EntityRegainHealthEvent.RegainReason.MAGIC_REGEN) {
            e.setCancelled(true);
        }
    }
}
