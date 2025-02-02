package com.project.Airline.botnetDetection.detection;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.project.Airline.botnetDetection.models.Traffic;
import com.project.Airline.botnetDetection.services.BlockedIpService;
import com.project.Airline.botnetDetection.services.IpTrafficService;
import com.project.Airline.botnetDetection.services.TrafficProfileService;
import com.project.Airline.botnetDetection.services.TrafficService;
import com.project.Airline.botnetDetection.configuration.RequestsInterceptor;
import com.project.Airline.botnetDetection.countermeasure.BlockedIpsFilter;
import com.project.Airline.botnetDetection.models.IpTraffic;

@EnableAsync
@Component
public class DDoSDetection {

	private final TrafficService trafficService;
	private final TrafficProfileService trafficProfileService;
	private final IpTrafficService ipTrafficService;
	private final RequestsInterceptor requestsInterceptor;
	private final BlockedIpService blockedIpService;
	private final BlockedIpsFilter blockedIpsFilter;

	
	@Autowired
	public DDoSDetection(TrafficService trafficService, TrafficProfileService trafficProfileService, 
			IpTrafficService ipTrafficService, RequestsInterceptor requestsInterceptor, 
			BlockedIpService blockedIpService, BlockedIpsFilter blockedIpsFilter) {

		this.trafficService = trafficService;
		this.trafficProfileService = trafficProfileService;
		this.ipTrafficService = ipTrafficService;
		this.requestsInterceptor = requestsInterceptor;
		this.blockedIpService = blockedIpService;
		this.blockedIpsFilter = blockedIpsFilter;
	}

	@Async
	@Scheduled(cron = "0 * * * * *")
	public void detection() throws Exception {
		
		if(requestsInterceptor.getTraffic().getTraffic() > 0L) {
			trafficService.updateTraffic(requestsInterceptor.getTraffic());
			Traffic nextMinuteTraffic = new Traffic();
			nextMinuteTraffic.setTraffic(0L);
			requestsInterceptor.setTraffic(nextMinuteTraffic);
		}
		
		ipTrafficService.updateTraffic(requestsInterceptor.getIpTrafficList());
		requestsInterceptor.setIpTrafficList(new ArrayList<IpTraffic>());

		LocalTime time = LocalTime.now().withSecond(0).withNano(0).minusMinutes(1);
		LocalDate day;
		if(time.equals(LocalTime.of(0, 0, 0))) {
			day = LocalDate.now().minusDays(1);
		} else {
			day = LocalDate.now();
		}
		
		List<Traffic> currentTraffic = trafficService.getTrafficByMinute(time);
		currentTraffic = currentTraffic.stream()
				.filter(traffic -> traffic.getDay().equals(day)).collect(Collectors.toList());
		
		double trafficThreshold;
		try {
			trafficThreshold = trafficProfileService.getTrafficProfile(time.withMinute(0)).getTraffic();
		} catch(Exception e) {
			return;
		}
		
		if (trafficThreshold < 1000) {
			trafficThreshold = 1250;
//			trafficThreshold = 5;
		} else {
			trafficThreshold = trafficThreshold * 1.25;
		}
		
		if(!currentTraffic.isEmpty()) {
			if (currentTraffic.get(0).getTraffic() > trafficThreshold) {
				System.out.println("DDoS attack detected");

				List<String> ipToBlock = ipTrafficService.getIpsByMostTraffic(time);
				blockedIpsFilter.addIps(ipToBlock);
				blockedIpService.addIps(ipToBlock);
			}
		}
	}
}
