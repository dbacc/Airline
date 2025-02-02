package com.project.Airline.botnetDetection.services.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.Airline.botnetDetection.exceptions.traffic.UpdateTrafficException;
import com.project.Airline.botnetDetection.models.Traffic;
import com.project.Airline.botnetDetection.repository.TrafficRepository;
import com.project.Airline.botnetDetection.services.TrafficService;

@Service
public class TrafficServiceImpl implements TrafficService {

	private final TrafficRepository trafficRepository;

	@Autowired
	public TrafficServiceImpl(TrafficRepository trafficRepository) {
		
		this.trafficRepository = trafficRepository;
	}
	
	@Override
	public Traffic updateTraffic(Traffic newTraffic) throws UpdateTrafficException {
		
		List<Traffic> filteredTraffic = trafficRepository.findByMinute(newTraffic.getMinute());
		filteredTraffic = filteredTraffic.stream()
				.filter(traffic -> traffic.getDay().equals(newTraffic.getDay()))
				.collect(Collectors.toList());
		
		if(filteredTraffic.size() > 1)
			throw new UpdateTrafficException("More than one relations found for the given time");
		
		if(filteredTraffic.isEmpty()) {
			
			trafficRepository.save(newTraffic);
			return newTraffic;
		} 
			
		filteredTraffic.get(0).setTraffic(filteredTraffic.get(0).getTraffic() + newTraffic.getTraffic());
		trafficRepository.save(filteredTraffic.get(0));
		return filteredTraffic.get(0);
	}
	
	@Override
	public List<Traffic> getTrafficByMinute(LocalTime minute) {
		
		return trafficRepository.findByMinute(minute);
	}
	
	@Override
	public void deleteDayTraffic(LocalDate day) {
		
		List<Traffic> dayTraffic = trafficRepository.findAll();
		dayTraffic.removeIf(traffic -> traffic.getDay().isAfter(day));
		dayTraffic.forEach(traffic -> trafficRepository.delete(traffic));
	}
}
