package net.linaris.Practice.database.redis.orm;

import redis.clients.util.*;
import redis.clients.jedis.exceptions.*;
import java.util.*;
import redis.clients.jedis.*;

public class Nest<T>
{
    private static final String COLON = ":";
    private static final String SPACE = " ";
    private StringBuilder sb;
    private String key;
    private List<String> keys;
    private Pool<Jedis> jedisPool;
    
    public void setPool(final Pool<Jedis> pool) {
        this.jedisPool = pool;
        this.checkRedisLiveness();
    }
    
    public List<String> keys() {
        return this.keys;
    }
    
    public Nest<T> fork() {
        return new Nest<T>(this.key());
    }
    
    public Nest() {
        this.key = "";
    }
    
    public Nest(final String key) {
        this.key = key;
    }
    
    public Nest(final Class<T> clazz) {
        this.key = JOhmUtils.getModelKey(clazz);
    }
    
    public Nest(final T model) {
        this.key = JOhmUtils.getModelKey(model.getClass());
    }
    
    public String key() {
        this.prefix();
        String generatedKey = this.sb.toString();
        generatedKey = generatedKey.substring(0, generatedKey.length() - 1);
        this.sb = null;
        return generatedKey;
    }
    
    private void prefix() {
        if (this.sb == null) {
            (this.sb = new StringBuilder()).append(this.key);
            this.sb.append(":");
        }
    }
    
    public Nest<T> next() {
        if (this.keys == null) {
            this.keys = new ArrayList<String>();
        }
        this.keys.add(this.key());
        return this;
    }
    
    public Nest<T> cat(final int id) {
        this.prefix();
        this.sb.append(id);
        this.sb.append(":");
        return this;
    }
    
    public Nest<T> cat(final Object field) {
        this.prefix();
        this.sb.append(field);
        this.sb.append(":");
        return this;
    }
    
    public Nest<T> cat(final String field) {
        this.prefix();
        this.sb.append(field);
        this.sb.append(":");
        return this;
    }
    
    public String set(final String value) {
        final Jedis jedis = this.getResource();
        final String set = jedis.set(this.key(), value);
        this.returnResource(jedis);
        return set;
    }
    
    public String get() {
        final Jedis jedis = this.getResource();
        final String string = jedis.get(this.key());
        this.returnResource(jedis);
        return string;
    }
    
    public Long incr() {
        final Jedis jedis = this.getResource();
        final Long incr = jedis.incr(this.key());
        this.returnResource(jedis);
        return incr;
    }
    
    public Long expire(final int seconds) {
        final Jedis jedis = this.getResource();
        final Long expire = jedis.expire(this.key(), seconds);
        this.returnResource(jedis);
        return expire;
    }
    
    public List<Object> multi(final TransactionBlock transaction) {
        final Jedis jedis = this.getResource();
        try {
            return jedis.multi(transaction);
        }
        catch (Exception e) {
            if (jedis.getClient().isInMulti()) {
                jedis.getClient().discard();
            }
            throw new JedisException(e);
        }
        finally {
            this.returnResource(jedis);
        }
    }
    
    public Long del() {
        final Jedis jedis = this.getResource();
        final Long del = jedis.del(this.key());
        this.returnResource(jedis);
        return del;
    }
    
    public Boolean exists() {
        final Jedis jedis = this.getResource();
        final Boolean exists = jedis.exists(this.key());
        this.returnResource(jedis);
        return exists;
    }
    
    public String hmset(final Map<String, String> hash) {
        final Jedis jedis = this.getResource();
        final String hmset = jedis.hmset(this.key(), hash);
        this.returnResource(jedis);
        return hmset;
    }
    
    public Map<String, String> hgetAll() {
        final Jedis jedis = this.getResource();
        final Map<String, String> hgetAll = jedis.hgetAll(this.key());
        this.returnResource(jedis);
        return hgetAll;
    }
    
    public String hget(final String field) {
        final Jedis jedis = this.getResource();
        final String value = jedis.hget(this.key(), field);
        this.returnResource(jedis);
        return value;
    }
    
    public Long hdel(final String field) {
        final Jedis jedis = this.getResource();
        final Long hdel = jedis.hdel(this.key(), field);
        this.returnResource(jedis);
        return hdel;
    }
    
    public Long hlen() {
        final Jedis jedis = this.getResource();
        final Long hlen = jedis.hlen(this.key());
        this.returnResource(jedis);
        return hlen;
    }
    
