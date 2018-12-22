package net.linaris.Practice.handlers.players.utils;

import java.util.*;
import java.util.concurrent.*;

public class AttributeMap
{
    private Map<AttributeKey, Object> internal;
    
    public AttributeMap() {
        this.internal = new ConcurrentHashMap<AttributeKey, Object>();
    }
    
    public <T> T get(final AttributeKey<T> key) {
        return key.getType().cast(this.internal.get(key));
    }
    
    public <T> void set(final AttributeKey<T> key, final T attribute) {
        if (attribute == null) {
            this.internal.remove(key);
        }
        else {
            this.internal.put(key, attribute);
        }
    }
    
    public void clear() {
        this.internal.clear();
    }
}
