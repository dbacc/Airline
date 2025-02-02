package com.project.Airline.botnetDetection.services;

import java.time.LocalTime;

import com.project.Airline.botnetDetection.exceptions.trafficProfile.CreatingProfileException;
import com.project.Airline.botnetDetection.exceptions.trafficProfile.GeneratingProfileException;
import com.project.Airline.botnetDetection.models.TrafficProfile;

import jakarta.persistence.EntityNotFoundException;

public interface TrafficProfileService {

	TrafficProfile getTrafficProfile(LocalTime hour) throws EntityNotFoundException;
	TrafficProfile addTrafficProfile(TrafficProfile trafficProfile) throws CreatingProfileException;
	TrafficProfile updateTrafficProfile(TrafficProfile trafficProfile);
	void generateTrafficProfile() throws GeneratingProfileException;
}
