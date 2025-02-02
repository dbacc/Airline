package com.project.Airline.services;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;

public interface LoginService {

	boolean attemptAuthentication(String username, String password, HttpServletRequest request) throws IOException;
}
