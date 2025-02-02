package com.project.Airline.exceptions.flight;

public class FlightStoringException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public FlightStoringException(String message) {
		super(message);
    }
}
