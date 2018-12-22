package net.linaris.Practice.database.redis.orm.collections;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.linaris.Practice.database.redis.orm.Indexed;
import net.linaris.Practice.database.redis.orm.JOhmUtils;
import net.linaris.Practice.database.redis.orm.Nest;
import net.linaris.Practice.database.redis.orm.RedisHelper;

public class RedisList<T> implements List<T>
{
    private final Nest<? extends T> nest;
    private final Class<? extends T> elementClazz;
    private final JOhmUtils.JOhmCollectionDataType johmElementType;
    private final Field field;
    private final Object owner;
    private final String[] ignoring;
    private final List<String> ignoredProperties;
    
    public RedisList(final Class<? extends T> clazz, final Nest<? extends T> nest, final Field field, final Object owner, final String... ignoring) {
        this.elementClazz = clazz;
        this.johmElementType = JOhmUtils.detectJOhmCollectionDataType(clazz);
        this.nest = nest;
        this.field = field;
        this.owner = owner;
        this.ignoring = ignoring;
        this.ignoredProperties = Arrays.asList(ignoring);
    }
    
    @Override
    public boolean add(final T e) {
        return this.internalAdd(e);
    }
    
    @Override
    public void add(final int index, final T element) {
        this.internalIndexedAdd(index, element);
    }
    
    @Override
    public boolean addAll(final Collection<? extends T> c) {
        boolean success = true;
        for (final T element : c) {
            success &= this.internalAdd(element);
        }
        return success;
    }
    
    @Override
    public boolean addAll(int index, final Collection<? extends T> c) {
        for (final T element : c) {
            this.internalIndexedAdd(index++, element);
        }
        return true;
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
    public T get(final int index) {
        T element = null;
        final String key = this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).lindex(index);
        if (!JOhmUtils.isNullOrEmpty(key)) {
            if (this.johmElementType == JOhmUtils.JOhmCollectionDataType.PRIMITIVE) {
                element = (T)JOhmUtils.Convertor.convert(this.field, this.elementClazz, key);
            }
            else if (this.johmElementType == JOhmUtils.JOhmCollectionDataType.MODEL) {
                element = RedisHelper.get(this.elementClazz, Integer.valueOf(key), this.ignoring);
            }
        }
        return element;
    }
    
    @Override
    public int indexOf(final Object o) {
        return this.scrollElements().indexOf(o);
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
    public int lastIndexOf(final Object o) {
        return this.scrollElements().lastIndexOf(o);
    }
    
    @Override
    public ListIterator<T> listIterator() {
        return this.scrollElements().listIterator();
    }
    
    @Override
    public ListIterator<T> listIterator(final int index) {
        return this.scrollElements().listIterator(index);
    }
    
    @Override
    public boolean remove(final Object o) {
        return this.internalRemove((T) o);
    }
    
    @Override
    public T remove(final int index) {
        return this.internalIndexedRemove(index);
    }
    
    @Override
    public boolean removeAll(final Collection<?> c) {
        boolean success = true;
        for (final Object element : c) {
            success &= this.internalRemove((T) element);
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
    public T set(final int index, final T element) {
        final T previousElement = this.get(index);
        this.internalIndexedAdd(index, element);
        return previousElement;
    }
    
    @Override
    public int size() {
        return (int)(Object)this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).llen();
    }
    
    @Override
    public List<T> subList(final int fromIndex, final int toIndex) {
        return this.scrollElements().subList(fromIndex, toIndex);
    }
    
    @Override
    public Object[] toArray() {
        return this.scrollElements().toArray();
    }
    
    @Override
    public <T> T[] toArray(final T[] a) {
        return this.scrollElements().toArray(a);
    }
    
    private boolean internalAdd(final T element) {
        boolean success = false;
        if (element != null) {
            if (this.johmElementType == JOhmUtils.JOhmCollectionDataType.PRIMITIVE) {
                success = (this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).rpush(element.toString()) > 0L);
            }
            else if (this.johmElementType == JOhmUtils.JOhmCollectionDataType.MODEL) {
                success = (this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).rpush(JOhmUtils.getId(element).toString()) > 0L);
            }
            this.indexValue(element);
        }
        return success;
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
    
    private void internalIndexedAdd(final int index, final T element) {
        if (element != null) {
            if (this.johmElementType == JOhmUtils.JOhmCollectionDataType.PRIMITIVE) {
                this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).lset(index, element.toString());
            }
            else if (this.johmElementType == JOhmUtils.JOhmCollectionDataType.MODEL) {
                this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).lset(index, JOhmUtils.getId(element).toString());
            }
            this.indexValue(element);
        }
    }
    
    private boolean internalRemove(final T element) {
        boolean success = false;
        if (element != null) {
            Long lrem = 0L;
            if (this.johmElementType == JOhmUtils.JOhmCollectionDataType.PRIMITIVE) {
                lrem = this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).lrem(1, element.toString());
            }
            else if (this.johmElementType == JOhmUtils.JOhmCollectionDataType.MODEL) {
                lrem = this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).lrem(1, JOhmUtils.getId(element).toString());
            }
            this.unindexValue(element);
            success = (lrem > 0L);
        }
        return success;
    }
    
    private T internalIndexedRemove(final int index) {
        final T element = this.get(index);
        this.internalRemove(element);
        return element;
    }
    
    private synchronized List<T> scrollElements() {
        final List<T> elements = new ArrayList<T>();
        final List<String> keys = this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).lrange(0, -1);
        for (final String key : keys) {
            if (this.ignoredProperties.contains(key)) {
                continue;
            }
            if (this.johmElementType == JOhmUtils.JOhmCollectionDataType.PRIMITIVE) {
                elements.add((T)JOhmUtils.Convertor.convert(this.field, this.elementClazz, key));
            }
            else {
                if (this.johmElementType != JOhmUtils.JOhmCollectionDataType.MODEL) {
                    continue;
                }
                elements.add(RedisHelper.get(this.elementClazz, Integer.valueOf(key), this.ignoring));
            }
        }
        return elements;
    }
}
