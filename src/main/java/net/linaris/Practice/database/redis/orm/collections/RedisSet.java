package net.linaris.Practice.database.redis.orm.collections;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.linaris.Practice.database.redis.orm.Indexed;
import net.linaris.Practice.database.redis.orm.JOhmUtils;
import net.linaris.Practice.database.redis.orm.Nest;
import net.linaris.Practice.database.redis.orm.RedisHelper;

public class RedisSet<T> implements Set<T>
{
    private final Nest<? extends T> nest;
    private final Class<? extends T> elementClazz;
    private final JOhmUtils.JOhmCollectionDataType johmElementType;
    private final Object owner;
    private final Field field;
    
    public RedisSet(final Class<? extends T> clazz, final Nest<? extends T> nest, final Field field, final Object owner) {
        this.elementClazz = clazz;
        this.johmElementType = JOhmUtils.detectJOhmCollectionDataType(clazz);
        this.nest = nest;
        this.field = field;
        this.owner = owner;
    }
    
    private void indexValue(final T element) {
        if (this.field.isAnnotationPresent(Indexed.class)) {
            if (this.johmElementType == JOhmUtils.JOhmCollectionDataType.PRIMITIVE) {
                this.nest.cat(this.field.getName()).cat(element.toString()).sadd(JOhmUtils.getId(this.owner).toString());
            }
            else if (this.johmElementType == JOhmUtils.JOhmCollectionDataType.MODEL) {
                this.nest.cat(this.field.getName()).cat(JOhmUtils.getId(element)).sadd(JOhmUtils.getId(this.owner).toString());
            }
        }
    }
    
    private void unindexValue(final T element) {
        if (this.field.isAnnotationPresent(Indexed.class)) {
            if (this.johmElementType == JOhmUtils.JOhmCollectionDataType.PRIMITIVE) {
                this.nest.cat(this.field.getName()).cat(element.toString()).srem(JOhmUtils.getId(this.owner).toString());
            }
            else if (this.johmElementType == JOhmUtils.JOhmCollectionDataType.MODEL) {
                this.nest.cat(this.field.getName()).cat(JOhmUtils.getId(element)).srem(JOhmUtils.getId(this.owner).toString());
            }
        }
    }
    
    @Override
    public int size() {
        return this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).smembers().size();
    }
    
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public boolean contains(final Object o) {
        return this.scrollElements().contains(o);
    }
    
    @Override
    public Iterator<T> iterator() {
        return this.scrollElements().iterator();
    }
    
    @Override
    public Object[] toArray() {
        return this.scrollElements().toArray();
    }
    
    @Override
    public <T> T[] toArray(final T[] a) {
        return this.scrollElements().toArray(a);
    }
    
    @Override
    public boolean add(final T element) {
        return this.internalAdd(element);
    }
    
    @Override
    public boolean remove(final Object o) {
        return this.internalRemove((T) o);
    }
    
    @Override
    public boolean containsAll(final Collection<?> c) {
        return this.scrollElements().containsAll(c);
    }
    
    @Override
    public boolean addAll(final Collection<? extends T> collection) {
        boolean success = true;
        for (final T element : collection) {
            success &= this.internalAdd(element);
        }
        return success;
    }
    
    @Override
    public boolean retainAll(final Collection<?> c) {
        this.clear();
        final Iterator<?> iterator = c.iterator();
        boolean success = true;
        while (iterator.hasNext()) {
            final T element = (T)iterator.next();
            success &= this.internalAdd(element);
        }
        return success;
    }
    
    @Override
    public boolean removeAll(final Collection<?> c) {
        final Iterator<?> iterator = c.iterator();
        boolean success = true;
        while (iterator.hasNext()) {
            final T element = (T)iterator.next();
            success &= this.internalRemove(element);
        }
        return success;
    }
    
    @Override
    public void clear() {
        this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).del();
    }
    
    private boolean internalAdd(final T element) {
        boolean success = false;
        if (element != null) {
            if (this.johmElementType == JOhmUtils.JOhmCollectionDataType.PRIMITIVE) {
                success = (this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).sadd(element.toString()) > 0L);
            }
            else if (this.johmElementType == JOhmUtils.JOhmCollectionDataType.MODEL) {
                success = (this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).sadd(JOhmUtils.getId(element).toString()) > 0L);
            }
            this.indexValue(element);
        }
        return success;
    }
    
    private boolean internalRemove(final T element) {
        boolean success = false;
        if (element != null) {
            if (this.johmElementType == JOhmUtils.JOhmCollectionDataType.PRIMITIVE) {
                success = (this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).srem(element.toString()) > 0L);
            }
            else if (this.johmElementType == JOhmUtils.JOhmCollectionDataType.MODEL) {
                success = (this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).srem(JOhmUtils.getId(element).toString()) > 0L);
            }
            this.unindexValue(element);
        }
        return success;
    }
    
    private synchronized Set<T> scrollElements() {
        final Set<String> keys = this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).smembers();
        final Set<T> elements = new HashSet<T>();
        for (final String key : keys) {
            if (this.johmElementType == JOhmUtils.JOhmCollectionDataType.PRIMITIVE) {
                elements.add((T)JOhmUtils.Convertor.convert(this.field, this.elementClazz, key));
            }
            else {
                if (this.johmElementType != JOhmUtils.JOhmCollectionDataType.MODEL) {
                    continue;
                }
                elements.add(RedisHelper.get(this.elementClazz, Integer.valueOf(key), new String[0]));
            }
        }
        return elements;
    }
}
