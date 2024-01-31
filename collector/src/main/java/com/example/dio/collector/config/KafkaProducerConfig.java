package com.example.dio.collector.config;

import com.example.dio.domain.ClientRoute;
import com.example.dio.domain.ClientRouteAggregate;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;


import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableKafka
public class KafkaProducerConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public ProducerFactory<String, ClientRoute> clientRouteProducerFactory() {
        return new DefaultKafkaProducerFactory<>(getKafkaProps());
    }

    @Bean
    public KafkaTemplate<String, ClientRoute> clientRouteKafkaTemplate() {
        var kafkaTemplate =  new KafkaTemplate<>(clientRouteProducerFactory());
        kafkaTemplate.setObservationEnabled(true);
        return kafkaTemplate;
    }

    @Bean
    public ProducerFactory<String, ClientRouteAggregate> clientRouteAggregateProducerFactory() {
        return new DefaultKafkaProducerFactory<>(getKafkaProps());
    }

    @Bean
    public KafkaTemplate<String, ClientRouteAggregate> clientRouteAggregateKafkaTemplate() {
        var kafkaTemplate = new KafkaTemplate<>(clientRouteAggregateProducerFactory());
        kafkaTemplate.setObservationEnabled(true);
        return kafkaTemplate;
    }

    private Map<String, Object> getKafkaProps() {
        var props = new HashMap<String, Object>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

}
