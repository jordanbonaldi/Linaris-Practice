package net.linaris.Practice.database.redis.orm;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Attribute {
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    
    String date() default "yyyy-MM-dd";
}
