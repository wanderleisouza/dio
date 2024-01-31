package com.example.dio.collector.service;

import com.example.dio.domain.ClientRouteAggregate;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.example.dio.collector.config.KafkaTopicConfig.NUM_OF_PARTITIONS;
import static com.example.dio.collector.config.KafkaTopicConfig.ROUTE_FILES_REFERENCE_TOPIC;

@Service
@AllArgsConstructor
@Slf4j
public class ClientRouteAggregateService {

    private final KafkaTemplate<String, ClientRouteAggregate> kafkaTemplate;
    private final Tracer tracer;
    private final ObservationRegistry observationRegistry;

    public void sendToTopic(ClientRouteAggregate clientRouteAggregate) {

        Observation.createNotStarted("sendToTopic", observationRegistry).observeChecked(() -> {
            var topicName = clientRouteAggregate.clientRoute().processingType().getTopicName();
            var message = new ProducerRecord<>(topicName, NUM_OF_PARTITIONS, clientRouteAggregate.clientRoute().id(), clientRouteAggregate);
            var traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
            return kafkaTemplate.send(message).whenComplete((sr, ex) ->
                log.info("ClientRouteAggregateService sent a message with traceId={} for the routeId={} (with {} files to copy) to the topic={})",
                    traceId, clientRouteAggregate.clientRoute().id(), clientRouteAggregate.files().size(), topicName));
        });

    }

}
