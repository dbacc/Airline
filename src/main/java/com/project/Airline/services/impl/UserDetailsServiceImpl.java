package com.project.Airline.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.Airline.models.User;
import com.project.Airline.repository.UserRepository;
import com.project.Airline.services.AuthorityService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;
	private final AuthorityService authorityService;	
	
	@Autowired
	public UserDetailsServiceImpl(UserRepository userRepository, AuthorityService authorityService) {
		
		this.userRepository = userRepository;
		this.authorityService = authorityService;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.findByUsername(username);
		
		if(user == null) {
			throw new UsernameNotFoundException("User with username " + username + " not found");
		}
		
		UserDetails userDetails = org.springframework.security.core.userdetails.User
				.withUsername(user.getUsername())
				.password(user.getPassword())
				.roles(authorityService.getAuthority(user))
				.build();
		
		return userDetails;
	}

}
