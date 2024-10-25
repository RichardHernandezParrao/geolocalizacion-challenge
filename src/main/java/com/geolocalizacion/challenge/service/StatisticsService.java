package com.geolocalizacion.challenge.service;

import com.geolocalizacion.challenge.model.dto.DistanceStatistic;
import com.geolocalizacion.challenge.repository.StatisticsRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;

    public StatisticsService(StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    public void recordDistance(String country, double distance) {
        List<DistanceStatistic> statistics = statisticsRepository.findAll();
        Optional<DistanceStatistic> existingStat = statistics.stream()
                .filter(stat -> stat.getCountry().equalsIgnoreCase(country))
                .findFirst();

        if (existingStat.isPresent()) {
            DistanceStatistic stat = existingStat.get();
            stat.setDistance(distance);
            stat.setInvocations(stat.getInvocations() + 1);
            statisticsRepository.save(stat);
        } else {
            DistanceStatistic newStat = new DistanceStatistic();
            newStat.setCountry(country);
            newStat.setDistance(distance);
            newStat.setInvocations(1);
            statisticsRepository.save(newStat);
        }
    }

    public String getAverageDistance() {
        List<DistanceStatistic> statistics = statisticsRepository.findAll();
        double totalDistance = 0;
        int totalInvocations = 0;
        Map<String, Double> countryDistanceMap = new HashMap<>();

        for (DistanceStatistic stat : statistics) {
            totalDistance += stat.getDistance() * stat.getInvocations();
            totalInvocations += stat.getInvocations();
            countryDistanceMap.put(stat.getCountry(), stat.getDistance());
        }

        double averageDistance = totalInvocations == 0 ? 0 : totalDistance / totalInvocations;

        StringBuilder table = new StringBuilder();
        table.append("País, Distancia, Número de Invocaciones\n");

        for (Map.Entry<String, Double> entry : countryDistanceMap.entrySet()) {
            String country = entry.getKey();
            double distance = entry.getValue();
            int invocations = statistics.stream()
                    .filter(stat -> stat.getCountry().equalsIgnoreCase(country))
                    .findFirst().map(DistanceStatistic::getInvocations)
                    .orElse(0);
            table.append(String.format("%s, %.2f km, %d\n", country, distance, invocations));
        }

        table.append(String.format("Distancia promedio: %.2f km\n", averageDistance));
        return table.toString();
    }

    public String getMaxDistance() {
        Optional<DistanceStatistic> maxStat = statisticsRepository.findAll().stream()
                .max((stat1, stat2) -> Double.compare(stat1.getDistance(), stat2.getDistance()));

        StringBuilder table = new StringBuilder();
        table.append("País, Distancia, Número de Invocaciones\n");

        if (maxStat.isPresent()) {
            DistanceStatistic stat = maxStat.get();
            table.append(String.format("%s, %.2f km, %d\n", stat.getCountry(), stat.getDistance(), stat.getInvocations()));
        } else {
            table.append("No hay datos disponibles.\n");
        }

        return table.toString();
    }

    public String getMinDistance() {
        Optional<DistanceStatistic> minStat = statisticsRepository.findAll().stream()
                .min((stat1, stat2) -> Double.compare(stat1.getDistance(), stat2.getDistance()));

        StringBuilder table = new StringBuilder();
        table.append("País, Distancia, Número de Invocaciones\n");

        if (minStat.isPresent()) {
            DistanceStatistic stat = minStat.get();
            table.append(String.format("%s, %.2f km, %d\n", stat.getCountry(), stat.getDistance(), stat.getInvocations()));
        } else {
            table.append("No hay datos disponibles.\n");
        }

        return table.toString();
    }
}
