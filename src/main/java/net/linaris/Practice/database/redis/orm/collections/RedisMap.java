package net.linaris.Practice.database.redis.orm.collections;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import net.linaris.Practice.database.redis.orm.Indexed;
import net.linaris.Practice.database.redis.orm.JOhmUtils;
import net.linaris.Practice.database.redis.orm.Nest;
import net.linaris.Practice.database.redis.orm.RedisHelper;

public class RedisMap<K, V> implements Map<K, V>
{
    private final Nest<? extends V> nest;
    private final Class<? extends K> keyClazz;
    private final Class<? extends V> valueClazz;
    private final JOhmUtils.JOhmCollectionDataType johmKeyType;
    private final JOhmUtils.JOhmCollectionDataType johmValueType;
    private final Field field;
    private final Object owner;
    
    public RedisMap(final Class<? extends K> keyClazz, final Class<? extends V> valueClazz, final Nest<? extends V> nest, final Field field, final Object owner) {
        this.keyClazz = keyClazz;
        this.valueClazz = valueClazz;
        this.johmKeyType = JOhmUtils.detectJOhmCollectionDataType(keyClazz);
        this.johmValueType = JOhmUtils.detectJOhmCollectionDataType(valueClazz);
        this.nest = nest;
        this.field = field;
        this.owner = owner;
    }
    
    private void indexValue(final K element) {
        if (this.field.isAnnotationPresent(Indexed.class)) {
            if (this.johmKeyType == JOhmUtils.JOhmCollectionDataType.PRIMITIVE) {
                this.nest.cat(this.field.getName()).cat(element).sadd(JOhmUtils.getId(this.owner).toString());
            }
            else if (this.johmKeyType == JOhmUtils.JOhmCollectionDataType.MODEL) {
                this.nest.cat(this.field.getName()).cat(JOhmUtils.getId(element)).sadd(JOhmUtils.getId(this.owner).toString());
            }
        }
    }
    
    private void unindexValue(final K element) {
        if (this.field.isAnnotationPresent(Indexed.class)) {
            if (this.johmKeyType == JOhmUtils.JOhmCollectionDataType.PRIMITIVE) {
                this.nest.cat(this.field.getName()).cat(element).srem(JOhmUtils.getId(this.owner).toString());
            }
            else if (this.johmKeyType == JOhmUtils.JOhmCollectionDataType.MODEL) {
                this.nest.cat(this.field.getName()).cat(JOhmUtils.getId(element)).srem(JOhmUtils.getId(this.owner).toString());
            }
        }
    }
    
