package com.atws.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
public class ProducerKafkaController {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public static final String topic = "topic-ws";

    /**
     * 同步阻塞
     * 需要特别注意的是： future.get()方法会阻塞，他会一直尝试获取发送结果，如果Kafka迟迟没有返回发送结果那么程序会阻塞到这里。所以这种发送方式是同步的。
     * 当然如果你的消息不重要允许丢失你也可以直接执行 ： kafkaTemplate.send ，不调用get()方法获取发送结果，程序就不会阻塞，当然你也就不知道消息到底有没有发送成功。
     *
     */
    @Transactional(rollbackFor = Exception.class)//支持事务
    @GetMapping("/send/{msg}")
    public String sendMsgSync(@PathVariable String msg) throws ExecutionException, InterruptedException {
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, 0, "my-key", msg);
        log.info("producer result: {}", future.get());
        return "sent success msg:" + msg;
    }

    /**
     * Callback异步回调，我们可以通过异步回调来接收发送结果
     */
    @GetMapping("/send/async/{msg}")
    public String sendMsgASyn(@PathVariable("msg")String msg) {

        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, msg);

        future.addCallback(new ListenableFutureCallback(){

            @Override
            public void onSuccess(Object result) {
                System.out.println("发送成功 result = "+result);
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("发送异常");
                ex.printStackTrace();
            }
        });

        return "发送成功";
    }

}
