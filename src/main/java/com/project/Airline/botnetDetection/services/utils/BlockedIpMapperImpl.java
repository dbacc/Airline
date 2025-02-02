package com.project.Airline.botnetDetection.services.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.project.Airline.botnetDetection.dto.BlockedIpDTO;
import com.project.Airline.botnetDetection.models.BlockedIp;

@Component
public class BlockedIpMapperImpl implements BlockedIpMapper {

	@Override
	public BlockedIpDTO toDTO(BlockedIp blockedIp) {

		BlockedIpDTO blockedIpDTO = new BlockedIpDTO();
		blockedIpDTO.setId(blockedIp.getId());
		blockedIpDTO.setIp(blockedIp.getIp());
		blockedIpDTO.setMarked("false");
		
		return blockedIpDTO;
	}

	@Override
	public BlockedIp toEntity(BlockedIpDTO blockedIpDTO) {
		
		BlockedIp blockedIp = new BlockedIp();
		blockedIp.setId(blockedIpDTO.getId());
		blockedIp.setIp(blockedIpDTO.getIp());
		
		return blockedIp;
	}

	@Override
	public List<BlockedIpDTO> toListDTO(List<BlockedIp> blockedIp) {

		return blockedIp.stream().map(x -> toDTO(x)).collect(Collectors.toList());
	}

	@Override
	public List<BlockedIp> toListEntity(List<BlockedIpDTO> blockedIpDTO) {

		return blockedIpDTO.stream().map(x -> toEntity(x)).collect(Collectors.toList());
	}

}
