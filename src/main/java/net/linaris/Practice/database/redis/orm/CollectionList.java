package net.linaris.Practice.database.redis.orm;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface CollectionList {
    Class<?> of();
}
