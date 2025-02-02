package com.project.Airline.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.project.Airline.exceptions.user.RegisterUserException;
import com.project.Airline.models.User;
import com.project.Airline.services.UserService;

import lombok.SneakyThrows;

@Controller
public class UserController {

	private final UserService userService;
	
	@Autowired
	public UserController(UserService userService) {
		
		this.userService = userService;
	}
	
	@SneakyThrows
	@PostMapping("/register")
	public ResponseEntity<User> registerUser(@RequestBody User user) {
		
		return new ResponseEntity<>(userService.registerUser(user), HttpStatus.CREATED);
	}
	
	@ExceptionHandler(value = RegisterUserException.class)
    private ResponseEntity<String> handleFlightStoringException(RegisterUserException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
	}
}
