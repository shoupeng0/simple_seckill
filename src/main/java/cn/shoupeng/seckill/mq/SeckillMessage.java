package cn.shoupeng.seckill.mq;


import lombok.Data;

@Data
public class SeckillMessage {
	private long userId;
	private long goodsId;

}
