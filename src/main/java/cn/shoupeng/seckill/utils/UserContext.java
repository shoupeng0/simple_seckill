package cn.shoupeng.seckill.utils;

public class UserContext {
    private static final ThreadLocal<Integer> userIdThreadLocal = new ThreadLocal<>();

    public static void setUserId(int userId) {
        userIdThreadLocal.set(userId);
    }

    public static Integer getUserId() {
        return userIdThreadLocal.get();
    }

    public static void clear() {
        userIdThreadLocal.remove();
    }
}