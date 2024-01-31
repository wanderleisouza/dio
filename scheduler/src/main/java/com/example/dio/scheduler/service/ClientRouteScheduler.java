package com.example.dio.scheduler.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@EnableScheduling
@Service
@AllArgsConstructor
@Slf4j
public class ClientRouteScheduler {

    private static final String CRON_TO_LOAD_CLIENT_ROUTES = "0 */1 9-18 ? * MON-FRI";
    private final ClientRouteService clientRouteService;

    //@Scheduled(cron = CRON_TO_LOAD_CLIENT_ROUTES)
    public void loadClientRoutes() {
       var clientRoutes = clientRouteService.findAll();
       log.info("Process started, loaded {} client routes", clientRoutes.size());
       clientRoutes.parallelStream().forEach(clientRouteService::sendToTopic);
    }

}
