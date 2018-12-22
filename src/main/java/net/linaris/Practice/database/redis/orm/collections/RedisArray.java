package net.linaris.Practice.database.redis.orm.collections;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import net.linaris.Practice.database.redis.orm.Indexed;
import net.linaris.Practice.database.redis.orm.JOhmUtils;
import net.linaris.Practice.database.redis.orm.Nest;
import net.linaris.Practice.database.redis.orm.RedisHelper;

public class RedisArray<T>
{
    private final int length;
    private final Class<? extends T> elementClazz;
    private final JOhmUtils.JOhmCollectionDataType johmElementType;
    private final Nest<? extends T> nest;
    private final Field field;
    private final Object owner;
    private boolean isIndexed;
    
    public RedisArray(final int length, final Class<? extends T> clazz, final Nest<? extends T> nest, final Field field, final Object owner) {
        this.length = length;
        this.elementClazz = clazz;
        this.johmElementType = JOhmUtils.detectJOhmCollectionDataType(clazz);
        this.nest = nest;
        this.field = field;
        this.isIndexed = field.isAnnotationPresent(Indexed.class);
        this.owner = owner;
    }
    
    public T[] read() {
        final T[] streamed = (T[])Array.newInstance(this.elementClazz, this.length);
        for (int iter = 0; iter < this.length; ++iter) {
            streamed[iter] = (T)this.elementClazz.cast(this.get(iter));
        }
        return streamed;
    }
    
    public void write(final T[] backingArray) {
        if (backingArray == null || backingArray.length == 0) {
            this.clear();
        }
        else {
            this.clear();
            for (int iter = 0; iter < backingArray.length; ++iter) {
                this.delete(iter);
                this.save(backingArray[iter]);
            }
        }
    }
    
    public Long clear() {
        return this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).del();
    }
    
    private boolean save(final T element) {
        boolean success = false;
        if (element != null) {
            if (this.johmElementType == JOhmUtils.JOhmCollectionDataType.PRIMITIVE) {
                success = (this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).rpush(element.toString()) > 0L);
            }
            else if (this.johmElementType == JOhmUtils.JOhmCollectionDataType.MODEL) {
                success = (this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).rpush(JOhmUtils.getId(element).toString()) > 0L);
            }
            if (this.isIndexed) {
                this.indexValue(element);
            }
        }
        return success;
    }
    
    private void delete(final int index) {
        final T element = this.get(index);
        this.internalDelete(element);
    }
    
    private T get(final int index) {
        T element = null;
        final String key = this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).lindex(index);
        if (!JOhmUtils.isNullOrEmpty(key)) {
            if (this.johmElementType == JOhmUtils.JOhmCollectionDataType.PRIMITIVE) {
                element = (T)JOhmUtils.Convertor.convert(this.field, this.elementClazz, key);
            }
            else if (this.johmElementType == JOhmUtils.JOhmCollectionDataType.MODEL) {
                element = RedisHelper.get(this.elementClazz, Integer.valueOf(key), new String[0]);
            }
        }
        return element;
    }
    
    private void indexValue(final T element) {
        if (this.johmElementType == JOhmUtils.JOhmCollectionDataType.PRIMITIVE) {
            this.nest.cat(this.field.getName()).cat(element.toString()).sadd(JOhmUtils.getId(this.owner).toString());
        }
        else if (this.johmElementType == JOhmUtils.JOhmCollectionDataType.MODEL) {
            this.nest.cat(this.field.getName()).cat(JOhmUtils.getId(element)).sadd(JOhmUtils.getId(this.owner).toString());
        }
    }
    
    private void unindexValue(final T element) {
        if (this.johmElementType == JOhmUtils.JOhmCollectionDataType.PRIMITIVE) {
            this.nest.cat(this.field.getName()).cat(element.toString()).srem(JOhmUtils.getId(this.owner).toString());
        }
        else if (this.johmElementType == JOhmUtils.JOhmCollectionDataType.MODEL) {
            this.nest.cat(this.field.getName()).cat(JOhmUtils.getId(element)).srem(JOhmUtils.getId(this.owner).toString());
        }
    }
    
    private boolean internalDelete(final T element) {
        if (element == null) {
            return false;
        }
        Long lrem = 0L;
        if (this.johmElementType == JOhmUtils.JOhmCollectionDataType.PRIMITIVE) {
            lrem = this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).lrem(1, element.toString());
        }
        else if (this.johmElementType == JOhmUtils.JOhmCollectionDataType.MODEL) {
            lrem = this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).lrem(1, JOhmUtils.getId(element).toString());
        }
        if (this.isIndexed) {
            this.unindexValue(element);
        }
        return lrem > 0L;
    }
}
