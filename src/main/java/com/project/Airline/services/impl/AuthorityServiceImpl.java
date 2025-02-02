package com.project.Airline.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.Airline.exceptions.authority.AddingAuthorityException;
import com.project.Airline.models.Authority;
import com.project.Airline.models.User;
import com.project.Airline.repository.AuthorityRepository;
import com.project.Airline.services.AuthorityService;

import jakarta.persistence.EntityNotFoundException;

@Service("AuthorotyServiceRepo")
public class AuthorityServiceImpl implements AuthorityService{

	private final AuthorityRepository authorityRepository;
	
	@Autowired
	public AuthorityServiceImpl(AuthorityRepository authorityRepository) {
		
		this.authorityRepository = authorityRepository;
	}

	@Override
	public Authority addAuthority(User user) throws AddingAuthorityException {
		
		Authority authority = new Authority();
		authority.setUser(user);
		authority.setRole("ADMIN");
		
		authorityRepository.save(authority);
		
		try {
			return authorityRepository.findByUser(user);
		}
		catch (EntityNotFoundException e) {
			throw new AddingAuthorityException(String.format("Error occurred while trying to register new user."));
		}
	}

	@Override
	public String getAuthority(User user) {
		
		return authorityRepository.findByUser(user).getRole();
	}

	
}
