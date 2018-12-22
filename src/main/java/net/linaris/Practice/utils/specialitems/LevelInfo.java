package net.linaris.Practice.utils.specialitems;

import java.util.*;

public class LevelInfo
{
    private List<String> lore;
    
    public LevelInfo(final List<String> lore) {
        this.lore = lore;
    }
    
    public List<String> getLore() {
        return this.lore;
    }
    
    public void setLore(final List<String> lore) {
        this.lore = lore;
    }
}
