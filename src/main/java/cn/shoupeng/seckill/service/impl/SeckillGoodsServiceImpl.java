package cn.shoupeng.seckill.service.impl;


import cn.shoupeng.seckill.entity.SeckillGoods;
import cn.shoupeng.seckill.mapper.SeckillGoodsMapper;
import cn.shoupeng.seckill.service.ISeckillGoodsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ShouPeng
 * @since 2024-11-05
 */
@Service
public class SeckillGoodsServiceImpl  implements ISeckillGoodsService {
    @Resource
    SeckillGoodsMapper seckillGoodsMapper;

    @Override
    public List<SeckillGoods> getSeckillGoods() {
        return seckillGoodsMapper.selectList(null);
    }

    @Override
    public int reduceStock(long goodsId) {
        return seckillGoodsMapper.reduceStock(goodsId);
    }

    @Override
    public SeckillGoods getseckillGoodsBoByGoodsId(long goodsId) {
        return seckillGoodsMapper.selectOne(new QueryWrapper<SeckillGoods>().eq("id",goodsId));
    }
}
