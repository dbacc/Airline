package com.project.Airline.botnetDetection.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.project.Airline.botnetDetection.exceptions.traffic.UpdateTrafficException;
import com.project.Airline.botnetDetection.models.Traffic;

import jakarta.persistence.EntityNotFoundException;

public interface TrafficService {

	List<Traffic> getTrafficByMinute(LocalTime minute) throws EntityNotFoundException ;
	Traffic updateTraffic(Traffic newTraffic) throws UpdateTrafficException;
	void deleteDayTraffic(LocalDate day);
}
