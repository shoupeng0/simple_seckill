package cn.shoupeng.seckill.mapper;


import cn.shoupeng.seckill.entity.SeckillGoods;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ShouPeng
 * @since 2024-11-05
 */
public interface SeckillGoodsMapper extends BaseMapper<SeckillGoods> {

    @Update("UPDATE seckill_goods " +
            "SET stock_count = stock_count - 1 " +
            "WHERE goods_id = #{goods_id} " +
            "AND stock_count > 0")
    int reduceStock(Long goods_id);

}
