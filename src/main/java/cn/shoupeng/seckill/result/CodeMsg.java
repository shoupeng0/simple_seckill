package cn.shoupeng.seckill.result;
/**
 * My Blog : www.hfbin.cn
 * github: https://github.com/hfbin
 * Created by: HuangFuBin
 * Date: 2018/6/09
 * Time: 12:00
 * Such description:
 */
public class CodeMsg {
	
	private int code;
	private String msg;
	
	//通用的错误码
	public static CodeMsg SUCCESS = new CodeMsg(0, "success");
	public static CodeMsg ACCESS_LIMIT= new CodeMsg(500104, "访问频繁");

	public static CodeMsg SECKILL_NO_GOODS = new CodeMsg(500100, "非秒杀商品");
	public static CodeMsg SECKILL_NO_LOCK = new CodeMsg(500101, "秒杀进行中，请勿重复提交!");

	public static CodeMsg ORDER_NOT_EXIST = new CodeMsg(500400, "订单不存在");

	public static CodeMsg SECKILL_OVER = new CodeMsg(500500, "商品已经秒杀完毕");
	public static CodeMsg SECKILL_OVER_STOCK = new CodeMsg(500500, "库存不够");

	public static CodeMsg SECKILLED = new CodeMsg(500501, "不能重复秒杀");
	private CodeMsg( ) {
	}
			
	private CodeMsg( int code,String msg ) {
		this.code = code;
		this.msg = msg;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public CodeMsg fillArgs(Object... args) {
		int code = this.code;
		String message = String.format(this.msg, args);
		return new CodeMsg(code, message);
	}

	@Override
	public String toString() {
		return "CodeMsg [code=" + code + ", msg=" + msg + "]";
	}
	
	
}
