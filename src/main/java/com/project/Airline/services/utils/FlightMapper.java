package com.project.Airline.services.utils;

import java.util.List;

import org.springframework.data.domain.Page;

import com.project.Airline.dto.FlightDTO;
import com.project.Airline.models.Flight;

public interface FlightMapper {

	FlightDTO toDTO(Flight flight);
	
	Flight toEntity(FlightDTO flight);
	
	List<FlightDTO> toListDTO(List<Flight> flights);
	
	List<Flight> toListEntity(List<FlightDTO> flightsDTO);
	
	List<FlightDTO> toListDTO(Page<Flight> flights);
	
}
