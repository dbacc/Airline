package com.project.Airline.services.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.project.Airline.dto.FlightDTO;
import com.project.Airline.models.Flight;


@Component
public class FlightMapperImpl implements FlightMapper {

	@Override
	public FlightDTO toDTO(Flight flight) {
		FlightDTO flightDTO = new FlightDTO();
		flightDTO.setId(flight.getId());
		flightDTO.setDeparturePlace(flight.getDeparturePlace());
		flightDTO.setArrivalPlace(flight.getArrivalPlace());
		flightDTO.setDepartureTime(flight.getDepartureTime());
		flightDTO.setArrivalTime(flight.getArrivalTime());
		
		return flightDTO;
	}
	
	@Override
	public Flight toEntity(FlightDTO flightDTO){
		Flight flight = new Flight();
		flight.setId(flightDTO.getId());
		flight.setDeparturePlace(flightDTO.getDeparturePlace());
		flight.setArrivalPlace(flightDTO.getArrivalPlace());
		flight.setDepartureTime(flightDTO.getDepartureTime());
		flight.setArrivalTime(flightDTO.getArrivalTime());
		
		return flight;
	}
	
	public List<FlightDTO> toListDTO(List<Flight> flights){
		
		return flights.stream().map(x -> toDTO(x)).collect(Collectors.toList());
	}
	
	public List<Flight> toListEntity(List<FlightDTO> flightsDTO){
		
		return flightsDTO.stream().map(x -> toEntity(x)).collect(Collectors.toList());
	}
	
	public List<FlightDTO> toListDTO(Page<Flight> flights) {
		return flights.stream().map(x -> toDTO(x)).collect(Collectors.toList());
	}
	
}
