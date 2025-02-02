package com.project.Airline.services;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.project.Airline.exceptions.user.RegisterUserException;
import com.project.Airline.models.User;

public interface UserService {

	User registerUser(User user) throws RegisterUserException;
	User getUserByUsername(String username) throws UsernameNotFoundException;
}