    public Set<String> hkeys() {
        final Jedis jedis = this.getResource();
        final Set<String> hkeys = jedis.hkeys(this.key());
        this.returnResource(jedis);
        return hkeys;
    }
    
    public Long sadd(final String member) {
        final Jedis jedis = this.getResource();
        final Long reply = jedis.sadd(this.key(), member);
        this.returnResource(jedis);
        return reply;
    }
    
    public Long srem(final String member) {
        final Jedis jedis = this.getResource();
        final Long reply = jedis.srem(this.key(), member);
        this.returnResource(jedis);
        return reply;
    }
    
    public Set<String> sinter() {
        final Jedis jedis = this.getResource();
        final Set<String> members = jedis.sinter((String[])this.keys.toArray(new String[0]));
        this.returnResource(jedis);
        return members;
    }
    
    public Set<String> smembers() {
        final Jedis jedis = this.getResource();
        final Set<String> members = jedis.smembers(this.key());
        this.returnResource(jedis);
        return members;
    }
    
    public Long rpush(final String string) {
        final Jedis jedis = this.getResource();
        final Long rpush = jedis.rpush(this.key(), string);
        this.returnResource(jedis);
        return rpush;
    }
    
    public String lset(final int index, final String value) {
        final Jedis jedis = this.getResource();
        final String lset = jedis.lset(this.key(), index, value);
        this.returnResource(jedis);
        return lset;
    }
    
    public String lindex(final int index) {
        final Jedis jedis = this.getResource();
        final String lindex = jedis.lindex(this.key(), index);
        this.returnResource(jedis);
        return lindex;
    }
    
    public Long llen() {
        final Jedis jedis = this.getResource();
        final Long llen = jedis.llen(this.key());
        this.returnResource(jedis);
        return llen;
    }
    
    public Long lrem(final int count, final String value) {
        final Jedis jedis = this.getResource();
        final Long lrem = jedis.lrem(this.key(), count, value);
        this.returnResource(jedis);
        return lrem;
    }
    
    public List<String> lrange(final int start, final int end) {
        final Jedis jedis = this.getResource();
        final List<String> lrange = jedis.lrange(this.key(), start, end);
        this.returnResource(jedis);
        return lrange;
    }
    
    public Set<String> zrange(final int start, final int end) {
        final Jedis jedis = this.getResource();
        final Set<String> zrange = jedis.zrange(this.key(), start, end);
        this.returnResource(jedis);
        return zrange;
    }
    
    public Set<String> zrevrange(final int start, final int end) {
        final Jedis jedis = this.getResource();
        final Set<String> zrange = jedis.zrevrange(this.key(), start, end);
        this.returnResource(jedis);
        return zrange;
    }
    
    public Set<Tuple> zrevrangewith(final int start, final int end) {
        final Jedis jedis = this.getResource();
        final Set<Tuple> zrange = jedis.zrevrangeWithScores(this.key(), start, end);
        this.returnResource(jedis);
        return zrange;
    }
    
    public Set<String> zrangebyscore(final int start, final int end) {
        final Jedis jedis = this.getResource();
        final Set<String> zrange = jedis.zrangeByScore(this.key(), start, end);
        this.returnResource(jedis);
        return zrange;
    }
    
    public Set<String> zrevrangebyscore(final int start, final int end) {
        final Jedis jedis = this.getResource();
        final Set<String> zrange = jedis.zrevrangeByScore(this.key(), start, end);
        this.returnResource(jedis);
        return zrange;
    }
    
    public Long zadd(final float score, final String member) {
        final Jedis jedis = this.getResource();
        final Long zadd = jedis.zadd(this.key(), score, member);
        this.returnResource(jedis);
        return zadd;
    }
    
    public Long zcard() {
        final Jedis jedis = this.getResource();
        final Long zadd = jedis.zcard(this.key());
        this.returnResource(jedis);
        return zadd;
    }
    
    public void returnResource(final Jedis jedis) {
        this.jedisPool.returnResource(jedis);
    }
    
    public Jedis getResource() {
        final Jedis jedis = this.jedisPool.getResource();
        if (!jedis.getDB().equals(RedisHelper.dbIndex)) {
            jedis.select((int)RedisHelper.dbIndex);
        }
        return jedis;
    }
    
    private void checkRedisLiveness() {
        if (this.jedisPool == null) {
            throw new JOhmException("JOhm will fail to do most useful tasks without Redis");
        }
    }
}
