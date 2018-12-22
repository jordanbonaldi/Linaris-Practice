package net.linaris.Practice.database.redis.orm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.linaris.Practice.database.redis.orm.collections.RedisArray;
import net.linaris.Practice.database.redis.orm.collections.RedisList;
import net.linaris.Practice.database.redis.orm.collections.RedisMap;
import net.linaris.Practice.database.redis.orm.collections.RedisSet;
import net.linaris.Practice.database.redis.orm.collections.RedisSortedSet;
import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

public final class RedisHelper
{
    private static Pool<Jedis> pool;
    public static long dbIndex;
    
    public static Long getId(final Object model) {
        return JOhmUtils.getId(model);
    }
    
    public static boolean isNew(final Object model) {
        return JOhmUtils.isNew(model);
    }
    
    public static <T> boolean exists(final Class<?> clazz, final long id) {
        JOhmUtils.Validator.checkValidModelClazz(clazz);
        final Nest nest = new Nest((Class<T>)clazz);
        nest.setPool(RedisHelper.pool);
        return nest.cat(id).exists();
    }
    
    public static <T> T get(final Class<?> clazz, final long id, final String... ignoring) {
        JOhmUtils.Validator.checkValidModelClazz(clazz);
        final Nest nest = new Nest((Class<T>)clazz);
        nest.setPool(RedisHelper.pool);
        if (!nest.cat(id).exists()) {
            return null;
        }
        try {
            final Object newInstance = clazz.newInstance();
            JOhmUtils.loadId(newInstance, id);
            JOhmUtils.initCollections(newInstance, nest, ignoring);
            final Map<String, String> hashedObject = (Map<String, String>)nest.cat(id).hgetAll();
            final List<String> ignoredProperties = Arrays.asList(ignoring);
            for (final Field field : JOhmUtils.gatherAllFields(clazz, ignoring)) {
                if (ignoredProperties.contains(field.getName())) {
                    continue;
                }
                fillField(hashedObject, newInstance, field, ignoring);
                fillArrayField(nest, newInstance, field);
            }
            return (T)newInstance;
        }
        catch (InstantiationException e) {
            throw new JOhmException(e);
        }
        catch (IllegalAccessException e2) {
            throw new JOhmException(e2);
        }
    }
    
    public static <T> List<T> find(final Class<T> clazz, String attributeName, final Object attributeValue, final String... ignoring) {
        JOhmUtils.Validator.checkValidModelClazz(clazz);
        List<Object> results = null;
        if (!JOhmUtils.Validator.isIndexable(attributeName)) {
            throw new InvalidFieldException();
        }
        try {
            final Field field = clazz.getDeclaredField(attributeName);
            field.setAccessible(true);
            if (!field.isAnnotationPresent(Indexed.class)) {
                throw new InvalidFieldException();
            }
            if (field.isAnnotationPresent(Reference.class)) {
                attributeName = JOhmUtils.getReferenceKeyName(field);
            }
        }
        catch (SecurityException e) {
            throw new InvalidFieldException();
        }
        catch (NoSuchFieldException e2) {
            throw new InvalidFieldException();
        }
        if (JOhmUtils.isNullOrEmpty(attributeValue)) {
            throw new InvalidFieldException();
        }
        final Nest nest = new Nest((Class<T>)clazz);
        nest.setPool(RedisHelper.pool);
        final Set<String> modelIdStrings = (Set<String>)nest.cat(attributeName).cat(attributeValue).smembers();
        if (modelIdStrings != null) {
            results = new ArrayList<Object>();
            Object indexed = null;
            for (final String modelIdString : modelIdStrings) {
                indexed = get(clazz, Long.parseLong(modelIdString), ignoring);
                if (indexed != null) {
                    results.add(indexed);
                }
            }
        }
        return (List<T>)results;
    }
    
