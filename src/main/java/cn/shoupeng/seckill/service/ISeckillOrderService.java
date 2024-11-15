package cn.shoupeng.seckill.service;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ShouPeng
 * @since 2024-11-05
 */
public interface ISeckillOrderService {

    void addOrder(long userId, long goodsId);

    Boolean getSeckillOrderByUserIdGoodsId(long userId,long goodsId);

}
