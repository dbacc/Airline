package com.project.Airline.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.Airline.exceptions.plane.PlaneDeletionException;
import com.project.Airline.exceptions.plane.PlaneNotFoundException;
import com.project.Airline.exceptions.plane.PlaneStoringException;
import com.project.Airline.exceptions.plane.PlaneUpdatingException;
import com.project.Airline.models.Plane;
import com.project.Airline.repository.PlaneRepository;
import com.project.Airline.services.PlaneService;

@Service("planeServiceRepo")
public class PlaneServiceImpl implements PlaneService {
	
	private final PlaneRepository planeRepository;
	
	@Autowired
	public PlaneServiceImpl(PlaneRepository planeRepository) {
		
		this.planeRepository = planeRepository;
	}
	
	@Override
	public Plane getPlaneById(Long id) throws PlaneNotFoundException{
		
		Optional<Plane> planeById = planeRepository.findById(id);
        if (planeById.isEmpty()) {
            throw new PlaneNotFoundException(String.format("Plane with id %d could not be found.", id));
        }
		
		return planeById.get();
	}
	
	@Override
	public Plane addNewPlane(Plane plane) throws PlaneStoringException{
		
		try {
			return planeRepository.save(plane);
		} catch (Exception e) {
			throw new PlaneStoringException("Error occurred while trying to store plane.");
		}
	}

	@Override
	public Plane updatePlane(Long id, Plane plane) throws PlaneNotFoundException, PlaneUpdatingException {
		
		Plane planeUpdate;
		try {
			planeUpdate = getPlaneById(id);
		} catch(Exception e) {
			throw new PlaneNotFoundException(String.format("Plane with id %d could not be found.", id));
		}
		
		planeUpdate.setBrand(plane.getBrand());
		planeUpdate.setModel(plane.getModel());
		planeUpdate.setYear(plane.getYear());
		
		try {
			return planeRepository.save(planeUpdate);
		} catch (Exception e) {
			throw new PlaneUpdatingException(String.format("Error occurred while trying to update plane with id %d.", id));
		}
	}

	@Override
	public Plane deletePlane(Long id) throws PlaneNotFoundException, PlaneDeletionException{
		
		Plane planeToBeDeleted;
		try {
			planeToBeDeleted = getPlaneById(id);
		} catch(Exception e) {
			throw new PlaneNotFoundException(String.format("Plane with id %d could not be found.", id));
		}
		
		planeRepository.delete(planeToBeDeleted);
		
		List<Plane> planes = planeRepository.findAll();
		Optional<Plane> planeById = planes.stream().filter(plane -> plane.getId().equals(id)).findAny();
		
		if(!planeById.isEmpty()) {
			throw new PlaneDeletionException(String.format("Error occurred while trying to delete plane with id %d.", id));
		}

		return planeToBeDeleted;
	}
}
