package cn.shoupeng.seckill.service.impl;

import cn.shoupeng.seckill.entity.SeckillOrder;
import cn.shoupeng.seckill.mapper.SeckillOrderMapper;
import cn.shoupeng.seckill.service.ISeckillGoodsService;
import cn.shoupeng.seckill.service.ISeckillOrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ShouPeng
 * @since 2024-11-05
 */
@Service
@Slf4j
public class SeckillOrderServiceImpl implements ISeckillOrderService {
    @Autowired
    SeckillOrderMapper seckillOrderMapper;

    @Autowired
    ISeckillGoodsService seckillGoodsService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addOrder(long userId, long goodsId) {
        int success = seckillGoodsService.reduceStock(goodsId);
        if (success != 1){
            throw new RuntimeException("秒杀失败");
        }
        int insert = seckillOrderMapper.insert(new SeckillOrder(null, userId, System.currentTimeMillis(), goodsId));
        if (insert != 1){
            throw new RuntimeException("秒杀失败");
        }
//        log.info("{}:goodsId 秒杀成功",goodsId);
    }

    @Override
    public Boolean getSeckillOrderByUserIdGoodsId(long userId, long goodsId){
        return seckillOrderMapper.selectCount(new QueryWrapper<SeckillOrder>().eq("user_id",userId).eq("goods_id",goodsId)) > 0;
    }
}
