package net.linaris.Practice.database.redis.orm;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.linaris.Practice.database.redis.orm.collections.RedisList;
import net.linaris.Practice.database.redis.orm.collections.RedisMap;
import net.linaris.Practice.database.redis.orm.collections.RedisSet;
import net.linaris.Practice.database.redis.orm.collections.RedisSortedSet;

public final class JOhmUtils
{
    private static final Set<Class<?>> JOHM_SUPPORTED_PRIMITIVES;
    private static final Set<Class<?>> JOHM_SUPPORTED_ANNOTATIONS;
    
    static String getReferenceKeyName(final Field field) {
        return field.getName() + "_id";
    }
    
    public static Long getId(final Object model) {
        return getId(model, true);
    }
    
    public static Long getId(final Object model, final boolean checkValidity) {
        Long id = null;
        if (model != null) {
            if (checkValidity) {
                Validator.checkValidModel(model);
            }
            id = Validator.checkValidId(model);
        }
        return id;
    }
    
    public static String getModelKey(final Class<?> clazz) {
        final Model model = clazz.getAnnotation(Model.class);
        return (model == null || model.value().equals("")) ? clazz.getSimpleName() : model.value();
    }
    
    static boolean isNew(final Object model) {
        return getId(model) == null;
    }
    
    static void initCollections(final Object model, final Nest<?> nest, final String... ignoring) {
        if (model == null || nest == null) {
            return;
        }
        final List<String> ignoredProperties = Arrays.asList(ignoring);
        for (final Field field : model.getClass().getDeclaredFields()) {
            if (!ignoredProperties.contains(field.getName())) {
                field.setAccessible(true);
                try {
                    if (field.isAnnotationPresent(CollectionList.class)) {
                        Validator.checkValidCollection(field);
                        final List<Object> list = (List<Object>)field.get(model);
                        if (list == null) {
                            final CollectionList annotation = field.getAnnotation(CollectionList.class);
                            final RedisList<Object> redisList = new RedisList<Object>(annotation.of(), nest, field, model, ignoring);
                            field.set(model, redisList);
                        }
                    }
                    if (field.isAnnotationPresent(CollectionSet.class)) {
                        Validator.checkValidCollection(field);
                        final Set<Object> set = (Set<Object>)field.get(model);
                        if (set == null) {
                            final CollectionSet annotation2 = field.getAnnotation(CollectionSet.class);
                            final RedisSet<Object> redisSet = new RedisSet<Object>(annotation2.of(), nest, field, model);
                            field.set(model, redisSet);
                        }
                    }
                    if (field.isAnnotationPresent(CollectionSortedSet.class)) {
                        Validator.checkValidCollection(field);
                        final Set<Object> sortedSet = (Set<Object>)field.get(model);
                        if (sortedSet == null) {
                            final CollectionSortedSet annotation3 = field.getAnnotation(CollectionSortedSet.class);
                            final RedisSortedSet<Object> redisSortedSet = new RedisSortedSet<Object>(annotation3.of(), annotation3.by(), nest, field, model);
                            field.set(model, redisSortedSet);
                        }
                    }
                    if (field.isAnnotationPresent(CollectionMap.class)) {
                        Validator.checkValidCollection(field);
                        final Map<Object, Object> map = (Map<Object, Object>)field.get(model);
                        if (map == null) {
                            final CollectionMap annotation4 = field.getAnnotation(CollectionMap.class);
                            final RedisMap<Object, Object> redisMap = new RedisMap<Object, Object>(annotation4.key(), annotation4.value(), nest, field, model);
                            field.set(model, redisMap);
                        }
                    }
                }
                catch (IllegalArgumentException e) {
                    throw new InvalidFieldException();
                }
                catch (IllegalAccessException e2) {
                    throw new InvalidFieldException();
                }
            }
        }
    }
    
