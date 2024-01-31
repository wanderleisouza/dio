package com.example.dio.collector.listener;

import com.example.dio.collector.config.KafkaConsumerConfig;
import com.example.dio.collector.service.ClientRouteAggregateService;
import com.example.dio.collector.service.CopyFilesService;
import com.example.dio.domain.ClientRouteAggregate;
import com.example.dio.domain.ProcessingType;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

import static com.example.dio.collector.config.KafkaTopicConfig.ROUTE_FILES_GROUP_REFERENCE_TOPIC;
import static com.example.dio.collector.config.KafkaTopicConfig.ROUTE_FILES_REFERENCE_TOPIC;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientRouteAggregateListener {

    private final Tracer tracer;
    private final CopyFilesService copyFilesService;
    private static final String CONTAINER_FACTORY = "clientRouteAggregateKafkaListenerContainerFactory";

    @KafkaListener(groupId = KafkaConsumerConfig.GROUP_ID,
                   topics = {ROUTE_FILES_REFERENCE_TOPIC, ROUTE_FILES_GROUP_REFERENCE_TOPIC}, containerFactory = CONTAINER_FACTORY)
    public void readFromTopic(@Payload ClientRouteAggregate clientRouteAggregate) {
        var traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
        log.info("ClientRouteAggregateListener received a message with traceId={} for the routeId={} (with {} files to copy, processingType={})",
            traceId, clientRouteAggregate.clientRoute().id(), clientRouteAggregate.files().size(), clientRouteAggregate.clientRoute().processingType());
        copyFilesInParallel(clientRouteAggregate, traceId);
    }

    public void copyFilesInParallel(ClientRouteAggregate clientRouteAggregate, String traceId) {

        log.info("Parallel copy started using {} available processors, processingType={}",
            numberOfCores(), clientRouteAggregate.clientRoute().processingType());

        var destination = clientRouteAggregate.clientRoute().targetFolder();

        if (clientRouteAggregate.clientRoute().processingType().equals(ProcessingType.GROUP)) {
            try (ForkJoinPool executor = ForkJoinPool.commonPool()) {
                clientRouteAggregate.files().parallelStream()
                    .forEach(file -> executor.submit(() -> copyFilesService.copyFile(file, destination, clientRouteAggregate.clientRoute(), traceId)).join());
            }
        } else {
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                executor.submit(()
                    -> copyFilesService.copyFile(clientRouteAggregate.files().getFirst(), destination, clientRouteAggregate.clientRoute(), traceId));
            }
        }

        log.info("Finished copy for client={} in routeId={}, {} file(s), using processingType={} for more info traceId={}",
            clientRouteAggregate.clientRoute().company(),clientRouteAggregate.clientRoute().id(),
            clientRouteAggregate.files().size(), clientRouteAggregate.clientRoute().processingType(), traceId);

    }

    static int numberOfCores() {
        return Runtime.getRuntime().availableProcessors();
    }

}
