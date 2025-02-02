package com.project.Airline.exceptions.plane;

public class PlaneNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public PlaneNotFoundException(String message) {
		super(message);
    }
}
