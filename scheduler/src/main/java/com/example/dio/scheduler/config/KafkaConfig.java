package com.example.dio.scheduler.config;

import com.example.dio.domain.ClientRoute;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@RequiredArgsConstructor
@EnableKafka
public class KafkaConfig {

    public static final String ROUTE_REFERENCE_TOPIC = "route_reference_loaded";
    private final KafkaTemplate<String, ClientRoute> kafkaTemplate;

    @Bean
    public NewTopic routeReferenceTopic() {
        return TopicBuilder.name(ROUTE_REFERENCE_TOPIC)
            .partitions(0)
            .replicas(0)
            .build();
    }

    @PostConstruct
    void setup() {
        this.kafkaTemplate.setObservationEnabled(true);
    }

}
