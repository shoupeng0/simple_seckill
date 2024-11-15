package cn.shoupeng.seckill.mq;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class MQSender {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendSeckillMessage(SeckillMessage seckillMessage) {
        String msg = JSON.toJSONString(seckillMessage);
        rabbitTemplate.convertAndSend(MQConfig.SECKILL_EXCHANGE,
                "seckill",msg,new CorrelationData(UUID.randomUUID().toString().substring(0,8)));
    }

}
