package com.project.Airline.controllers;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.Airline.dto.FlightDTO;
import com.project.Airline.exceptions.flight.FlightDeletionException;
import com.project.Airline.exceptions.flight.FlightNotFoundException;
import com.project.Airline.exceptions.flight.FlightParametersException;
import com.project.Airline.exceptions.flight.FlightStoringException;
import com.project.Airline.exceptions.flight.FlightUpdatingException;
import com.project.Airline.models.Flight;
import com.project.Airline.services.FlightService;

import lombok.SneakyThrows;

@RestController
@RequestMapping("/flights")
public class FlightsController {
	
	private final FlightService flightService;
	
	@Autowired
	public FlightsController(FlightService flightService) {
		
		this.flightService = flightService;
	}

	@SneakyThrows
    @GetMapping("/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable("id") Long id) {
        
		return ResponseEntity.ok(flightService.getFlightById(id));
    }
	
	@SneakyThrows
    @GetMapping()
    public ResponseEntity<List<FlightDTO>> getFlights(
            @RequestParam(required = false) String departurePlace,
            @RequestParam(required = false) String arrivalPlace,
            @RequestParam(required = false) String departureDate
    ) {
        return ResponseEntity.ok(flightService.getFlights(
        		departurePlace,
        		arrivalPlace,
        		departureDate
        ));
    }
	
	@SneakyThrows
    @GetMapping("/page")
    public ResponseEntity<List<FlightDTO>> getFlightsPagination(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "2") Integer elementsInPage
    ) {
        return ResponseEntity.ok(flightService.getFlightsPagination(page, elementsInPage));
    }
	
	@SneakyThrows
	@PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<Flight> addNewFlight(@RequestBody() Flight flight) {
        
		return new ResponseEntity<>(flightService.addNewFlight(flight), HttpStatus.CREATED);
    }
	
    @SneakyThrows
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Flight> updateFlight(@PathVariable("id") Long id, @RequestBody() Flight flight) {
        
    	return new ResponseEntity<>(flightService.updateFlight(id, flight), HttpStatus.CREATED);
    }
	
    @SneakyThrows
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Flight> deleteFlight(@PathVariable("id") Long id) {
        
    	return new ResponseEntity<>(flightService.deleteFlight(id), HttpStatus.OK);
    }
    
    
    @ExceptionHandler(value = FlightNotFoundException.class)
    private ResponseEntity<String> handleFlightNotFoundException(FlightNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
    
    @ExceptionHandler(value = FlightParametersException.class)
    private ResponseEntity<String> handleFlightParametersException(FlightParametersException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
    
    @ExceptionHandler(value = FlightStoringException.class)
    private ResponseEntity<String> handleFlightStoringException(FlightStoringException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
    
    @ExceptionHandler(value = FlightUpdatingException.class)
    private ResponseEntity<String> handleFlightUpdatingException(FlightUpdatingException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
    
    @ExceptionHandler(value = FlightDeletionException.class)
    private ResponseEntity<String> handleFlightDeletionException(FlightDeletionException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }
    
}
