package cn.shoupeng.seckill.mq;

import org.checkerframework.checker.units.qual.A;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MQConfig {

	public static final String SECKILL_QUEUE = "seckill.queue";
	public static final String SECKILL_EXCHANGE = "seckill.exchange";

	@Bean
	public MessageConverter getMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public DirectExchange seckillExchange() {
		return new DirectExchange(SECKILL_EXCHANGE, true, false);
	}

	@Bean
	public Queue seckillQueue() {
		return new Queue(SECKILL_QUEUE, true);
	}

	// 绑定队列和交换机
	@Bean
	public Binding binding(@Autowired Queue queue, @Autowired DirectExchange directExchange) {
		return BindingBuilder.bind(queue).to(directExchange).with("seckill");
	}

	@Bean
	@Primary
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(getMessageConverter());
		// 启用消息确认机制
		rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
			if (ack) {
				// 消息发送成功
				System.out.println("消息发送成功，ID：" + (correlationData != null ? correlationData.getId() : "null"));
			} else {
				// 消息发送失败，记录日志
				System.err.println("消息发送失败，原因：" + cause + "，ID：" + (correlationData != null ? correlationData.getId() : "null"));
				// 可以在此处进行消息重发或其他处理
			}
		});
		return rabbitTemplate;
	}

}
