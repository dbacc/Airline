package com.project.Airline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.Airline.models.Authority;
import com.project.Airline.models.User;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

	Authority findByUser(User user);
}
