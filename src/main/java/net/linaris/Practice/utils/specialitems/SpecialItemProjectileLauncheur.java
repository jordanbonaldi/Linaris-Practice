package net.linaris.Practice.utils.specialitems;

import org.bukkit.entity.*;
import org.bukkit.event.entity.*;

public interface SpecialItemProjectileLauncheur
{
    public static final String flag = "SpecialItemProjectileLauncheurFlag";
    
    void projectileHitBlock(Player p0, ProjectileHitEvent p1);
    
    void projectileDamageEntity(Player p0, Projectile p1, EntityDamageByEntityEvent p2);
}
