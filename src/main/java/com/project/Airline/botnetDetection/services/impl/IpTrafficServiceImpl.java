package com.project.Airline.botnetDetection.services.impl;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.project.Airline.botnetDetection.exceptions.traffic.UpdateTrafficException;
import com.project.Airline.botnetDetection.models.IpTraffic;
import com.project.Airline.botnetDetection.repository.IpTrafficRepository;
import com.project.Airline.botnetDetection.services.IpTrafficService;

@EnableAsync
@Service
public class IpTrafficServiceImpl implements IpTrafficService {

	private final IpTrafficRepository ipTrafficRepository;
	
	@Autowired
	public IpTrafficServiceImpl(IpTrafficRepository ipTrafficRepository) {

		this.ipTrafficRepository = ipTrafficRepository;
	}

	@Override
	public List<IpTraffic> updateTraffic(List<IpTraffic> ipTrafficList) throws UpdateTrafficException {
		
		for(IpTraffic ipTraffic : ipTrafficList) {
			
			List<IpTraffic> filteredTraffic = ipTrafficRepository.findByIp(ipTraffic.getIp());
			filteredTraffic = filteredTraffic.stream()
					.filter(traffic -> traffic.getMinute().equals(ipTraffic.getMinute())).collect(Collectors.toList());
			
			if(filteredTraffic.size() > 1)
				throw new UpdateTrafficException("More than one relations found for the given time");
			
			if(filteredTraffic.isEmpty()) {			
				ipTrafficRepository.save(ipTraffic);	
			} else {
				filteredTraffic.get(0).setTraffic(filteredTraffic.get(0).getTraffic() + ipTraffic.getTraffic());
				ipTrafficRepository.save(filteredTraffic.get(0));
			}
		}
		
		return ipTrafficList;
	}
	
	
	public List<String> getIpsByMostTraffic(LocalTime time) {
		
		List<IpTraffic> mostIpTraffic = ipTrafficRepository.findByMinute(time);
		List<Long> onlyTraffic = mostIpTraffic.stream().map(traffic -> traffic.getTraffic()).collect(Collectors.toList());
		Long maxTraffic;
		
		if(!onlyTraffic.isEmpty()) {
		
			maxTraffic = Collections.max(onlyTraffic);
		} else {
			return Arrays.asList();
		}
		
		 List<String> ipsToBlock = new ArrayList<>();
		 
		 for(IpTraffic traffic : mostIpTraffic) {
			 if(traffic.getTraffic() > maxTraffic * 0.75) {
				 ipsToBlock.add(traffic.getIp());
			 }
		 }
		 
		 return ipsToBlock;
	}
	
	@Async
	@Scheduled(cron = "0 * * * * *")
	@Override
	public void deleteIpTraffic() {
		
		List<IpTraffic> oldTraffic = ipTrafficRepository.findAll();
		oldTraffic.removeIf(traffic -> traffic.getMinute().isAfter(LocalTime.now().withSecond(0).withNano(0).minusMinutes(6)));
		oldTraffic.forEach(traffic -> ipTrafficRepository.delete(traffic));
	}

}
