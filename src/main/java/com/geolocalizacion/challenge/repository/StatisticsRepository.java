package com.geolocalizacion.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.geolocalizacion.challenge.model.DistanceStatistic;

@Repository
public interface StatisticsRepository extends JpaRepository<DistanceStatistic, Long> {
}