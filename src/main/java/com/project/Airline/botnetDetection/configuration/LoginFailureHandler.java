package com.project.Airline.botnetDetection.configuration;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.Airline.botnetDetection.exceptions.loginFails.UpdateLoginFailsException;
import com.project.Airline.botnetDetection.services.LoginFailsService;

@Component
public class LoginFailureHandler {

	private LoginFailsService loginFailsService;
	
	@Autowired
	public LoginFailureHandler(LoginFailsService loginFailsService) {
		
		this.loginFailsService = loginFailsService;
	}
	
	public void onAuthenticationFailure() throws IOException {
		
		try {
			loginFailsService.updateLoginFails(LocalDateTime.now());
		} catch (UpdateLoginFailsException e) {
			throw new IOException("More than one relations found for the given time");
		}
	}
}
