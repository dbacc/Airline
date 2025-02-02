package com.project.Airline.botnetDetection.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class BlockedIpDTOList {

	private List<BlockedIpDTO> ips;
	
	public void addIp(BlockedIpDTO ip) {
		this.ips.add(ip);
	}
}
