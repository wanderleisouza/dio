package com.example.dio.collector.config;

import com.example.dio.domain.ClientRoute;
import com.example.dio.domain.ClientRouteAggregate;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableKafka
public class KafkaConsumerConfig {

    public static final String GROUP_ID = "group-1";

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public ConsumerFactory<String, ClientRoute> clientRouteConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(getKafkaProps());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ClientRoute> clientRouteKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ClientRoute> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(clientRouteConsumerFactory());
        factory.getContainerProperties().setObservationEnabled(true);
        return factory;
    }

    public ConsumerFactory<String, ClientRouteAggregate> clientRouteAggregateConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(getKafkaProps(), new StringDeserializer(), new JsonDeserializer<>(ClientRouteAggregate.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ClientRouteAggregate> clientRouteAggregateKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ClientRouteAggregate> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(clientRouteAggregateConsumerFactory());
        factory.getContainerProperties().setObservationEnabled(true);
        return factory;
    }

    private Map<String, Object> getKafkaProps() {
        var props = new HashMap<String, Object>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return props;
    }

}