    public static <T> List<T> find(final Class<?> clazz, final NVField... attributes) {
        JOhmUtils.Validator.checkValidModelClazz(clazz);
        List<Object> results = null;
        final Nest nest = new Nest((Class<T>)clazz);
        nest.setPool(RedisHelper.pool);
        for (final NVField pair : attributes) {
            if (!JOhmUtils.Validator.isIndexable(pair.getAttributeName())) {
                throw new InvalidFieldException();
            }
            String attributeName;
            try {
                final Field field = clazz.getDeclaredField(pair.getAttributeName());
                field.setAccessible(true);
                if (!field.isAnnotationPresent(Indexed.class)) {
                    throw new InvalidFieldException();
                }
                if (field.isAnnotationPresent(Reference.class)) {
                    attributeName = JOhmUtils.getReferenceKeyName(field);
                }
                else {
                    attributeName = pair.getAttributeName();
                }
            }
            catch (SecurityException e) {
                throw new InvalidFieldException();
            }
            catch (NoSuchFieldException e2) {
                throw new InvalidFieldException();
            }
            if (JOhmUtils.isNullOrEmpty(pair.getAttributeValue())) {
                throw new InvalidFieldException();
            }
            nest.cat(attributeName).cat(pair.getAttributeValue()).next();
        }
        final Set<String> modelIdStrings = (Set<String>)nest.sinter();
        if (modelIdStrings != null) {
            results = new ArrayList<Object>();
            Object indexed = null;
            for (final String modelIdString : modelIdStrings) {
                indexed = get(clazz, Integer.parseInt(modelIdString), new String[0]);
                if (indexed != null) {
                    results.add(indexed);
                }
            }
        }
        return (List<T>)results;
    }
    
    public static <T> T save(final Object model) {
        return save(model, false);
    }
    
    public static <T> T save(final Object model, final boolean saveChildren) {
        if (!isNew(model)) {
            delete(model.getClass(), JOhmUtils.getId(model), true, saveChildren);
        }
        final Nest nest = initIfNeeded(model);
        final Map<String, String> hashedObject = new HashMap<String, String>();
        Map<RedisArray<Object>, Object[]> pendingArraysToPersist = null;
        try {
            String fieldName = null;
            for (final Field field : JOhmUtils.gatherAllFields(model.getClass(), new String[0])) {
                field.setAccessible(true);
                if (JOhmUtils.detectJOhmCollection(field) || field.isAnnotationPresent(Id.class)) {
                    if (!field.isAnnotationPresent(Id.class)) {
                        continue;
                    }
                    JOhmUtils.Validator.checkValidIdType(field);
                }
                else {
                    if (field.isAnnotationPresent(Array.class)) {
                        final Object[] backingArray = (Object[])field.get(model);
                        final int actualLength = (backingArray == null) ? 0 : backingArray.length;
                        JOhmUtils.Validator.checkValidArrayBounds(field, actualLength);
                        final Array annotation = field.getAnnotation(Array.class);
                        final RedisArray<Object> redisArray = new RedisArray<Object>(annotation.length(), annotation.of(), nest, field, model);
                        if (pendingArraysToPersist == null) {
                            pendingArraysToPersist = new LinkedHashMap<RedisArray<Object>, Object[]>();
                        }
                        pendingArraysToPersist.put(redisArray, backingArray);
                    }
                    JOhmUtils.Validator.checkAttributeReferenceIndexRules(field);
                    if (field.isAnnotationPresent(Attribute.class)) {
                        fieldName = field.getName();
                        final Object fieldValueObject = field.get(model);
                        if (fieldValueObject != null) {
                            if (field.getType().equals(Date.class)) {
                                try {
                                    hashedObject.put(fieldName, Long.toString(((Date)fieldValueObject).getTime()));
                                }
                                catch (Throwable e3) {
                                    hashedObject.put(fieldName, Long.toString(((Date)fieldValueObject).getTime()));
                                }
                            }
                            else {
                                hashedObject.put(fieldName, fieldValueObject.toString());
                            }
                        }
                    }
                    if (field.isAnnotationPresent(Reference.class)) {
                        fieldName = JOhmUtils.getReferenceKeyName(field);
                        final Object child = field.get(model);
                        if (child != null) {
                            if (JOhmUtils.getId(child) == null) {
                                throw new MissingIdException(fieldName);
                            }
                            if (saveChildren) {
                                save(child, saveChildren);
                            }
                            hashedObject.put(fieldName, String.valueOf(JOhmUtils.getId(child)));
                        }
                    }
                    if (!field.isAnnotationPresent(Indexed.class)) {
                        continue;
                    }
                    Object fieldValue = field.get(model);
                    if (fieldValue != null && field.isAnnotationPresent(Reference.class)) {
                        fieldValue = JOhmUtils.getId(fieldValue);
                    }
                    if (JOhmUtils.isNullOrEmpty(fieldValue)) {
                        continue;
                    }
                    nest.cat(fieldName).cat(fieldValue).sadd(String.valueOf(JOhmUtils.getId(model)));
                }
            }
        }
        catch (IllegalArgumentException e) {
            throw new JOhmException(e);
        }
        catch (IllegalAccessException e2) {
            throw new JOhmException(e2);
        }
        final Long modelId = JOhmUtils.getId(model);
        if (model.getClass().isAnnotationPresent(SupportAll.class)) {
            nest.cat("all").sadd(String.valueOf(modelId));
        }
        nest.cat(modelId).del();
        nest.cat(modelId).hmset(hashedObject);
        if (model instanceof ModelObject) {
            final ModelObject modelObject = (ModelObject)model;
            modelObject.save(nest);
        }
        if (pendingArraysToPersist != null && pendingArraysToPersist.size() > 0) {
            for (final Map.Entry<RedisArray<Object>, Object[]> arrayEntry : pendingArraysToPersist.entrySet()) {
                arrayEntry.getKey().write(arrayEntry.getValue());
            }
        }
        return (T)model;
    }
    
