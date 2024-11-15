package cn.shoupeng.seckill.ratelimit;

import cn.shoupeng.seckill.anno.RateLimited;
import cn.shoupeng.seckill.exception.RateLimitedException;
import cn.shoupeng.seckill.utils.KEY;
import cn.shoupeng.seckill.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
public class IpRateLimitAspect {

    @Autowired
    private HttpServletRequest request;

    final  String script =
            "local key = KEYS[1] " +
                    "local limit = tonumber(ARGV[1]) " +
                    "local timeWindow = tonumber(ARGV[2]) " +
                    "local timeUnit = ARGV[3] " +
                    "local current_count = tonumber(redis.call('GET', key)) " +
                    "if current_count and current_count >= limit then " +
                    "   return -1 " +
                    "end " +
                    "if not current_count then " +
                    "   if timeUnit == 'SECONDS' then " +
                    "       redis.call('SET', key, 1, 'EX', timeWindow) " +
                    "   elseif timeUnit == 'MINUTES' then " +
                    "       redis.call('SET', key, 1, 'EX', timeWindow * 60) " +
                    "   elseif timeUnit == 'HOURS' then " +
                    "       redis.call('SET', key, 1, 'EX', timeWindow * 3600) " +
                    "   end " +
                    "else " +
                    "   redis.call('INCR', key) " +
                    "end " +
                    "return 1 ";

    @Pointcut("@annotation(cn.shoupeng.seckill.anno.RateLimited)") // 使用自定义注解进行限流
    public void rateLimitPointcut() {}

    @Before("rateLimitPointcut()")
    public void checkRateLimit(JoinPoint joinPoint) {
        String ip = getClientIp(request);
        String key = KEY.RATE_LIMITED + ip;
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        RateLimited rateLimited = method.getAnnotation(RateLimited.class);
        int limit = rateLimited.limit();
        int timeWindow = rateLimited.timeWindow();
        String timeUnit = rateLimited.timeUnit();

        Long result = RedisUtil.redisTemplate().execute(
                (RedisCallback<Long>) connection -> connection.eval(
                        script.getBytes(),
                        ReturnType.INTEGER,
                        1,
                        (key).getBytes(),
                        String.valueOf(limit).getBytes(), String.valueOf(timeWindow).getBytes(), timeUnit.getBytes()
                )
        );

        // 判断返回值，决定是否允许请求
        if (result != null && result == -1) {
            throw new RateLimitedException("请求过于频繁，请稍后再试");
        }

    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