    @Override
    public void clear() {
        final Map<String, String> savedHash = this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).hgetAll();
        for (final Entry<String, String> entry : savedHash.entrySet()) {
            this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).hdel(entry.getKey());
        }
        this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).del();
    }
    
    @Override
    public boolean containsKey(final Object key) {
        return this.scrollElements().containsKey(key);
    }
    
    @Override
    public boolean containsValue(final Object value) {
        return this.scrollElements().containsValue(value);
    }
    
    @Override
    public Set<Entry<K, V>> entrySet() {
        return this.scrollElements().entrySet();
    }
    
    @Override
    public V get(final Object key) {
        V value = null;
        String valueKey = null;
        if (this.johmKeyType == JOhmUtils.JOhmCollectionDataType.PRIMITIVE) {
            valueKey = this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).hget(key.toString());
        }
        else if (this.johmKeyType == JOhmUtils.JOhmCollectionDataType.MODEL) {
            valueKey = this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).hget(JOhmUtils.getId(key).toString());
        }
        if (!JOhmUtils.isNullOrEmpty(valueKey)) {
            if (this.johmValueType == JOhmUtils.JOhmCollectionDataType.PRIMITIVE) {
                value = (V)JOhmUtils.Convertor.convert(this.field, this.valueClazz, valueKey);
            }
            else if (this.johmValueType == JOhmUtils.JOhmCollectionDataType.MODEL) {
                value = RedisHelper.get(this.valueClazz, Long.valueOf(valueKey), new String[0]);
            }
        }
        return value;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public Set<K> keySet() {
        final Set<K> keys = new LinkedHashSet<K>();
        for (final String key : this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).hkeys()) {
            if (this.johmKeyType == JOhmUtils.JOhmCollectionDataType.PRIMITIVE) {
                keys.add((K)JOhmUtils.Convertor.convert(this.field, this.keyClazz, key));
            }
            else {
                if (this.johmKeyType != JOhmUtils.JOhmCollectionDataType.MODEL) {
                    continue;
                }
                keys.add(RedisHelper.get(this.keyClazz, Integer.parseInt(key), new String[0]));
            }
        }
        return keys;
    }
    
    @Override
    public V put(final K key, final V value) {
        final V previousValue = this.get(key);
        this.internalPut(key, value);
        return previousValue;
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends V> mapToCopyIn) {
        for (final Entry<? extends K, ? extends V> entry : mapToCopyIn.entrySet()) {
            this.internalPut(entry.getKey(), entry.getValue());
        }
    }
    
    @Override
    public V remove(final Object key) {
        final V value = this.get(key);
        if (this.johmKeyType == JOhmUtils.JOhmCollectionDataType.PRIMITIVE) {
            this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).hdel(key.toString());
        }
        else if (this.johmKeyType == JOhmUtils.JOhmCollectionDataType.MODEL) {
            this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).hdel(JOhmUtils.getId(key).toString());
        }
        this.unindexValue((K) key);
        return value;
    }
    
    @Override
    public int size() {
        return (int)(Object)this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).hlen();
    }
    
    @Override
    public Collection<V> values() {
        return this.scrollElements().values();
    }
    
    private V internalPut(final K key, final V value) {
        final Map<String, String> hash = new LinkedHashMap<String, String>();
        String keyString = null;
        String valueString = null;
        if (this.johmKeyType == JOhmUtils.JOhmCollectionDataType.PRIMITIVE && this.johmValueType == JOhmUtils.JOhmCollectionDataType.PRIMITIVE) {
            keyString = key.toString();
            valueString = value.toString();
        }
        else if (this.johmKeyType == JOhmUtils.JOhmCollectionDataType.PRIMITIVE && this.johmValueType == JOhmUtils.JOhmCollectionDataType.MODEL) {
            keyString = key.toString();
            valueString = JOhmUtils.getId(value).toString();
        }
        else if (this.johmKeyType == JOhmUtils.JOhmCollectionDataType.MODEL && this.johmValueType == JOhmUtils.JOhmCollectionDataType.PRIMITIVE) {
            keyString = JOhmUtils.getId(key).toString();
            valueString = value.toString();
        }
        else if (this.johmKeyType == JOhmUtils.JOhmCollectionDataType.MODEL && this.johmValueType == JOhmUtils.JOhmCollectionDataType.MODEL) {
            keyString = JOhmUtils.getId(key).toString();
            valueString = JOhmUtils.getId(value).toString();
        }
        hash.put(keyString, valueString);
        this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).hmset(hash);
        this.indexValue(key);
        return value;
    }
    
    private synchronized Map<K, V> scrollElements() {
        final Map<String, String> savedHash = this.nest.cat(JOhmUtils.getId(this.owner)).cat(this.field.getName()).hgetAll();
        final Map<K, V> backingMap = new HashMap<K, V>();
        K savedKey = null;
        V savedValue = null;
        for (final Entry<String, String> entry : savedHash.entrySet()) {
            if (this.johmKeyType == JOhmUtils.JOhmCollectionDataType.PRIMITIVE && this.johmValueType == JOhmUtils.JOhmCollectionDataType.PRIMITIVE) {
                savedKey = (K)JOhmUtils.Convertor.convert(this.field, this.keyClazz, entry.getKey());
                savedValue = (V)JOhmUtils.Convertor.convert(this.field, this.valueClazz, entry.getValue());
            }
            else if (this.johmKeyType == JOhmUtils.JOhmCollectionDataType.PRIMITIVE && this.johmValueType == JOhmUtils.JOhmCollectionDataType.MODEL) {
                savedKey = (K)JOhmUtils.Convertor.convert(this.field, this.keyClazz, entry.getKey());
                savedValue = RedisHelper.get(this.valueClazz, Integer.parseInt(entry.getValue()), new String[0]);
            }
            else if (this.johmKeyType == JOhmUtils.JOhmCollectionDataType.MODEL && this.johmValueType == JOhmUtils.JOhmCollectionDataType.PRIMITIVE) {
                savedKey = RedisHelper.get(this.keyClazz, Integer.parseInt(entry.getKey()), new String[0]);
                savedValue = (V)JOhmUtils.Convertor.convert(this.field, this.valueClazz, entry.getValue());
            }
            else if (this.johmKeyType == JOhmUtils.JOhmCollectionDataType.MODEL && this.johmValueType == JOhmUtils.JOhmCollectionDataType.MODEL) {
                savedKey = RedisHelper.get(this.keyClazz, Integer.parseInt(entry.getKey()), new String[0]);
                savedValue = RedisHelper.get(this.valueClazz, Integer.parseInt(entry.getValue()), new String[0]);
            }
            backingMap.put(savedKey, savedValue);
        }
        return backingMap;
    }
}
