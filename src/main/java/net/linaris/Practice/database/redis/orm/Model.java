package net.linaris.Practice.database.redis.orm;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Model {
    String value() default "";
}
