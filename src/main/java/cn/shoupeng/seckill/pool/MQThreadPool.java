package cn.shoupeng.seckill.pool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @author ShouPeng
 * @description
 * @since 2024/11/5
 */
@Configuration
public class MQThreadPool {

    @Bean
    public ExecutorService createThreadPool() {
        // 核心池大小（线程池最小线程数）
        int corePoolSize = Runtime.getRuntime().availableProcessors() + 1;

        // 最大池大小（线程池最大线程数）
        int maximumPoolSize = corePoolSize + 5;

        // 空闲线程存活时间
        long keepAliveTime = 120L;

        // 时间单位
        TimeUnit timeUnit = TimeUnit.SECONDS;

        // 阻塞队列，存放待处理的任务
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(2000);

        // 创建线程池
        return new ThreadPoolExecutor(
                corePoolSize,       // 核心池大小
                maximumPoolSize,    // 最大池大小
                keepAliveTime,      // 线程空闲时的存活时间
                timeUnit,           // 时间单位
                workQueue,          // 阻塞队列
                new ThreadPoolExecutor.AbortPolicy() // 拒绝策略：超出最大线程数时抛出异常
        );
    }

}
