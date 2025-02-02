package com.project.Airline.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.Airline.exceptions.user.RegisterUserException;
import com.project.Airline.models.User;
import com.project.Airline.repository.UserRepository;
import com.project.Airline.services.AuthorityService;
import com.project.Airline.services.UserService;

@Service("UserServiceRepo")
public class UserServiceImpl implements UserService{

	private PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final AuthorityService authorityService;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository, AuthorityService authorityService, PasswordEncoder passwordEncoder) {
		
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authorityService = authorityService;
	}
	
	@Override
	public User registerUser(User user) throws RegisterUserException {
		
		User newUser = new User();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(passwordEncoder.encode(user.getPassword()));
		newUser.setEnabled(user.getEnabled());
		
		userRepository.save(newUser);
		
		try {
			authorityService.addAuthority(userRepository.findByUsername(newUser.getUsername()));
			return userRepository.findByUsername(user.getUsername());
		}
		catch (Exception e) {
			throw new RegisterUserException(String.format("Error occurred while trying to register new user."));
		}
	}

	@Override
	public User getUserByUsername(String username) throws UsernameNotFoundException {
		
		return userRepository.findByUsername(username);
	}
}
