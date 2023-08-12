package com.atws.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wangshan
 * @date 2023-08-09 22:55
 */
@RestController
public class SemaphoreTestController {

    public volatile Semaphore semaphore = new Semaphore(2);

    public AtomicInteger integer = new AtomicInteger(0);

    /**
     * semaphore限流测试
     * @return
     */
    @RequestMapping("/limit/test")
    public String xianLiu(){
        try {

            semaphore.acquire();
            integer.addAndGet(1);
            System.out.println("integer: "+integer.get());
            String name = Thread.currentThread().getName();
//            System.out.println(name+" running");
            Thread.sleep(10000);
//            System.out.println(name+" end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            semaphore.release();
            integer.decrementAndGet();
        }

        return "success";
    }

}
