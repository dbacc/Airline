package com.project.Airline.botnetDetection.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.project.Airline.botnetDetection.exceptions.loginFails.UpdateLoginFailsException;
import com.project.Airline.botnetDetection.models.LoginFails;

import jakarta.persistence.EntityNotFoundException;

public interface LoginFailsService {

	List<LoginFails> getLoginFailsByHour(LocalTime hour) throws EntityNotFoundException ;
	LoginFails updateLoginFails(LocalDateTime attemptDateTime) throws UpdateLoginFailsException;
	void deleteLoginFailsByDay(LocalDate day);
}
