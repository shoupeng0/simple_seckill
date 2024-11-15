package cn.shoupeng.seckill.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimited {
    int limit() default 3;
    int timeWindow() default 1;

    //SECONDS,MINUTES,HOURS
    String timeUnit() default "MINUTES";
}
