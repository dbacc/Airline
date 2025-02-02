package com.project.Airline.services.impl;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.project.Airline.dto.FlightDTO;
import com.project.Airline.exceptions.flight.FlightDeletionException;
import com.project.Airline.exceptions.flight.FlightNotFoundException;
import com.project.Airline.exceptions.flight.FlightParametersException;
import com.project.Airline.exceptions.flight.FlightStoringException;
import com.project.Airline.exceptions.flight.FlightUpdatingException;
import com.project.Airline.models.Flight;
import com.project.Airline.repository.FlightRepository;
import com.project.Airline.services.FlightService;
import com.project.Airline.services.utils.FlightMapper;

@Service("flightServiceRepo")
public class FlightServiceImpl implements FlightService {
	
	private final FlightRepository flightRepository;
	
	private final FlightMapper flightMapper;
	
	@Autowired
	public FlightServiceImpl(FlightRepository flightRepository, FlightMapper flightMapper) {
		
		this.flightRepository = flightRepository;
		this.flightMapper = flightMapper;
	}
	
	@Override
	public List<FlightDTO> getFlights(String departurePlace, String arrivalPlace, String departureDate)
			throws FlightNotFoundException, FlightParametersException{
		
		List<Flight> flights = flightRepository.findAll();
		
		if (flights.isEmpty())
            return Collections.emptyList();

        if (departurePlace == null || departurePlace.isBlank())
        	throw new FlightParametersException("No departure location was given as a parameters.");
        
        if (arrivalPlace == null || arrivalPlace.isBlank())
        	throw new FlightParametersException("No arrival location was given as a parameters.");
        
        if (departureDate == null || departureDate.isBlank())
        	throw new FlightParametersException("No departure date was given as a parameters.");
        
        LocalDate formattedDate;
        try {
        	formattedDate = LocalDate.parse(departureDate);
        } catch (DateTimeParseException e) {
        	throw new FlightParametersException("No valid departure date was provided as a parameters.");
        }
        
        flights = flights.stream().filter(flight -> flight.getDepartureTime().toLocalDate().equals(formattedDate)
        		&& flight.getDeparturePlace().equalsIgnoreCase(departurePlace)
        		&& flight.getArrivalPlace().equalsIgnoreCase(arrivalPlace)).collect(Collectors.toList());
        
        return flightMapper.toListDTO(flights);
	}

	@Override
	public List<FlightDTO> getFlightsPagination(Integer page, Integer elementsInPage){
		
		return flightMapper.toListDTO(flightRepository.findAll(PageRequest.of(page, elementsInPage)));
	}

	@Override
	public Flight getFlightById(Long id) throws FlightNotFoundException{
		
		Optional<Flight> flightById = flightRepository.findById(id);
        if (flightById.isEmpty()) {
            throw new FlightNotFoundException(String.format("Flight with id %d could not be found.", id));
        }
		
		return flightById.get();
	}
	
	@Override
	public Flight addNewFlight(Flight flight) throws FlightStoringException{
		
		try {
			Flight addedFlight = flightRepository.save(flight);
			return addedFlight;
		} catch (Exception e) {
			throw new FlightStoringException("Error occurred while trying to store flight.");
		}
	}

	@Override
	public Flight updateFlight(Long id, Flight flight) throws FlightNotFoundException, FlightUpdatingException {
		
		Flight flightUpdate;
		try {
			flightUpdate = getFlightById(id);
		} catch(Exception e) {
			throw new FlightNotFoundException(String.format("Flight with id %d could not be found.", id));
		}
		
		flightUpdate.setDeparturePlace(flight.getDeparturePlace());
		flightUpdate.setArrivalPlace(flight.getArrivalPlace());
		flightUpdate.setDepartureTime(flight.getDepartureTime());
		flightUpdate.setArrivalTime(flight.getArrivalTime());
		flightUpdate.setPlane(flight.getPlane());
		
		try {
			return flightRepository.save(flightUpdate);
		} catch (Exception e) {
			throw new FlightUpdatingException(String.format("Error occurred while trying to update flight with id %d.", id));
		}
	}

	@Override
	public Flight deleteFlight(Long id) throws FlightNotFoundException, FlightDeletionException{
		
		Flight flightToBeDeleted;
		try {
			flightToBeDeleted = getFlightById(id);
		} catch(Exception e) {
			throw new FlightNotFoundException(String.format("Flight with id %d could not be found.", id));
		}
		
		flightRepository.delete(flightToBeDeleted);
		
		List<Flight> flights = flightRepository.findAll();
		Optional<Flight> flightById = flights.stream().filter(flight -> flight.getId().equals(id)).findAny();
		
		if(!flightById.isEmpty()) {
			throw new FlightDeletionException(String.format("Error occurred while trying to delete flight with id %d.", id));
		}

		return flightToBeDeleted;
	}

}
