package cn.shoupeng.seckill.exception;

/**
 * @author ShouPeng
 * @description
 * @since 2024/11/5
 */
public class RateLimitedException extends RuntimeException{

    public RateLimitedException(String msg){
        super(msg);
    }

}
