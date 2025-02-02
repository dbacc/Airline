package com.project.Airline.exceptions.flight;

public class FlightUpdatingException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public FlightUpdatingException(String message) {
		super(message);
    }
}
