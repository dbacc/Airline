package com.project.Airline.services;

import com.project.Airline.exceptions.plane.PlaneDeletionException;
import com.project.Airline.exceptions.plane.PlaneNotFoundException;
import com.project.Airline.exceptions.plane.PlaneStoringException;
import com.project.Airline.exceptions.plane.PlaneUpdatingException;
import com.project.Airline.models.Plane;

public interface PlaneService {

	Plane getPlaneById(Long id) throws PlaneNotFoundException;
	
	Plane addNewPlane(Plane plane) throws PlaneStoringException;
	
	Plane updatePlane(Long id, Plane plane) throws PlaneNotFoundException, PlaneUpdatingException;
	
	Plane deletePlane(Long id) throws PlaneNotFoundException, PlaneDeletionException;
}
