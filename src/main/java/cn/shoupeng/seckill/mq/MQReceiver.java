package cn.shoupeng.seckill.mq;


import cn.shoupeng.seckill.entity.SeckillGoods;
import cn.shoupeng.seckill.entity.SeckillOrder;
import cn.shoupeng.seckill.service.IOrderInfoService;
import cn.shoupeng.seckill.service.ISeckillGoodsService;
import cn.shoupeng.seckill.service.ISeckillOrderService;
import cn.shoupeng.seckill.utils.KEY;
import cn.shoupeng.seckill.utils.RedisUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.K;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.*;

@Service
@Slf4j
public class MQReceiver {


    @Resource
    ISeckillGoodsService goodsService;


    @Resource
    ISeckillOrderService seckillOrderService;

    @RabbitListener(queues = MQConfig.SECKILL_QUEUE, concurrency = "1")
    public void receive(String message) {
        SeckillMessage mm = JSON.parseObject(message, SeckillMessage.class);
        long goodsId = mm.getGoodsId();

//        SeckillGoods goods = goodsService.getseckillGoodsBoByGoodsId(goodsId);
//        int stock = goods.getStockCount();
//        if (stock <= 0) {
//                log.info("{}库存不足",mm.getUserId());
//            return;
//        }

        //判断是否已经秒杀到了
        Boolean isSeckill = seckillOrderService.getSeckillOrderByUserIdGoodsId(mm.getUserId(), goodsId);
        if (isSeckill){
            recover(null,message);
            return;
        }

//        String key = KEY.SECKILLED + ":" + mm.getUserId() + ":" + goodsId;
//        if (ObjectUtils.isNotEmpty(RedisUtil.get(key))) {
//            log.info(key);
//            recover(null, message);
//            return;
//        }
        try {
            seckillOrderService.addOrder(mm.getUserId(), goodsId);
        }catch (Exception e){
            recover(null,message);
        }

//        RedisUtil.set(key,"",TimeUnit.MINUTES,1);

//            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
    }

    @Resource
    ExecutorService mqTreadPoll;

    public void recover(Exception e, String message) {
        mqTreadPoll.submit(() -> {
            SeckillMessage mm = JSON.parseObject(message, SeckillMessage.class);
            SeckillGoods seckillGoods = goodsService.getseckillGoodsBoByGoodsId(mm.getGoodsId());
            log.info("回流库存:{}", seckillGoods.getStockCount());
            String key = KEY.SECKILL_GOODS + mm.getGoodsId();
            RedisUtil.set(key, seckillGoods.getStockCount(), TimeUnit.SECONDS, RedisUtil.getExpire(key));
//            log.info("回流库存");
//            SeckillMessage mm = JSON.parseObject(message, SeckillMessage.class);
//            String key = KEY.SECKILL_GOODS + mm.getGoodsId();
//            RedisUtil.incr(key);
        });
    }

}
