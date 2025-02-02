package com.project.Airline.botnetDetection.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.Airline.botnetDetection.models.Traffic;

@Repository
public interface TrafficRepository extends JpaRepository<Traffic, Long> {

	List<Traffic> findByMinute(LocalTime minute);
	List<Traffic> findByDay(LocalDate day);
}
