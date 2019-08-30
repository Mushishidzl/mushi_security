package org.mushi.test;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 目标：编写一段代码，模拟并发请求为20，且总的请求数为1000，当1000个请求完成后，打印“请求完成”
 */
@Slf4j
public class TestConcurrentRequest {

    // 总的请求个数
    public static final int requestTotal = 10000;

    // 同一时刻最大的并发线程的个数
    public static final int concurrentThreadNum = 50;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        CountDownLatch countDownLatch = new CountDownLatch(requestTotal);
        Semaphore semaphore = new Semaphore(concurrentThreadNum);
        for (int i = 0; i < requestTotal; i++) {
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    String result = testRequestUri();
                    log.info("result:{}.", result);
                    semaphore.release();
                } catch (InterruptedException e) {
                    log.error("exception", e);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        log.info("请求完成");
    }

    private static String testRequestUri() {
        Map<String, String> param = new HashMap<>();
        param.put("mobile","18401675279");
        return HttpClientUtil.doGet("http://127.0.0.1:8083/code/sms",param);
    }
}