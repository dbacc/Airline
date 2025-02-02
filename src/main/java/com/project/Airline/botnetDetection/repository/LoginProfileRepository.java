package com.project.Airline.botnetDetection.repository;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.Airline.botnetDetection.models.LoginProfile;

@Repository
public interface LoginProfileRepository extends JpaRepository<LoginProfile, Long> {

	List<LoginProfile> findByHour(LocalTime hour);
}
