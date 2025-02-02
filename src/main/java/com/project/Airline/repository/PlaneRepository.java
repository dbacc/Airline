package com.project.Airline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.Airline.models.Plane;

@Repository
public interface PlaneRepository extends JpaRepository<Plane, Long> {

}
