package com.project.Airline.botnetDetection.repository;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.Airline.botnetDetection.models.TrafficProfile;

@Repository
public interface TrafficProfileRepository extends JpaRepository<TrafficProfile, Long> {

	List<TrafficProfile> findByHour(LocalTime hour);
}
