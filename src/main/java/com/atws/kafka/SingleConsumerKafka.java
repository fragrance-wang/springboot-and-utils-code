package com.atws.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * 一条条的消费kafka记录
 */
@Slf4j
@Component
public class SingleConsumerKafka {

//    @KafkaListener(id = "", topics = {ProducerKafkaController.topic}, groupId = "group.demo")
//    public void listener(String input) {
//        log.info("consumer value：{}", input);
//    }

    /**
     * 异步提交kafka消费信息
     */
//    @KafkaListener(id = "", topics = {ProducerKafkaController.topic}, groupId = "group.demo")
    public void listener(ConsumerRecord<?, ?> record, Consumer consumer) {
        // 消费的哪个topic、partition的消息,打印出消息内容
        System.out.println(String.format("消费：topic:%s-partition:%s-offset:%s-value:%s", record.topic(),record.partition(),record.offset(),record.value()));
        consumer.commitAsync();
    }
}