    public static boolean delete(final Class<?> clazz, final long id) {
        return delete(clazz, id, true, false);
    }
    
    public static <T> Long expire(final T model, final int seconds) {
        return expire(model, seconds, false);
    }
    
    public static <T> Long expire(final T model, final int seconds, final boolean expireIndexes) {
        final Nest<T> nest = (Nest<T>)initIfNeeded(model);
        if (expireIndexes) {
            for (final Field field : JOhmUtils.gatherAllFields(model.getClass(), new String[0])) {
                if (field.isAnnotationPresent(Indexed.class)) {
                    field.setAccessible(true);
                    Object fieldValue = null;
                    try {
                        fieldValue = field.get(model);
                    }
                    catch (IllegalArgumentException e) {
                        throw new JOhmException(e);
                    }
                    catch (IllegalAccessException e2) {
                        throw new JOhmException(e2);
                    }
                    if (fieldValue != null && field.isAnnotationPresent(Reference.class)) {
                        fieldValue = JOhmUtils.getId(fieldValue);
                    }
                    if (JOhmUtils.isNullOrEmpty(fieldValue)) {
                        continue;
                    }
                    nest.cat(field.getName()).cat(fieldValue).expire(seconds);
                }
            }
        }
        return nest.cat(JOhmUtils.getId(model)).expire(seconds);
    }
    
    public static <T> boolean delete(final Class<?> clazz, final long id, final boolean deleteIndexes, final boolean deleteChildren) {
        JOhmUtils.Validator.checkValidModelClazz(clazz);
        boolean deleted = false;
        final Object persistedModel = get(clazz, id, new String[0]);
        if (persistedModel != null) {
            final Nest nest = new Nest((T)persistedModel);
            nest.setPool(RedisHelper.pool);
            if (deleteIndexes) {
                for (final Field field : JOhmUtils.gatherAllFields(clazz, new String[0])) {
                    if (field.isAnnotationPresent(Indexed.class)) {
                        field.setAccessible(true);
                        Object fieldValue = null;
                        try {
                            fieldValue = field.get(persistedModel);
                        }
                        catch (IllegalArgumentException e) {
                            throw new JOhmException(e);
                        }
                        catch (IllegalAccessException e2) {
                            throw new JOhmException(e2);
                        }
                        if (fieldValue != null && field.isAnnotationPresent(Reference.class)) {
                            fieldValue = JOhmUtils.getId(fieldValue);
                        }
                        if (JOhmUtils.isNullOrEmpty(fieldValue)) {
                            continue;
                        }
                        nest.cat(field.getName()).cat(fieldValue).srem(String.valueOf(id));
                    }
                }
            }
            if (deleteChildren) {
                for (final Field field : JOhmUtils.gatherAllFields(clazz, new String[0])) {
                    if (field.isAnnotationPresent(Reference.class)) {
                        field.setAccessible(true);
                        try {
                            final Object child = field.get(persistedModel);
                            if (child != null) {
                                delete(child.getClass(), JOhmUtils.getId(child), deleteIndexes, deleteChildren);
                            }
                        }
                        catch (IllegalArgumentException e3) {
                            throw new JOhmException(e3);
                        }
                        catch (IllegalAccessException e4) {
                            throw new JOhmException(e4);
                        }
                    }
                    clearRedisCollection(field, nest, persistedModel);
                }
            }
            deleted = (nest.cat(id).del() == 1L);
        }
        return deleted;
    }
    
