package net.linaris.Practice.database;

import java.sql.*;

import net.linaris.Practice.database.daos.*;
import net.linaris.Practice.database.redis.*;

public class DatabaseConnector
{
    private static Credentials redisCredentials;
    private static RedisDatabase redisDatabase;
    private static UsersDao usersDao;
    private static BuildsDao buildsDao;
    private static MatchsDao matchsDao;
    private static StatsDao statsDao;
    
    public static void setupConnection(final Credentials redis) throws SQLException {
        DatabaseConnector.redisCredentials = redis;
        (DatabaseConnector.redisDatabase = RedisDatabase.create(redis)).setup();
        setupDaos();
    }
    
    private static void setupDaos() throws SQLException {
        DatabaseConnector.usersDao = new UsersDao();
        DatabaseConnector.buildsDao = new BuildsDao();
        DatabaseConnector.matchsDao = new MatchsDao();
        DatabaseConnector.statsDao = new StatsDao();
    }
    
    public static void closeConnection() throws SQLException {
        DatabaseConnector.redisDatabase.getPool().close();
    }
    
    public static Credentials getRedisCredentials() {
        return DatabaseConnector.redisCredentials;
    }
    
    public static RedisDatabase getRedisDatabase() {
        return DatabaseConnector.redisDatabase;
    }
    
    public static UsersDao getUsersDao() {
        return DatabaseConnector.usersDao;
    }
    
    public static BuildsDao getBuildsDao() {
        return DatabaseConnector.buildsDao;
    }
    
    public static MatchsDao getMatchsDao() {
        return DatabaseConnector.matchsDao;
    }
    
    public static StatsDao getStatsDao() {
        return DatabaseConnector.statsDao;
    }
}
