package com.geolocalizacion.challenge.controller;

import com.geolocalizacion.challenge.service.StatisticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/statistics/average-distance")
    public String getAverageDistance() {
        return statisticsService.getAverageDistance();
    }

    @GetMapping("/statistics/max-distance")
    public String getMaxDistance() {
        return statisticsService.getMaxDistance();
    }

    @GetMapping("/statistics/min-distance")
    public String getMinDistance() {
        return statisticsService.getMinDistance();
    }
}