    private static void clearRedisCollection(final Field field, final Nest nest, final Object persistedModel) {
        if (field.isAnnotationPresent(Array.class)) {
            field.setAccessible(true);
            final Array annotation = field.getAnnotation(Array.class);
            new RedisArray(annotation.length(), annotation.of(), nest, field, persistedModel).clear();
        }
        if (field.isAnnotationPresent(CollectionList.class)) {
            field.setAccessible(true);
            final CollectionList annotation2 = field.getAnnotation(CollectionList.class);
            new RedisList(annotation2.of(), nest, field, persistedModel, new String[0]).clear();
        }
        if (field.isAnnotationPresent(CollectionSet.class)) {
            field.setAccessible(true);
            final CollectionSet annotation3 = field.getAnnotation(CollectionSet.class);
            new RedisSet(annotation3.of(), nest, field, persistedModel).clear();
        }
        if (field.isAnnotationPresent(CollectionSortedSet.class)) {
            field.setAccessible(true);
            final CollectionSortedSet annotation4 = field.getAnnotation(CollectionSortedSet.class);
            new RedisSortedSet(annotation4.of(), annotation4.by(), nest, field, persistedModel).clear();
        }
        if (field.isAnnotationPresent(CollectionMap.class)) {
            field.setAccessible(true);
            final CollectionMap annotation5 = field.getAnnotation(CollectionMap.class);
            new RedisMap(annotation5.key(), annotation5.value(), nest, field, persistedModel).clear();
        }
    }
    
    public static Pool<Jedis> setPool(final Pool<Jedis> pool) {
        return RedisHelper.pool = pool;
    }
    
    public static Pool<Jedis> getPool() {
        return RedisHelper.pool;
    }
    
    private static void fillField(final Map<String, String> hashedObject, final Object newInstance, final Field field, final String... ignoring) throws IllegalAccessException {
        JOhmUtils.Validator.checkAttributeReferenceIndexRules(field);
        if (field.isAnnotationPresent(Attribute.class)) {
            field.setAccessible(true);
            field.set(newInstance, JOhmUtils.Convertor.convert(field, hashedObject.get(field.getName())));
        }
        if (field.isAnnotationPresent(Reference.class)) {
            field.setAccessible(true);
            final String serializedReferenceId = hashedObject.get(JOhmUtils.getReferenceKeyName(field));
            if (serializedReferenceId != null) {
                final Long referenceId = Long.valueOf(serializedReferenceId);
                field.set(newInstance, get(field.getType(), referenceId, ignoring));
            }
        }
    }
    
    private static <T> void fillArrayField(final Nest nest, final Object model, final Field field) throws IllegalArgumentException, IllegalAccessException {
        if (field.isAnnotationPresent(Array.class)) {
            field.setAccessible(true);
            final Array annotation = field.getAnnotation(Array.class);
            final RedisArray redisArray = new RedisArray(annotation.length(), (Class<? extends T>)annotation.of(), nest, field, model);
            field.set(model, redisArray.read());
        }
    }
    
    private static <T> Nest initIfNeeded(final Object model) {
        Long id = JOhmUtils.getId(model);
        final Nest nest = new Nest((T)model);
        nest.setPool(RedisHelper.pool);
        if (id == null) {
            id = nest.cat("id").incr();
            JOhmUtils.loadId(model, id);
            JOhmUtils.initCollections(model, nest, new String[0]);
        }
        return nest;
    }
    
    public static <T> Set<T> getAll(final Class<T> clazz) {
        JOhmUtils.Validator.checkValidModelClazz(clazz);
        JOhmUtils.Validator.checkSupportAll(clazz);
        Set<Object> results = null;
        final Nest nest = new Nest((Class<T>)clazz);
        nest.setPool(RedisHelper.pool);
        final Set<String> modelIdStrings = (Set<String>)nest.cat("all").smembers();
        if (modelIdStrings != null) {
            results = new HashSet<Object>();
            Object indexed = null;
            for (final String modelIdString : modelIdStrings) {
                indexed = get(clazz, Long.parseLong(modelIdString), new String[0]);
                if (indexed != null) {
                    results.add(indexed);
                }
            }
        }
        return (Set<T>)results;
    }
    
    public static void selectDb(final long dbIndex) {
        RedisHelper.dbIndex = dbIndex;
    }
    
    public static void flushDb() {
        final Jedis jedis = RedisHelper.pool.getResource();
        try {
            if (!jedis.getDB().equals(RedisHelper.dbIndex)) {
                jedis.select((int)RedisHelper.dbIndex);
            }
            jedis.flushDB();
        }
        finally {
            RedisHelper.pool.returnResource(jedis);
        }
    }
    
    static {
        RedisHelper.dbIndex = 0L;
    }
}
