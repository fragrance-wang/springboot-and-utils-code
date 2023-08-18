package com.atws.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.callback.Callback;

@Slf4j
@RestController
public class ProducerKafkaController {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public static final String topic = "topic-ws";

    @GetMapping("/send/{input}")
    public String sendMsg(@PathVariable String input) {
        kafkaTemplate.send(topic, 0, "my-key", input);
        log.info("producer msg: {}", input);
        return "msg:" + input;
    }
}
