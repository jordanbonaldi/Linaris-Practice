package net.linaris.Practice.utils;

import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.util.*;

public class VelocityUtils
{
    public static Vector getBumpVector(final Entity entity, final Location from, final double power) {
        final Vector bump = entity.getLocation().toVector().subtract(from.toVector()).normalize();
        bump.multiply(power);
        return bump;
    }
    
    public static Vector getPullVector(final Entity entity, final Location to, final double power) {
        final Vector pull = to.toVector().subtract(entity.getLocation().toVector()).normalize();
        pull.multiply(power);
        return pull;
    }
    
    public static void bumpEntity(final Entity entity, final Location from, final double power) {
        entity.setVelocity(getBumpVector(entity, from, power));
    }
    
    public static void bumpEntity(final Entity entity, final Location from, final double power, final double fixedY) {
        final Vector vector = getBumpVector(entity, from, power);
        vector.setY(fixedY);
        entity.setVelocity(vector);
    }
    
    public static void pullEntity(final Entity entity, final Location to, final double power) {
        entity.setVelocity(getPullVector(entity, to, power));
    }
    
    public static void pullEntity(final Entity entity, final Location from, final double power, final double fixedY) {
        final Vector vector = getPullVector(entity, from, power);
        vector.setY(fixedY);
        entity.setVelocity(vector);
    }
}
