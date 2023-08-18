package com.atws.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConsumerKafka {

    @KafkaListener(id = "", topics = {ProducerKafkaController.topic}, groupId = "group.demo")
    public void listener(String input) {
        log.info("consumer value：{}", input);
    }
}
