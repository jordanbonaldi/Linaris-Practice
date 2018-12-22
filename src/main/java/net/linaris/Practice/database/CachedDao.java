package net.linaris.Practice.database;

import java.util.List;
import java.util.Optional;

import net.linaris.Practice.database.redis.orm.NVField;
import net.linaris.Practice.database.redis.orm.RedisHelper;

public class CachedDao<M>
{
    private Class<M> clazz;
    
    public CachedDao(final Class<M> clazz) {
        this.clazz = clazz;
    }
    
    public M get(final Long id, final String... ignoring) {
        return RedisHelper.get(this.clazz, id, ignoring);
    }
    
    public List<M> find(final String attributeName, final String attributeValue, final String... ignore) {
        return RedisHelper.find(this.clazz, attributeName, attributeValue, ignore);
    }
    
    public Optional<M> findOne(final String attributeName, final String attributeValue, final String... ignore) {
        return this.find(attributeName, attributeValue, ignore).stream().findFirst();
    }
    
    public List<M> find(final NVField... fields) {
        return RedisHelper.find(this.clazz, fields);
    }
    
    public Optional<M> findOne(final NVField... fields) {
        return this.find(fields).stream().findFirst();
    }
    
    public boolean exist(final Class<M> clazz, final Long id) {
        return RedisHelper.exists(clazz, id);
    }
    
    public boolean exist(final Class<M> clazz, final String attributeName, final String attributeValue) {
        return !this.find(attributeName, attributeValue, new String[0]).isEmpty();
    }
    
    public boolean exist(final Class<M> clazz, final NVField... fields) {
        return !this.find(fields).isEmpty();
    }
    
    public M save(final M model) {
        RedisHelper.save(model);
        return model;
    }
    
    public Class<M> getClazz() {
        return this.clazz;
    }
}
