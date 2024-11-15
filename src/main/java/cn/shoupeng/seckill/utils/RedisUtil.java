package cn.shoupeng.seckill.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Component("ru")
@DependsOn("rd")
public class RedisUtil implements InitializingBean {
    private static RedisTemplate<String, Object> redisTemplate = null;

    @Autowired
    private RedisTemplate<String, Object> redisTemplateAutowired;

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("==========");
        System.out.println(redisTemplateAutowired);
        redisTemplate = redisTemplateAutowired;
    }

    /**
     * 是否存在key
     * @param key 查询的key
     * @return b or f
     */
    public static boolean hasKey(String key){
        return redisTemplate.hasKey(key);
    }

    public static Long ttl(String key){
        return redisTemplate.getExpire(key,TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     *
     * @param key Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public static boolean expire(final String key, final long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    public static void incr(String key){
        redisTemplate.opsForValue().increment(key);

    }

    public static long getExpire(String key){
        Long expire = redisTemplate.getExpire(key);
        return expire == null ? 0 : expire;
    }

    public static Long decr(String key){
        return redisTemplate.opsForValue().decrement(key);
    }

    public static void decr(String key,long num){
        redisTemplate.opsForValue().decrement(key,num);
    }

    public static Boolean hHasKey(String key,String hashKey){
        return redisTemplate.opsForHash().hasKey(key,hashKey);
    }

    public static void hIncr(String key,String hashKey,Integer delta){
        redisTemplate.opsForHash().increment(key,hashKey,delta);
    }

         //    public static Boolean hExpire(String key,String hashKey){
         //        return redisTemplate;
         //    }

    public static void incrBy(String key,Long l){
        redisTemplate.opsForValue().increment(key,l);
    }

    /**
     * 设置有效时间
     *
     * @param key Redis键
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return true=设置成功；false=设置失败
     */
    public static boolean expire(final String key, final long timeout, final TimeUnit unit) {

        Boolean ret = redisTemplate.expire(key, timeout, unit);
        return ret != null && ret;
    }

    /**
     * 删除单个key
     *
     * @param key 键
     * @return true=删除成功；false=删除失败
     */
    public static boolean del(final String key) {

        Boolean ret = redisTemplate.delete(key);
        return ret != null && ret;
    }

    /**
     * 删除多个key
     *
     * @param keys 键集合
     * @return 成功删除的个数
     */
    public static long del(final String... keys) {
        Long ret = redisTemplate.delete(Arrays.asList(keys));
        return ret == null ? 0 : ret;
    }

    /**
     * 存入普通对象
     *
     * @param key Redis键
     * @param value 值
     */
    public static void set(final String key, final Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

         // 存储普通对象操作

    /**
     * 存入普通对象
     *
     * @param key 键
     * @param value 值
     * @param timeout 有效期，单位秒
     */
    public static void set(final String key, final Object value, final long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }


    /**
     * 存入普通对象
     *
     * @param key 键
     * @param value 值
     * @param timeout 有效期，单位秒
     */
    public static void set(final String key, final Object value,TimeUnit timeUnit, final long timeout) {
        redisTemplate.opsForValue().set(key, value,timeout,timeUnit);
    }

    /**
     * 获取普通对象
     *
     * @param key 键
     * @return 对象
     */
    public static Object get(final String key) {
        return redisTemplate.opsForValue().get(key);
    }

         // 存储Hash操作

    /**
     * 往Hash中存入数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @param value 值
     */
    public static void hPut(final String key, final String hKey, final Object value) {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * 往Hash中存入多个数据
     *
     * @param key Redis键
     * @param values Hash键值对
     */
    public static void hPutAll(final String key, final Map<String, Object> values) {
        redisTemplate.opsForHash().putAll(key, values);
    }

    /**
     * 往Hash中存入数据
     *
     * @param key Redis键
     * @param hKey Hash键
     */
    public static void hDelete(final String key, final String... hKey) {
        Long delete = redisTemplate.opsForHash().delete(key, hKey);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public static Object hGet(final String key, final String hKey) {
        return redisTemplate.opsForHash().get(key, hKey);
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public static List<Object> hMultiGet(final String key, final Collection<Object> hKeys) {

        return redisTemplate.opsForHash().multiGet(key, hKeys);
    }

         // 存储Set相关操作

    /**
     * 往Set中存入数据
     *
     * @param key Redis键
     * @param values 值
     * @return 存入的个数
     */
    public static long sSet(final String key, final Object... values) {
        Long count = redisTemplate.opsForSet().add(key, values);
        return count == null ? 0 : count;
    }

    /**
     * 删除Set中的数据
     *
     * @param key Redis键
     * @param values 值
     * @return 移除的个数
     */
    public static long sDel(final String key, final Object... values) {
        Long count = redisTemplate.opsForSet().remove(key, values);
        return count == null ? 0 : count;
    }

         // 存储List相关操作

    /**
     * 往List中存入数据
     *
     * @param key Redis键
     * @param value 数据
     * @return 存入的个数
     */
    public static long lPush(final String key, final Object value) {
        Long count = redisTemplate.opsForList().rightPush(key, value);
        return count == null ? 0 : count;
    }

    /**
     * 往List中存入多个数据
     *
     * @param key Redis键
     * @param values 多个数据
     * @return 存入的个数
     */
    public static long lPushAll(final String key, final Collection<Object> values) {
        Long count = redisTemplate.opsForList().rightPushAll(key, values);
        return count == null ? 0 : count;
    }

    /**
     * 往List中存入多个数据
     *
     * @param key Redis键
     * @param values 多个数据
     * @return 存入的个数
     */
    public static long lPushAll(final String key, final Object... values) {
        Long count = redisTemplate.opsForList().rightPushAll(key, values);
        return count == null ? 0 : count;
    }

    /**
     * 从List中获取begin到end之间的元素
     *
     * @param key Redis键
     * @param start 开始位置
     * @param end 结束位置（start=0，end=-1表示获取全部元素）
     * @return List对象
     */
    public static List<Object> lGet(final String key, final int start, final int end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 扣减库存操作
     * @param goodsId 商品ID
     * @return 扣减后的库存，如果库存不足返回 -1
     */
    public static Long reduceStock(String lua,int goodsId) {
        return redisTemplate.execute(
                (RedisCallback<Long>) connection -> connection.eval(
                        lua.getBytes(),
                        ReturnType.INTEGER,
                        1,
                        (KEY.SECKILL_GOODS + goodsId).getBytes(),
                        String.valueOf(1).getBytes()
                )
        );
    }

    public static RedisTemplate<String, Object> redisTemplate() {
        return redisTemplate;
    }

}
