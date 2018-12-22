package net.linaris.Practice.utils;

import org.bukkit.generator.*;
import java.util.*;
import org.bukkit.*;

public class VoidWorldGenerator extends ChunkGenerator
{
    public List<BlockPopulator> getDefaultPopulators(final World world) {
        return Arrays.asList(new BlockPopulator[0]);
    }
    
    public boolean canSpawn(final World world, final int x, final int z) {
        return true;
    }
    
    public byte[] generate(final World world, final Random rand, final int chunkx, final int chunkz) {
        return new byte[32768];
    }
    
    public Location getFixedSpawnLocation(final World world, final Random random) {
        return new Location(world, 0.0, 30.0, 0.0);
    }
}
