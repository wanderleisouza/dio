package com.example.dio.collector.listener;

import com.example.dio.collector.config.KafkaConsumerConfig;
import com.example.dio.collector.config.KafkaTopicConfig;
import com.example.dio.collector.service.ClientRouteAggregateService;
import com.example.dio.domain.ClientRoute;
import com.example.dio.collector.service.ScanRouteService;
import com.example.dio.domain.ClientRouteAggregate;
import com.example.dio.domain.ProcessingType;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientRouteListener {

    private final ScanRouteService scanRouteService;
    private final Tracer tracer;
    private final ClientRouteAggregateService clientRouteAggregateService;
    private static final String CONTAINER_FACTORY = "clientRouteKafkaListenerContainerFactory";

    @KafkaListener(groupId = KafkaConsumerConfig.GROUP_ID, topics = KafkaTopicConfig.ROUTE_REFERENCE_TOPIC, containerFactory = CONTAINER_FACTORY)
    public void readFromTopic(@Payload ClientRoute clientRoute) {
        var traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
        log.info("ClientRouteListener received a message with traceId={} and the payload={})", traceId, clientRoute);

        if (clientRoute.processingType().equals(ProcessingType.GROUP)) {
            clientRouteAggregateService.sendToTopic(new ClientRouteAggregate(clientRoute, scanRouteService.listFiles(clientRoute)));
        } else {
            scanRouteService.listFiles(clientRoute).parallelStream().forEach(file -> {
                var listSingleElement = new ArrayList<Path>();
                listSingleElement.add(file);
                clientRouteAggregateService.sendToTopic(new ClientRouteAggregate(clientRoute, listSingleElement));
            });
        }

    }

}
