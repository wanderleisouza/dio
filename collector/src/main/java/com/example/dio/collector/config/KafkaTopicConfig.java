package com.example.dio.collector.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String ROUTE_REFERENCE_TOPIC = "route_reference_loaded";
    public static final String ROUTE_FILES_REFERENCE_TOPIC = "route_files_reference_loaded";
    public static final String ROUTE_FILES_GROUP_REFERENCE_TOPIC = "route_files_group_reference_loaded";

    public static final int NUM_OF_PARTITIONS = 0;
    private static final int NUM_OF_REPLICAS = 0;

    @Bean
    public NewTopic routeReferenceTopic() {
        return TopicBuilder.name(ROUTE_REFERENCE_TOPIC)
            .partitions(NUM_OF_PARTITIONS)
            .replicas(NUM_OF_REPLICAS)
            .build();
    }

    @Bean
    public NewTopic routeFilesReferenceTopic() {
        return TopicBuilder.name(ROUTE_FILES_REFERENCE_TOPIC)
            .partitions(NUM_OF_PARTITIONS)
            .replicas(NUM_OF_REPLICAS)
            .build();
    }

    @Bean
    public NewTopic routeFilesGroupReferenceTopic() {
        return TopicBuilder.name(ROUTE_FILES_GROUP_REFERENCE_TOPIC)
            .partitions(NUM_OF_PARTITIONS)
            .replicas(NUM_OF_REPLICAS)
            .build();
    }

}