    static void loadId(final Object model, final Long id) {
        if (model != null) {
            boolean idFieldPresent = false;
            for (final Field field : model.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Id.class)) {
                    idFieldPresent = true;
                    Validator.checkValidIdType(field);
                    try {
                        field.set(model, id);
                        break;
                    }
                    catch (IllegalArgumentException e) {
                        throw new JOhmException(e);
                    }
                    catch (IllegalAccessException e2) {
                        throw new JOhmException(e2);
                    }
                }
            }
            if (!idFieldPresent) {
                throw new JOhmException("JOhm does not support a Model without an Id");
            }
        }
    }
    
    static boolean detectJOhmCollection(final Field field) {
        boolean isJOhmCollection = false;
        if (field.isAnnotationPresent(CollectionList.class) || field.isAnnotationPresent(CollectionSet.class) || field.isAnnotationPresent(CollectionSortedSet.class) || field.isAnnotationPresent(CollectionMap.class)) {
            isJOhmCollection = true;
        }
        return isJOhmCollection;
    }
    
    public static JOhmCollectionDataType detectJOhmCollectionDataType(final Class<?> dataClazz) {
        JOhmCollectionDataType type = null;
        if (Validator.checkSupportedPrimitiveClazz(dataClazz)) {
            type = JOhmCollectionDataType.PRIMITIVE;
        }
        else {
            try {
                Validator.checkValidModelClazz(dataClazz);
                type = JOhmCollectionDataType.MODEL;
            }
            catch (JOhmException ex) {}
        }
        if (type == null) {
            throw new JOhmException(dataClazz.getSimpleName() + " is not a supported JOhm Collection Data Type");
        }
        return type;
    }
    
    public static boolean isNullOrEmpty(final Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj.getClass().equals(Collection.class)) {
            return ((Collection)obj).size() == 0;
        }
        return obj.toString().trim().length() == 0;
    }
    
    static List<Field> gatherAllFields(Class<?> clazz, final String... ignoring) {
        final List<Field> allFields = new ArrayList<Field>();
        for (final Field field : clazz.getDeclaredFields()) {
            allFields.add(field);
        }
        while ((clazz = clazz.getSuperclass()) != null) {
            allFields.addAll(gatherAllFields(clazz, new String[0]));
        }
        for (final String ignore : ignoring) {
            if (allFields.contains(ignore)) {
                allFields.remove(ignore);
            }
        }
        return Collections.unmodifiableList((List<? extends Field>)allFields);
    }
    
    static {
        JOHM_SUPPORTED_PRIMITIVES = new HashSet<Class<?>>();
        JOHM_SUPPORTED_ANNOTATIONS = new HashSet<Class<?>>();
        JOhmUtils.JOHM_SUPPORTED_PRIMITIVES.add(String.class);
        JOhmUtils.JOHM_SUPPORTED_PRIMITIVES.add(Byte.class);
        JOhmUtils.JOHM_SUPPORTED_PRIMITIVES.add(Byte.TYPE);
        JOhmUtils.JOHM_SUPPORTED_PRIMITIVES.add(Character.class);
        JOhmUtils.JOHM_SUPPORTED_PRIMITIVES.add(Character.TYPE);
        JOhmUtils.JOHM_SUPPORTED_PRIMITIVES.add(Short.class);
        JOhmUtils.JOHM_SUPPORTED_PRIMITIVES.add(Short.TYPE);
        JOhmUtils.JOHM_SUPPORTED_PRIMITIVES.add(Integer.class);
        JOhmUtils.JOHM_SUPPORTED_PRIMITIVES.add(Integer.TYPE);
        JOhmUtils.JOHM_SUPPORTED_PRIMITIVES.add(Float.class);
        JOhmUtils.JOHM_SUPPORTED_PRIMITIVES.add(Float.TYPE);
        JOhmUtils.JOHM_SUPPORTED_PRIMITIVES.add(Double.class);
        JOhmUtils.JOHM_SUPPORTED_PRIMITIVES.add(Double.TYPE);
        JOhmUtils.JOHM_SUPPORTED_PRIMITIVES.add(Long.class);
        JOhmUtils.JOHM_SUPPORTED_PRIMITIVES.add(Long.TYPE);
        JOhmUtils.JOHM_SUPPORTED_PRIMITIVES.add(Boolean.class);
        JOhmUtils.JOHM_SUPPORTED_PRIMITIVES.add(Boolean.TYPE);
        JOhmUtils.JOHM_SUPPORTED_PRIMITIVES.add(BigDecimal.class);
        JOhmUtils.JOHM_SUPPORTED_PRIMITIVES.add(BigInteger.class);
        JOhmUtils.JOHM_SUPPORTED_PRIMITIVES.add(Date.class);
        JOhmUtils.JOHM_SUPPORTED_ANNOTATIONS.add(Array.class);
        JOhmUtils.JOHM_SUPPORTED_ANNOTATIONS.add(Attribute.class);
        JOhmUtils.JOHM_SUPPORTED_ANNOTATIONS.add(CollectionList.class);
        JOhmUtils.JOHM_SUPPORTED_ANNOTATIONS.add(CollectionMap.class);
        JOhmUtils.JOHM_SUPPORTED_ANNOTATIONS.add(CollectionSet.class);
        JOhmUtils.JOHM_SUPPORTED_ANNOTATIONS.add(CollectionSortedSet.class);
        JOhmUtils.JOHM_SUPPORTED_ANNOTATIONS.add(Id.class);
        JOhmUtils.JOHM_SUPPORTED_ANNOTATIONS.add(Indexed.class);
        JOhmUtils.JOHM_SUPPORTED_ANNOTATIONS.add(Model.class);
        JOhmUtils.JOHM_SUPPORTED_ANNOTATIONS.add(Reference.class);
    }
    
    public enum JOhmCollectionDataType
    {
        PRIMITIVE, 
        MODEL;
    }
    
    public static final class Convertor
    {
        static Object convert(final Field field, final String value) {
            return convert(field, field.getType(), value);
        }
        
        public static Object convert(final Class<?> type, final String value) {
            return convert(null, type, value);
        }
        
        public static Object convert(final Field field, final Class<?> type, final String value) {
            if (type.equals(Byte.class) || type.equals(Byte.TYPE)) {
                return new Byte(value);
            }
            if (type.equals(Character.class) || type.equals(Character.TYPE)) {
                if (JOhmUtils.isNullOrEmpty(value)) {
                    return '\0';
                }
                if (value.length() > 1) {
                    throw new IllegalArgumentException("Non-character value masquerading as characters in a string");
                }
                return value.charAt(0);
            }
            else {
                if (type.equals(Short.class) || type.equals(Short.TYPE)) {
                    return new Short(value);
                }
                if (type.equals(Integer.class) || type.equals(Integer.TYPE)) {
                    if (value == null) {
                        return 0;
                    }
                    return new Integer(value);
                }
                else if (type.equals(Float.class) || type.equals(Float.TYPE)) {
                    if (value == null) {
                        return 0.0f;
                    }
                    return new Float(value);
                }
                else if (type.equals(Double.class) || type.equals(Double.TYPE)) {
                    if (value == null) {
                        return 0.0;
                    }
                    return new Double(value);
                }
                else if (type.equals(Long.class) || type.equals(Long.TYPE)) {
                    if (value == null) {
                        return 0L;
                    }
                    return new Long(value);
                }
                else if (type.equals(Boolean.class) || type.equals(Boolean.TYPE)) {
                    if (value == null) {
                        return false;
                    }
                    return new Boolean(value);
                }
                else if (type.equals(BigDecimal.class)) {
                    if (value == null) {
                        return BigDecimal.ZERO;
                    }
                    return new BigDecimal(value);
                }
                else if (type.equals(BigInteger.class)) {
                    if (value == null) {
                        return BigInteger.ZERO;
                    }
                    return new BigInteger(value);
                }
                else {
                    if (type.isEnum() || type.equals(Enum.class)) {
                        return null;
                    }
                    if (type.equals(Date.class)) {
                        return (value == null) ? new Date() : new Date(Long.parseLong(value));
                    }
                    if (type.equals(Collection.class)) {
                        return null;
                    }
                    if (type.isArray()) {
                        return null;
                    }
                    return value;
                }
            }
        }
    }
    
    public static final class Validator
    {
        public static void checkValidAttribute(final Field field) {
            final Class<?> type = field.getType();
            if (!type.equals(Byte.class) && !type.equals(Byte.TYPE) && !type.equals(Character.class) && !type.equals(Character.TYPE) && !type.equals(Short.class) && !type.equals(Short.TYPE) && !type.equals(Integer.class) && !type.equals(Integer.TYPE) && !type.equals(Float.class) && !type.equals(Float.TYPE) && !type.equals(Double.class) && !type.equals(Double.TYPE) && !type.equals(Long.class) && !type.equals(Long.TYPE) && !type.equals(Boolean.class) && !type.equals(Boolean.TYPE) && !type.equals(BigDecimal.class) && !type.equals(BigInteger.class) && !type.equals(String.class) && !type.equals(Date.class) && !type.isEnum() && !type.equals(Enum.class)) {
                throw new JOhmException(field.getType().getSimpleName() + " is not a JOhm-supported Attribute");
            }
        }
        
        public static void checkValidReference(final Field field) {
            if (!field.getType().getClass().isInstance(Model.class) && !field.getType().getClass().isAssignableFrom(ModelObject.class)) {
                throw new JOhmException(field.getType().getSimpleName() + " is not a subclass of Model");
            }
        }
        
        public static Long checkValidId(final Object model) {
            Long id = null;
            boolean idFieldPresent = false;
            for (final Field field : model.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Id.class)) {
                    checkValidIdType(field);
                    try {
                        id = (Long)field.get(model);
                        idFieldPresent = true;
                        break;
                    }
                    catch (IllegalArgumentException e) {
                        throw new JOhmException(e);
                    }
                    catch (IllegalAccessException e2) {
                        throw new JOhmException(e2);
                    }
                }
            }
            if (!idFieldPresent) {
                throw new JOhmException("JOhm does not support a Model without an Id");
            }
            return id;
        }
        
        public static void checkValidIdType(final Field field) {
            final Annotation[] annotations = field.getAnnotations();
            if (annotations.length > 1) {
                for (final Annotation annotation : annotations) {
                    final Class<?> annotationType = annotation.annotationType();
                    if (!annotationType.equals(Id.class)) {
                        if (JOhmUtils.JOHM_SUPPORTED_ANNOTATIONS.contains(annotationType)) {
                            throw new JOhmException("Element annotated @Id cannot have any other JOhm annotations");
                        }
                    }
                }
            }
            final Class<?> type = field.getType().getClass();
            if (!type.isInstance(Long.class) || !type.isInstance(Long.TYPE)) {
                throw new JOhmException(field.getType().getSimpleName() + " is annotated an Id but is not a long");
            }
        }
        
        public static boolean isIndexable(final String attributeName) {
            return !JOhmUtils.isNullOrEmpty(attributeName);
        }
        
        public static void checkValidModel(final Object model) {
            checkValidModelClazz(model.getClass());
        }
        
        public static void checkValidModelClazz(final Class<?> modelClazz) {
            if (!modelClazz.isAnnotationPresent(Model.class) && !modelClazz.isAssignableFrom(ModelObject.class)) {
                throw new JOhmException("Class pretending to be a Model but is not really annotated");
            }
            if (modelClazz.isInterface()) {
                throw new JOhmException("An interface cannot be annotated as a Model");
            }
        }
        
        public static void checkSupportAll(final Class<?> modelClazz) {
            if (!modelClazz.isAnnotationPresent(SupportAll.class)) {
                throw new JOhmException("This Model does'nt support getAll(). Please annotate with @SupportAll");
            }
        }
        
        public static void checkValidCollection(final Field field) {
            boolean isList = false;
            boolean isSet = false;
            boolean isMap = false;
            boolean isSortedSet = false;
            if (field.isAnnotationPresent(CollectionList.class)) {
                checkValidCollectionList(field);
                isList = true;
            }
            if (field.isAnnotationPresent(CollectionSet.class)) {
                checkValidCollectionSet(field);
                isSet = true;
            }
            if (field.isAnnotationPresent(CollectionSortedSet.class)) {
                checkValidCollectionSortedSet(field);
                isSortedSet = true;
            }
            if (field.isAnnotationPresent(CollectionMap.class)) {
                checkValidCollectionMap(field);
                isMap = true;
            }
            if (isList && isSet && isMap && isSortedSet) {
                throw new JOhmException(field.getName() + " can be declared a List or a Set or a SortedSet or a Map but not more than one type");
            }
        }
        
        public static void checkValidCollectionList(final Field field) {
            if (!field.getType().getClass().isInstance(List.class)) {
                throw new JOhmException(field.getType().getSimpleName() + " is not a subclass of List");
            }
        }
        
        public static void checkValidCollectionSet(final Field field) {
            if (!field.getType().getClass().isInstance(Set.class)) {
                throw new JOhmException(field.getType().getSimpleName() + " is not a subclass of Set");
            }
        }
        
        public static void checkValidCollectionSortedSet(final Field field) {
            if (!field.getType().getClass().isInstance(Set.class)) {
                throw new JOhmException(field.getType().getSimpleName() + " is not a subclass of Set");
            }
        }
        
        public static void checkValidCollectionMap(final Field field) {
            if (!field.getType().getClass().isInstance(Map.class)) {
                throw new JOhmException(field.getType().getSimpleName() + " is not a subclass of Map");
            }
        }
        
        public static void checkValidArrayBounds(final Field field, final int actualLength) {
            if (field.getAnnotation(Array.class).length() < actualLength) {
                throw new JOhmException(field.getType().getSimpleName() + " has an actual length greater than the expected annotated array bounds");
            }
        }
        
        public static void checkAttributeReferenceIndexRules(final Field field) {
            final boolean isAttribute = field.isAnnotationPresent(Attribute.class);
            final boolean isReference = field.isAnnotationPresent(Reference.class);
            final boolean isIndexed = field.isAnnotationPresent(Indexed.class);
            if (isAttribute) {
                if (isReference) {
                    throw new JOhmException(field.getName() + " is both an Attribute and a Reference which is invalid");
                }
                if (isIndexed && !isIndexable(field.getName())) {
                    throw new InvalidFieldException();
                }
                if (field.getType().equals(Model.class)) {
                    throw new JOhmException(field.getType().getSimpleName() + " is an Attribute and a Model which is invalid");
                }
                if (field.getType().getClass().isAssignableFrom(ModelObject.class)) {
                    throw new JOhmException(field.getType().getSimpleName() + " is an Attribute and a Model which is invalid");
                }
                checkValidAttribute(field);
            }
            if (isReference) {
                checkValidReference(field);
            }
        }
        
        public static boolean checkSupportedPrimitiveClazz(final Class<?> primitiveClazz) {
            return JOhmUtils.JOHM_SUPPORTED_PRIMITIVES.contains(primitiveClazz);
        }
    }
}
