package cn.shoupeng.seckill.controller;


import cn.shoupeng.seckill.anno.RateLimited;
import cn.shoupeng.seckill.entity.SeckillGoods;
import cn.shoupeng.seckill.mq.MQSender;
import cn.shoupeng.seckill.mq.SeckillMessage;
import cn.shoupeng.seckill.result.CodeMsg;
import cn.shoupeng.seckill.result.Result;
import cn.shoupeng.seckill.service.ISeckillGoodsService;
import cn.shoupeng.seckill.utils.KEY;
import cn.shoupeng.seckill.utils.RedisUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author ShouPeng
 * @since 2024-11-05
 */
@RestController
@RequestMapping("/seckill")
@DependsOn("ru")
@Slf4j
public class SeckillGoodsController implements InitializingBean{

    @Resource
    ISeckillGoodsService seckillGoodsService;

    @Resource
    MQSender sender;

    final Random random = new Random();

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("缓存预热...");
        List<SeckillGoods> seckillGoods = seckillGoodsService.getSeckillGoods();
        LocalDateTime now = LocalDateTime.now();
        for (SeckillGoods seckillGood : seckillGoods) {
            LocalDateTime s = seckillGood.getStartDate();
            LocalDateTime e = seckillGood.getEndDate();
            if (now.isAfter(s) && now.isBefore(e)){
                System.out.println("seckillGood:" + seckillGood.getGoodsId());
                RedisUtil.set(KEY.SECKILL_GOODS + seckillGood.getGoodsId(),seckillGood.getStockCount(), TimeUnit.SECONDS
                        , Duration.between(s, e).getSeconds());
            }
        }
    }

    private static final String STOCK_LUA_SCRIPT =
            "if tonumber(redis.call('get', KEYS[1])) >= tonumber(ARGV[1]) then " +
                    "   return redis.call('decrby', KEYS[1], ARGV[1]); " +
                    "else " +
                    "   return -1; " +
                    "end;";
    int i = 0;

    @RequestMapping("/db")
    @RateLimited(timeUnit = "SECONDS",timeWindow = 5,limit = 2)
    public Result seckilldb(@RequestParam("goodsId") int goodsId) {

        int userId = i++;
        String lockKey = KEY.SECKILL_LOCK + goodsId + ":" + userId;
        boolean lockAcquired = Boolean.TRUE.equals(RedisUtil.redisTemplate().opsForValue().setIfAbsent(lockKey, "", 15, TimeUnit.SECONDS));
        if (!lockAcquired) {
            return Result.error(CodeMsg.SECKILL_NO_LOCK);
        }

        //判断库存
        Long stock = RedisUtil.reduceStock(STOCK_LUA_SCRIPT, goodsId);
        if (ObjectUtils.isEmpty(stock) || stock <= 0) {
            return Result.error(CodeMsg.SECKILL_NO_GOODS);
        }

//        log.info(userId + "============= 预扣成功:" + stock );
        //入队
        SeckillMessage mm = new SeckillMessage();
        mm.setUserId(userId);
        mm.setGoodsId(goodsId);
        sender.sendSeckillMessage(mm);
        return Result.success(stock);
    }


}
