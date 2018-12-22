package net.linaris.Practice.database.redis.orm.collections;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import net.linaris.Practice.database.redis.orm.Indexed;
import net.linaris.Practice.database.redis.orm.JOhmException;
import net.linaris.Practice.database.redis.orm.JOhmUtils;
import net.linaris.Practice.database.redis.orm.Nest;
import net.linaris.Practice.database.redis.orm.RedisHelper;

public class RedisSortedSet<T> implements Set<T>
{
    private final Nest<? extends T> nest;
    private final Class<? extends T> clazz;
    private final Field field;
    private final Object owner;
    private final String byFieldName;
    
    public RedisSortedSet(final Class<? extends T> clazz, final String byField, final Nest<? extends T> nest, final Field field, final Object owner) {
        this.clazz = clazz;
        this.nest = nest;
        this.field = field;
        this.owner = owner;
        this.byFieldName = byField;
    }
    
    private synchronized Set<T> scrollElements() {
        final Set<String> ids = this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).zrange(0, -1);
        final Set<T> elements = new LinkedHashSet<T>();
        for (final String id : ids) {
            elements.add(RedisHelper.get(this.clazz, Integer.valueOf(id), new String[0]));
        }
        return elements;
    }
    
    private void indexValue(final T element) {
        if (this.field.isAnnotationPresent(Indexed.class)) {
            this.nest.cat(this.field.getName()).cat(JOhmUtils.getId(element)).sadd(JOhmUtils.getId(this.owner).toString());
        }
    }
    
    private void unindexValue(final T element) {
        if (this.field.isAnnotationPresent(Indexed.class)) {
            this.nest.cat(this.field.getName()).cat(JOhmUtils.getId(element)).srem(JOhmUtils.getId(this.owner).toString());
        }
    }
    
    private boolean internalAdd(final T element) {
        boolean success = false;
        if (element != null) {
            try {
                final Field byField = element.getClass().getDeclaredField(this.byFieldName);
                byField.setAccessible(true);
                Object fieldValue = byField.get(element);
                if (fieldValue == null) {
                    fieldValue = 0.0f;
                }
                success = (this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).zadd(Float.class.cast(fieldValue), JOhmUtils.getId(element).toString()) > 0L);
                this.indexValue(element);
            }
            catch (SecurityException e) {
                throw new JOhmException(e);
            }
            catch (IllegalArgumentException e2) {
                throw new JOhmException(e2);
            }
            catch (IllegalAccessException e3) {
                throw new JOhmException(e3);
            }
            catch (NoSuchFieldException e4) {
                throw new JOhmException(e4);
            }
        }
        return success;
    }
    
    private boolean internalRemove(final T element) {
        boolean success = false;
        if (element != null) {
            success = (this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).srem(JOhmUtils.getId(element).toString()) > 0L);
            this.unindexValue(element);
        }
        return success;
    }
    
    @Override
    public boolean add(final T e) {
        return this.internalAdd(e);
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
    public void clear() {
        this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).del();
    }
    
    @Override
    public boolean contains(final Object o) {
        return this.scrollElements().contains(o);
    }
    
    @Override
    public boolean containsAll(final Collection<?> c) {
        return this.scrollElements().containsAll(c);
    }
    
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public Iterator<T> iterator() {
        return this.scrollElements().iterator();
    }
    
    @Override
    public boolean remove(final Object o) {
        return this.internalRemove((T) o);
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
    public int size() {
        return (int)(Object)this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).zcard();
    }
    
    @Override
    public Object[] toArray() {
        return this.scrollElements().toArray();
    }
    
    @Override
    public <T> T[] toArray(final T[] a) {
        return this.scrollElements().toArray(a);
    }
}
