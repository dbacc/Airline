package com.project.Airline.botnetDetection.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.Airline.botnetDetection.models.BlockedIp;

@Repository
public interface BlockedIpRepository extends JpaRepository<BlockedIp, Long> {

	BlockedIp findByIp(String ip);
}
