package net.linaris.Practice.handlers.maps;

import com.boydti.fawe.object.schematic.*;

import net.linaris.Practice.*;
import net.linaris.Practice.handlers.games.*;

import java.util.*;

import com.boydti.fawe.*;
import java.util.logging.*;
import java.io.*;

public class Map
{
    private final int id;
    private final String name;
    private final int sizeX;
    private final int sizeY;
    private final int sizeZ;
    private Schematic schematic;
    private List<WrappedLocation> spawns;
    
    public Map(final int id, final String name, final int sizeX, final int sizeY, final int sizeZ) {
        this.id = id;
        this.name = name;
        this.spawns = new ArrayList<WrappedLocation>();
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        try {
            this.schematic = FaweAPI.load(new File(Practice.getInstance().getDataFolder() + "/maps/" + id + ".schematic"));
        }
        catch (IOException e) {
            Practice.getInstance().getLogger().log(Level.SEVERE, "Can't load map with name " + name + " because he don't have shematic", e);
        }
    }
    
    protected void addSpawn(final WrappedLocation spawn) {
        this.spawns.add(spawn);
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getSizeX() {
        return this.sizeX;
    }
    
    public int getSizeY() {
        return this.sizeY;
    }
    
    public int getSizeZ() {
        return this.sizeZ;
    }
    
    public Schematic getSchematic() {
        return this.schematic;
    }
    
    public List<WrappedLocation> getSpawns() {
        return this.spawns;
    }
}
