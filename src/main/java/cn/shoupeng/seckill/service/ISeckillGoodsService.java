package cn.shoupeng.seckill.service;


import cn.shoupeng.seckill.entity.SeckillGoods;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ShouPeng
 * @since 2024-11-05
 */
public interface ISeckillGoodsService {

    List<SeckillGoods> getSeckillGoods();
    int reduceStock(long goodsId);
    SeckillGoods getseckillGoodsBoByGoodsId(long goodsId);
}
