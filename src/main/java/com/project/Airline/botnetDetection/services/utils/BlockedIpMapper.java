package com.project.Airline.botnetDetection.services.utils;

import java.util.List;

import com.project.Airline.botnetDetection.dto.BlockedIpDTO;
import com.project.Airline.botnetDetection.models.BlockedIp;

public interface BlockedIpMapper {

	BlockedIpDTO toDTO(BlockedIp blockedIp);
	
	BlockedIp toEntity(BlockedIpDTO blockedIpDTO);
	
	List<BlockedIpDTO> toListDTO(List<BlockedIp> blockedIp);
	
	List<BlockedIp> toListEntity(List<BlockedIpDTO> blockedIpDTO);
	
}
