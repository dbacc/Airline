package com.project.Airline.botnetDetection.services;

import java.util.List;

import com.project.Airline.botnetDetection.dto.BlockedIpDTOList;
import com.project.Airline.botnetDetection.models.BlockedIp;

public interface BlockedIpService {

	BlockedIpDTOList getIpsDTO();
	List<BlockedIp> addIps(List<String> ips);
	List<BlockedIp> deleteIps(BlockedIpDTOList ipListDTO);
	List<BlockedIp> deleteAllIps();
}
