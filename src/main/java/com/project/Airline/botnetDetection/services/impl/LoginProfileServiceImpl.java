package com.project.Airline.botnetDetection.services.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.project.Airline.botnetDetection.exceptions.trafficProfile.CreatingProfileException;
import com.project.Airline.botnetDetection.exceptions.trafficProfile.GeneratingProfileException;
import com.project.Airline.botnetDetection.models.LoginFails;
import com.project.Airline.botnetDetection.models.LoginProfile;
import com.project.Airline.botnetDetection.repository.LoginProfileRepository;
import com.project.Airline.botnetDetection.services.LoginFailsService;
import com.project.Airline.botnetDetection.services.LoginProfileService;

import jakarta.persistence.EntityNotFoundException;

@EnableAsync
@Service
public class LoginProfileServiceImpl implements LoginProfileService {

	private final LoginFailsService loginFailsService;
	private final LoginProfileRepository loginProfileRepository;
	
	@Autowired
	public LoginProfileServiceImpl(LoginFailsService loginFailsService, LoginProfileRepository loginProfileRepository) {

		this.loginFailsService = loginFailsService;
		this.loginProfileRepository = loginProfileRepository;
	}

	@Override
	public LoginProfile getLoginProfile(LocalTime hour) throws EntityNotFoundException {

		List<LoginProfile> attempts = loginProfileRepository.findByHour(hour);
		if(attempts.isEmpty())
			throw new EntityNotFoundException("No profile for current time");
		
		return attempts.get(0);
	}

	@Override
	public LoginProfile addLoginProfile(LoginProfile loginProfile) throws CreatingProfileException {

		loginProfileRepository.save(loginProfile);
		List<LoginProfile> createdProfile = loginProfileRepository.findByHour(loginProfile.getHour());
		if(createdProfile.isEmpty())
			throw new CreatingProfileException("Could not create profile");
		
		return createdProfile.get(0);
	}

	@Override
	public LoginProfile updateLoginProfile(LoginProfile loginProfile) {

		loginProfileRepository.save(loginProfile);
		List<LoginProfile> updatedProfile = loginProfileRepository.findByHour(loginProfile.getHour());
		return updatedProfile.get(0);
	}

	@Async
	@Scheduled(cron = "0 0 0 * * *")
	@Override
	public void generateLoginProfile() throws GeneratingProfileException {

		Long averageAttempts = 0L;
		
		LocalTime t = LocalTime.parse("00:00:00");
		do {
			List<LoginFails> attemptsInAnHour = loginFailsService.getLoginFailsByHour(t);
			
			averageAttempts = attemptsInAnHour.stream()
					.map(attempts -> attempts.getFailCount()).reduce(0L, (a, b) -> a + b);
			
			if(attemptsInAnHour.size() > 0) {
			
				averageAttempts = averageAttempts / attemptsInAnHour.size();
			}
			
			List<LoginProfile> loginProfiles = loginProfileRepository.findByHour(t); 
			
			if (loginProfiles.isEmpty()) {
				
				LoginProfile loginProfile = new LoginProfile();
				loginProfile.setHour(t);
				loginProfile.setFailAttempts(averageAttempts);
				try {
					addLoginProfile(loginProfile);
				} catch (Exception e) {
					throw new GeneratingProfileException("Could not generate traffic profile");
				}
			
			} else {
				
				updateLoginProfile(loginProfiles.get(0));
			}
			
			t = t.plusHours(1);
		} while (t.equals(LocalTime.parse("23:00:00")));
		
		loginFailsService.deleteLoginFailsByDay(LocalDate.now().minusDays(7));
	}
}
