package com.example.dio.domain;

import com.example.dio.collector.config.KafkaTopicConfig;
import lombok.Getter;

@Getter
public enum ProcessingType {

    INDIVIDUAL(KafkaTopicConfig.ROUTE_FILES_REFERENCE_TOPIC),
    GROUP(KafkaTopicConfig.ROUTE_FILES_GROUP_REFERENCE_TOPIC);

    private final String topicName;

    private ProcessingType(String topicName) {
        this.topicName = topicName;
    }

}
