package com.dongfeng.study.controller;

import com.dongfeng.study.bean.base.BaseResponse;
import com.dongfeng.study.config.aop.flowlimit.ARateLimiter;
import com.dongfeng.study.config.aop.flowlimit.RedisLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author eastFeng
 * @date 2020/8/15 - 12:56
 */
@Slf4j
@RequestMapping("/limiterTest")
@RestController
public class LimiterController {

    /**
     * 3秒内允许请求10次
     */
    @RedisLimiter(value = "redisLimiterTest", timeout = 3)
    @GetMapping("/redisLimiterTest")
    public BaseResponse<String> redisLimiterTest(){
        log.info("-----redisLimiterTest start");
        for (int i=0; i<10; ++i){
            log.info("{}", i);
        }
        return BaseResponse.successInstance("【redisLimiterTest】被执行了...你不能总是看到我，快速刷新我看一下！");
    }

    /**
     * 一秒钟5个许可证
     */
    @ARateLimiter(value = "rateLimiterTest", qps=5.0)
    @GetMapping("rateLimiterTest")
    public BaseResponse<String> rateLimiterTest(){
        return BaseResponse.successInstance("【rateLimiterTest】被执行了...你不能总是看到我，快速刷新我看一下！");
    }

    public static void main(String[] args) {
        System.out.println("HHHH----------------------HHHH");
    }
}
