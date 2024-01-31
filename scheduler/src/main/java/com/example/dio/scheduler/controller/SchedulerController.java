package com.example.dio.scheduler.controller;

import com.example.dio.scheduler.service.ClientRouteScheduler;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class SchedulerController {

    private final ClientRouteScheduler clientRouteScheduler;

    @GetMapping("/collect")
    public void collect() {
        clientRouteScheduler.loadClientRoutes();
    }

}
