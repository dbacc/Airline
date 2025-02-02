package com.project.Airline.services;

import java.util.List;

import com.project.Airline.dto.FlightDTO;
import com.project.Airline.exceptions.flight.FlightDeletionException;
import com.project.Airline.exceptions.flight.FlightNotFoundException;
import com.project.Airline.exceptions.flight.FlightParametersException;
import com.project.Airline.exceptions.flight.FlightStoringException;
import com.project.Airline.exceptions.flight.FlightUpdatingException;
import com.project.Airline.models.Flight;

public interface FlightService {

	List<FlightDTO> getFlights(String departurePlace,
							   String arrivalPlace,
							   String departureDate) throws FlightNotFoundException, FlightParametersException;
	
	List<FlightDTO> getFlightsPagination(Integer page, Integer elementsInPage);
	
	Flight getFlightById(Long id) throws FlightNotFoundException;
	
	Flight addNewFlight(Flight flight) throws FlightStoringException;
	
	Flight updateFlight(Long id, Flight flight) throws FlightNotFoundException, FlightUpdatingException;
	
	Flight deleteFlight(Long id) throws FlightNotFoundException, FlightDeletionException;
}
