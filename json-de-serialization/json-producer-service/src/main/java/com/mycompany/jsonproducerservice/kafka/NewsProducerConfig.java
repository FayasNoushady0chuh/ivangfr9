package com.mycompany.jsonproducerservice.kafka;

import com.mycompany.jsonproducerservice.kafka.event.News;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class NewsProducerConfig {

    private final KafkaProperties kafkaProperties;

    @Bean
    KafkaTemplate<String, News> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    ProducerFactory<String, News> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    Map<String, Object> producerConfigs() {
        Map<String, Object> props = kafkaProperties.buildProducerProperties();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return props;
    }

    // As the application will create a topic in Kafka, it is better to update the KafkaAdmin's BOOTSTRAP_SERVERS_CONFIG
    // property with the bootstrap servers' url configured, otherwise it will get the default 'localhost:9082'
    @Bean
    KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        return new KafkaAdmin(configs);
    }

    @Bean
    NewTopic newTopic() {
        Map<String, String> producerProperties = kafkaProperties.getProducer().getProperties();
        return new NewTopic(producerProperties.get("topic"), Integer.parseInt(producerProperties.get("num-partitions")), (short) 1);
    }
}
