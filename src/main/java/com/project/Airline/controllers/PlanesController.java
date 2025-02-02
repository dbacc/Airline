package com.project.Airline.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.Airline.exceptions.plane.PlaneDeletionException;
import com.project.Airline.exceptions.plane.PlaneNotFoundException;
import com.project.Airline.exceptions.plane.PlaneStoringException;
import com.project.Airline.exceptions.plane.PlaneUpdatingException;
import com.project.Airline.models.Plane;
import com.project.Airline.services.PlaneService;

import lombok.SneakyThrows;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/planes")
public class PlanesController {
	
	private final PlaneService planeService;
	
	@Autowired
	public PlanesController(PlaneService planeService) {
		
		this.planeService = planeService;
	}
	
	@SneakyThrows
    @GetMapping("/{id}")
    public ResponseEntity<Plane> getPlaneById(@PathVariable("id") Long id) {
        
		return ResponseEntity.ok(planeService.getPlaneById(id));
    }
	
	@SneakyThrows
    @PostMapping()
    public ResponseEntity<Plane> addNewPlane(@RequestBody() Plane plane) {
        
		return new ResponseEntity<>(planeService.addNewPlane(plane), HttpStatus.CREATED);
    }
	
    @SneakyThrows
    @PutMapping("/{id}")
    public ResponseEntity<Plane> updatePlane(@PathVariable("id") Long id, @RequestBody() Plane plane) {
        
    	return new ResponseEntity<>(planeService.updatePlane(id, plane), HttpStatus.CREATED);
    }
	
    @SneakyThrows
    @DeleteMapping("/{id}")
    public ResponseEntity<Plane> deletePlane(@PathVariable("id") Long id) {
        
    	return new ResponseEntity<>(planeService.deletePlane(id), HttpStatus.OK);
    }
    
    
    @ExceptionHandler(value = PlaneNotFoundException.class)
    private ResponseEntity<String> handlePlaneNotFoundException(PlaneNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
    
    @ExceptionHandler(value = PlaneStoringException.class)
    private ResponseEntity<String> handlePlaneStoringException(PlaneStoringException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
    
    @ExceptionHandler(value = PlaneUpdatingException.class)
    private ResponseEntity<String> handlePlaneUpdatingException(PlaneUpdatingException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
    
    @ExceptionHandler(value = PlaneDeletionException.class)
    private ResponseEntity<String> handlePlaneDeletionException(PlaneDeletionException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }
    
}
