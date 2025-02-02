package com.project.Airline.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.Airline.dto.FlightDTO;
import com.project.Airline.exceptions.flight.FlightNotFoundException;
import com.project.Airline.exceptions.flight.FlightParametersException;
import com.project.Airline.services.FlightService;

import lombok.SneakyThrows;

import java.util.List;

@Controller
public class HomeController {

	private final FlightService flightService;
	
	@Autowired
	public HomeController(FlightService flightService) {
		
		this.flightService = flightService;
	}
	
    @GetMapping("/")
    public String home(Model model) {
        
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	model.addAttribute("isAuthenticated", (auth != null && auth.isAuthenticated() && auth instanceof UsernamePasswordAuthenticationToken));
    	return "index";
    }

	@SneakyThrows
    @GetMapping("/search")
    public String searchFlights(
            @RequestParam("departureAirport") String departureAirport,
            @RequestParam("destinationAirport") String destinationAirport,
            @RequestParam("departureDate") String departureDate,
            @RequestParam("passengers") int passengers,
            Model model) {

        List<FlightDTO> searchResults = (flightService.getFlights(departureAirport, destinationAirport, departureDate));

        model.addAttribute("results", searchResults);
        model.addAttribute("departureAirport", departureAirport);
        model.addAttribute("destinationAirport", destinationAirport);
        model.addAttribute("departureDate", departureDate);
        model.addAttribute("passengers", passengers);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	model.addAttribute("isAuthenticated", (auth != null && auth.isAuthenticated() && auth instanceof UsernamePasswordAuthenticationToken));
        
        return "index";
    }

	@GetMapping("/flight")
	public String showFlight(@RequestParam("id") Long id, Model model) {
		
		model.addAttribute("id", id);
		
		return "flight";
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
}
