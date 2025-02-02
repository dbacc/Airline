package com.project.Airline.botnetDetection.services.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.project.Airline.botnetDetection.exceptions.trafficProfile.CreatingProfileException;
import com.project.Airline.botnetDetection.exceptions.trafficProfile.GeneratingProfileException;
import com.project.Airline.botnetDetection.models.Traffic;
import com.project.Airline.botnetDetection.models.TrafficProfile;
import com.project.Airline.botnetDetection.repository.TrafficProfileRepository;
import com.project.Airline.botnetDetection.services.TrafficProfileService;
import com.project.Airline.botnetDetection.services.TrafficService;

import jakarta.persistence.EntityNotFoundException;

@EnableAsync
@Service
public class TrafficProfileServiceImp implements TrafficProfileService {

	private final TrafficService trafficService;
	private final TrafficProfileRepository trafficProfileRepository;
	
	@Autowired
	public TrafficProfileServiceImp(TrafficService trafficService, TrafficProfileRepository trafficProfileRepository) {

		this.trafficService = trafficService;
		this.trafficProfileRepository = trafficProfileRepository;
	}
	
	@Override
	public TrafficProfile getTrafficProfile(LocalTime hour) throws EntityNotFoundException {
		
		List<TrafficProfile> traffic = trafficProfileRepository.findByHour(hour);
		if(traffic.isEmpty())
			throw new EntityNotFoundException("No profile for current time");
		
		return traffic.get(0);
	}

	@Override
	public TrafficProfile addTrafficProfile(TrafficProfile trafficProfile) throws CreatingProfileException {
		
		trafficProfileRepository.save(trafficProfile);
		List<TrafficProfile> createdProfile = trafficProfileRepository.findByHour(trafficProfile.getHour());
		if(createdProfile.isEmpty())
			throw new CreatingProfileException("Could not create profile");
		
		return createdProfile.get(0);
	}

	@Override
	public TrafficProfile updateTrafficProfile(TrafficProfile trafficProfile) {
		
		trafficProfileRepository.save(trafficProfile);
		List<TrafficProfile> updatedProfile = trafficProfileRepository.findByHour(trafficProfile.getHour());
		return updatedProfile.get(0);
	}

	@Async
	@Scheduled(cron = "0 0 0 * * *")
	@Override
	public void generateTrafficProfile() throws GeneratingProfileException {

		Long[] averageTraffic = new Long[59];
		
		LocalTime t = LocalTime.parse("00:00:00");
		do {
			int minuteInAnHour = t.getMinute();
			
			List<Traffic> minuteTraffic = trafficService.getTrafficByMinute(t);
			
			averageTraffic[minuteInAnHour] = minuteTraffic.stream()
					.map(traffic -> traffic.getTraffic()).reduce(0L, (a, b) -> a + b);
			
			if(minuteTraffic.size() > 0) {
			
				averageTraffic[minuteInAnHour] = averageTraffic[minuteInAnHour] / minuteTraffic.size();
			}
			
			if (minuteInAnHour == 59) {
				Long trafficByHour = Arrays.stream(averageTraffic).reduce(0L, (a, b) -> a + b);
				trafficByHour = trafficByHour / 60;
				
				List<TrafficProfile> trafficProfiles = trafficProfileRepository.findByHour(t.withMinute(0)); 
				
				if (trafficProfiles.isEmpty()) {
					
					TrafficProfile trafficProfile = new TrafficProfile();
					trafficProfile.setHour(t.withMinute(0));
					trafficProfile.setTraffic(trafficByHour);
					try {
						addTrafficProfile(trafficProfile);
					} catch (Exception e) {
						throw new GeneratingProfileException("Could not generate traffic profile");
					}
				
				} else {
					
					updateTrafficProfile(trafficProfiles.get(0));
				}
			
			}
			
			t = t.plusMinutes(1);
		} while (t.equals(LocalTime.parse("23:59:00")));
		
		trafficService.deleteDayTraffic(LocalDate.now().minusDays(7));
	}
}
