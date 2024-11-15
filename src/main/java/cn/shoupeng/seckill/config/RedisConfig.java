package cn.shoupeng.seckill.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

/**
 * @author spp
 * @date 2020-09-18 09:39
 **/
@Configuration
@ConfigurationProperties(prefix = "spring.redis.cluster")
@Data
public class RedisConfig {
    private List<String> nodes;

    @Bean("lcf")
    @Primary
    public RedisConnectionFactory redisConnectionFactory() {
        RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration(nodes);
        return new LettuceConnectionFactory(clusterConfiguration);
    }

    /**
     * 获取缓存操作助手对象
     *
     * @return
     */
    @Bean("rd")
    public RedisTemplate<String, Object> redisTemplate(@Qualifier("lcf") RedisConnectionFactory factory) {
        System.out.println("============");
        //创建Redis缓存操作助手RedisTemplate对象
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        //采用fastjson来序列化
        redisTemplate.setDefaultSerializer(new GenericFastJsonRedisSerializer());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
