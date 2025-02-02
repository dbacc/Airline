package com.project.Airline.services;

import com.project.Airline.exceptions.authority.AddingAuthorityException;
import com.project.Airline.models.Authority;
import com.project.Airline.models.User;

public interface AuthorityService {

	Authority addAuthority(User user) throws AddingAuthorityException;
	String getAuthority(User user);
}
