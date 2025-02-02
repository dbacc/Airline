package com.project.Airline.botnetDetection.repository;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.Airline.botnetDetection.models.IpTraffic;

@Repository
public interface IpTrafficRepository extends JpaRepository<IpTraffic, Long> {

	List<IpTraffic> findByMinute(LocalTime minute);
	List<IpTraffic> findByIp(String ip);
}
