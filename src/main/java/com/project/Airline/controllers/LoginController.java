package com.project.Airline.controllers;

import java.io.IOException;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.Airline.botnetDetection.detection.ATODetection;
import com.project.Airline.botnetDetection.services.RecaptchaService;
import com.project.Airline.exceptions.user.RegisterUserException;
import com.project.Airline.services.LoginService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;

@Controller
public class LoginController {

	private final ATODetection atoDetection;
	private final RecaptchaService recaptchaService;
	private final LoginService loginService;
	
	@Autowired
	public LoginController(ATODetection atoDetection, RecaptchaService recaptchaService, LoginService loginService) {

		this.atoDetection = atoDetection;
		this.recaptchaService = recaptchaService;
		this.loginService = loginService;
	}
	
	@GetMapping("/login")
	public String login(Model model) {
		
		model.addAttribute("ATODetected", atoDetection.getATODetected());
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("isAuthenticated", (auth != null && auth.isAuthenticated() && auth instanceof UsernamePasswordAuthenticationToken));
		
		return "login";
	}
	
	@SneakyThrows
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password,
			 @RequestParam(name = "g-recaptcha-response") String recaptchaResponse, HttpServletRequest request) {

    	if(atoDetection.getATODetected()) {
    		
    		if(!recaptchaService.verifyRecaptcha(recaptchaResponse)) {
    		
    			return "redirect:/login?recaptcha_error";
    		}
    	}
    	
    	if(loginService.attemptAuthentication(username, password, request)) {
    		return "redirect:/";
    	}
    	
    	return "redirect:/login?error";
    }

	@ExceptionHandler(value = IOException.class)
    private ResponseEntity<String> loginAttemptException(RegisterUserException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
	}
}

