package com.project.Airline.botnetDetection.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.Airline.botnetDetection.countermeasure.BlockedIpsFilter;
import com.project.Airline.botnetDetection.dto.BlockedIpDTO;
import com.project.Airline.botnetDetection.dto.BlockedIpDTOList;
import com.project.Airline.botnetDetection.models.BlockedIp;
import com.project.Airline.botnetDetection.repository.BlockedIpRepository;
import com.project.Airline.botnetDetection.services.BlockedIpService;
import com.project.Airline.botnetDetection.services.utils.BlockedIpMapper;

@Service
public class BlockedIpServiceImpl implements BlockedIpService {

	private final BlockedIpRepository blockedIpRepository;
	private final BlockedIpMapper blockedIpMapper;
	private final BlockedIpsFilter blockedIpsFilter;
	
	@Autowired
	public BlockedIpServiceImpl(BlockedIpRepository blockedIpRepository, BlockedIpMapper blockedIpMapper, 
			BlockedIpsFilter blockedIpsFilter) {
		
		this.blockedIpRepository = blockedIpRepository;
		this.blockedIpMapper = blockedIpMapper;
		this.blockedIpsFilter = blockedIpsFilter;
		this.blockedIpsFilter.updateIps(blockedIpRepository.findAll());
	}
	
	@Override
	public BlockedIpDTOList getIpsDTO() {

		List<BlockedIp> ipList = blockedIpRepository.findAll();
		BlockedIpDTOList ipListDTO = new BlockedIpDTOList(blockedIpMapper.toListDTO(ipList));
		
		return ipListDTO;
	}

	@Override
	public List<BlockedIp> addIps(List<String> ips) {
		
		List<BlockedIp> newBlockedIps = ips.stream().map(ip -> new BlockedIp(ip)).collect(Collectors.toList());
		newBlockedIps.stream().forEach(newIp -> blockedIpRepository.save(newIp));
		
		return newBlockedIps;
	}
	
	@Override
	public List<BlockedIp> deleteIps(BlockedIpDTOList ipListDTO) {
		
		List<BlockedIp> ipList = new ArrayList<>();
		for(BlockedIpDTO ip : ipListDTO.getIps()) {
			
			if(ip.getMarked() != null) {
				ipList.add(blockedIpMapper.toEntity(ip));
			}
		}
		
		ipList.stream().forEach(ip -> blockedIpRepository.delete(ip));
		blockedIpsFilter.updateIps(blockedIpRepository.findAll());
		
		return ipList;
	}
	
	public List<BlockedIp> deleteAllIps() {
		
		List<BlockedIp> ipList = blockedIpRepository.findAll();
		ipList.stream().forEach(ip -> blockedIpRepository.delete(ip));
		blockedIpsFilter.updateIps(blockedIpRepository.findAll());
		
		return ipList;
	}
}
