package com.atws.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BatchConsumerFactoryConfig {


    @Autowired
    private ConsumerFactory<String, Object> consumerFactory;

    /**
     *  消费者批量工厂
     */
    @Bean
    public KafkaListenerContainerFactory<?> batchFactory() {
        Map<String, Object> properties = consumerFactory.getConfigurationProperties();
        System.err.println("properties:"+ properties);
        //消费者参数配置
        Map<String, Object> map = new HashMap<>();
        map.put(ConsumerConfig.GROUP_ID_CONFIG, properties.get(ConsumerConfig.GROUP_ID_CONFIG));
        map.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, properties.get(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG));//earliest
        map.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.get(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
        map.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, properties.get(ConsumerConfig.MAX_POLL_RECORDS_CONFIG));//批量消费，每次最多消费多少条数据
        map.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, properties.get(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG));
        map.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, properties.get(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG));//消费会话超时时间(超过这个时间consumer没有发送心跳,就会触发rebalance操作)
        map.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, properties.get(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG));//消费请求超时时间
        map.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, properties.get(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG));
        map.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, properties.get(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG));
        System.err.println(map);
        ConcurrentKafkaListenerContainerFactory<Integer, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(map));
        //设置并发量，小于或等于Topic的分区数,根据实际情况设定
//        factory.setConcurrency(batchConcurrency);
        factory.getContainerProperties().setPollTimeout(1500);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        //设置为批量消费，每个批次数量在Kafka配置参数中设置ConsumerConfig.MAX_POLL_RECORDS_CONFIG
        factory.setBatchListener(true);
        return factory;
    }
}
