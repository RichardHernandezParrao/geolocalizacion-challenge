package com.geolocalizacion.challenge.repository;

import com.geolocalizacion.challenge.model.dto.DistanceStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatisticsRepository extends JpaRepository<DistanceStatistic, Long> {
}