package com.project.Airline.botnetDetection.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.Airline.botnetDetection.models.LoginFails;

@Repository
public interface LoginFailsRepository extends JpaRepository<LoginFails, Long> {

	List<LoginFails> findByHour(LocalTime attemptTime);
	List<LoginFails> findByDay(LocalDate day);
}
