package com.example.dio.scheduler.service;

import com.example.dio.scheduler.repository.ClientRouteRepository;
import com.example.dio.domain.ClientRoute;
import io.micrometer.observation.NullObservation;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class ClientRouteService {

    private final KafkaTemplate<String, ClientRoute> kafkaTemplate;
    private final NewTopic routeReferenceTopic;
    private final Tracer tracer;
    private final ObservationRegistry observationRegistry;
    private final ClientRouteRepository clientRouteRepository;

    public List<ClientRoute> findAll() {
        return clientRouteRepository.findAll();
    }

    public void sendToTopic(ClientRoute clientRoute) {

        // NullObservation to clear all the thread stuff and create a new observation from here
        new NullObservation(observationRegistry)
                .observe(() -> Observation.start("sendToTopic", observationRegistry)
                        .observe(() -> {
                            var message = new ProducerRecord<>(routeReferenceTopic.name(), routeReferenceTopic.numPartitions(), clientRoute.id(), clientRoute);
                            var traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
                            return kafkaTemplate.send(message).whenComplete((sr, ex) ->
                                    log.info("ClientRouteService sent a message with traceId={} and the payload={} for the topic={})",
                                            traceId, clientRoute, routeReferenceTopic.name()));
                        }));
    }

}
