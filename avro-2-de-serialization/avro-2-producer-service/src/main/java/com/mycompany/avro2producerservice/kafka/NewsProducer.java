package com.mycompany.avro2producerservice.kafka;

import com.mycompany.commons.avroserialization.avro.NewsMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class NewsProducer {

    private final KafkaTemplate<String, NewsMessage> kafkaTemplate;
    private final KafkaProperties kafkaProperties;

    public void send(NewsMessage newsMessage) {
        String kafkaTopic = kafkaProperties.getProducer().getProperties().get("topic");
        log.info("Sending News '{}' to topic '{}'", newsMessage, kafkaTopic);
        kafkaTemplate.send(kafkaTopic, String.valueOf(newsMessage.getFromId()), newsMessage);
    }
}
