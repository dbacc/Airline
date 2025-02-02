package com.project.Airline.botnetDetection.services;

import java.time.LocalTime;

import com.project.Airline.botnetDetection.exceptions.trafficProfile.CreatingProfileException;
import com.project.Airline.botnetDetection.exceptions.trafficProfile.GeneratingProfileException;
import com.project.Airline.botnetDetection.models.LoginProfile;

import jakarta.persistence.EntityNotFoundException;

public interface LoginProfileService {

	LoginProfile getLoginProfile(LocalTime hour) throws EntityNotFoundException;
	LoginProfile addLoginProfile(LoginProfile loginProfile) throws CreatingProfileException;
	LoginProfile updateLoginProfile(LoginProfile loginProfile);
	void generateLoginProfile() throws GeneratingProfileException;
}
