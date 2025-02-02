package com.project.Airline.botnetDetection.services;

import java.time.LocalTime;
import java.util.List;

import com.project.Airline.botnetDetection.exceptions.traffic.UpdateTrafficException;
import com.project.Airline.botnetDetection.models.IpTraffic;

public interface IpTrafficService {

	List<IpTraffic> updateTraffic(List<IpTraffic> ipTrafficList) throws UpdateTrafficException;
	void deleteIpTraffic();
	List<String> getIpsByMostTraffic(LocalTime time);
}
