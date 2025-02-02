package com.project.Airline.services.impl;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.project.Airline.botnetDetection.configuration.LoginFailureHandler;
import com.project.Airline.services.LoginService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class LoginServiceImpl implements LoginService {

	private AuthenticationManager authenticationManager;
	private LoginFailureHandler loginFailureHandler;
	
	public LoginServiceImpl(AuthenticationManager authenticationManager, LoginFailureHandler loginFailureHandler) {
		
		this.authenticationManager = authenticationManager;
		this.loginFailureHandler = loginFailureHandler;
	}
	
	@Override
	public boolean attemptAuthentication(String username, String password, HttpServletRequest request) throws IOException {

		UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
		
		Authentication authenticated;
        try {
        	authenticated = authenticationManager.authenticate(authenticationToken);
        } catch(AuthenticationException e) {
        	loginFailureHandler.onAuthenticationFailure();
        	return false;
        }
        
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authenticated);
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
        
		return true;
	}
}
