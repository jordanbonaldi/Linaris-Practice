package net.linaris.Practice.database.redis;

import org.apache.commons.pool2.impl.*;

import net.linaris.Practice.database.*;
import net.linaris.Practice.database.redis.orm.*;
import redis.clients.util.*;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.*;

public final class RedisDatabase implements Database
{
    private final Credentials credentials;
    private JedisPool pool;
    
    private RedisDatabase(final Credentials credentials) {
        this.credentials = credentials;
    }
    
    public static RedisDatabase create(final Credentials credentials) {
        return new RedisDatabase(credentials);
    }
    
    @Override
    public void setup() {
        final JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20);
        config.setMinIdle(5);
        config.setMaxIdle(10);
        config.setMaxWaitMillis(200L);
        config.setBlockWhenExhausted(false);
        final String host = this.credentials.getHost();
        int port = 6379;
        if (host.split(":").length == 2) {
            try {
                port = Integer.parseInt(host.split(":")[1]);
            }
            catch (NumberFormatException ignored) {
                System.out.println("Host " + host + " has an invalid port!");
            }
        }
        RedisHelper.setPool(this.pool = ((this.credentials.getPassword() != null && this.credentials.getPassword().length() > 0) ? new JedisPool(config, host, port, 1000, this.credentials.getPassword(), this.credentials.database()) : new JedisPool(config, host, port, 1000)));
    }
    
    public Jedis getResource() {
        return this.pool.getResource();
    }
    
    public Credentials credentials() {
        return this.credentials;
    }
    
    public boolean connected() {
        try (final Jedis jedis = this.getResource()) {
            return true;
        }
        catch (JedisConnectionException e) {
            System.out.println("Redis connection had died, reconnecting.");
            final boolean connection = false;
            this.setup();
            return connection;
        }
    }
    
    public JedisPool getPool() {
        return this.pool;
    }
}
