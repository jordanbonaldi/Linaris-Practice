package net.linaris.Practice.handlers.games;

import org.bukkit.*;

public class WrappedLocation
{
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    
    public Location getLocation(final World world) {
        return new Location(world, this.x, this.y, this.z, this.yaw, this.pitch);
    }
    
    public WrappedLocation getClone() {
        return new WrappedLocation(this.x, this.y, this.z, this.yaw, this.pitch);
    }
    
    public WrappedLocation getClone(final WrappedLocation loc) {
        return new WrappedLocation(this.x + loc.getX(), this.y + loc.getY(), this.z + loc.getZ(), this.yaw, this.pitch);
    }
    
    public WrappedLocation(final double x, final double y, final double z, final float yaw, final float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public float getPitch() {
        return this.pitch;
    }
}
