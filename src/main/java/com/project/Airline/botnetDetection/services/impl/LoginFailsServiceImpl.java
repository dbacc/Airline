package com.project.Airline.botnetDetection.services.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.Airline.botnetDetection.exceptions.loginFails.UpdateLoginFailsException;
import com.project.Airline.botnetDetection.models.LoginFails;
import com.project.Airline.botnetDetection.repository.LoginFailsRepository;
import com.project.Airline.botnetDetection.services.LoginFailsService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class LoginFailsServiceImpl implements LoginFailsService {

	public final LoginFailsRepository loginFailsRepository;
	
	@Autowired
	public LoginFailsServiceImpl(LoginFailsRepository loginFailsRepository) {

		this.loginFailsRepository = loginFailsRepository;
	}

	@Override
	public List<LoginFails> getLoginFailsByHour(LocalTime hour) throws EntityNotFoundException {
		
		return loginFailsRepository.findByHour(hour);
	}

	@Override
	public LoginFails updateLoginFails(LocalDateTime attemptDateTime) throws UpdateLoginFailsException {
		
		List<LoginFails> filteredAttempts = loginFailsRepository.findByHour(attemptDateTime.toLocalTime().withMinute(0).withSecond(0).withNano(0));
		filteredAttempts = filteredAttempts.stream()
				.filter(attempt -> attempt.getDay().equals(attemptDateTime.toLocalDate()))
				.collect(Collectors.toList());
		
		if(filteredAttempts.size() > 1)
			throw new UpdateLoginFailsException("More than one relations found for the given time");
		
		if(filteredAttempts.isEmpty()) {
			
			LoginFails attempt = new LoginFails();
			attempt.setDay(attemptDateTime.toLocalDate());
			attempt.setHour(attemptDateTime.toLocalTime().withMinute(0).withSecond(0).withNano(0));
			attempt.setFailCount(1L);
			loginFailsRepository.save(attempt);
			return attempt;
			
		} 
			
		filteredAttempts.get(0).setFailCount(filteredAttempts.get(0).getFailCount() + 1);
		loginFailsRepository.save(filteredAttempts.get(0));
		return filteredAttempts.get(0);
	}

	@Override
	public void deleteLoginFailsByDay(LocalDate day) {
		
		List<LoginFails> dayAttempts = loginFailsRepository.findAll();
		dayAttempts.removeIf(attempt -> attempt.getDay().isAfter(day));
		dayAttempts.forEach(attempt -> loginFailsRepository.delete(attempt));
	}

}
