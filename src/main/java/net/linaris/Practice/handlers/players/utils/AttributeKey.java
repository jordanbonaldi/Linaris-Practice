package net.linaris.Practice.handlers.players.utils;

import java.util.concurrent.atomic.*;
import com.google.common.base.*;

public final class AttributeKey<T>
{
    private static AtomicInteger nextId;
    private final int id;
    private final String name;
    private final Class<T> type;
    
    private AttributeKey(final Class<T> type, final String name) {
        Preconditions.checkNotNull((Object)type, (Object)"The class can't be null");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(name), (Object)"The name can't be null or empty");
        this.type = type;
        this.name = name;
        this.id = AttributeKey.nextId.getAndIncrement();
    }
    
    Class<T> getType() {
        return this.type;
    }
    
    public static <T> AttributeKey<T> valueOf(final Class<T> type, final String name) {
        return new AttributeKey<T>(type, name);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        final AttributeKey that = (AttributeKey)obj;
        return this.id == that.id && this.name.equals(that.name);
    }
    
    @Override
    public int hashCode() {
        int result = this.id;
        result = 31 * result + this.name.hashCode();
        return result;
    }
    
    static {
        AttributeKey.nextId = new AtomicInteger(0);
    }
}
