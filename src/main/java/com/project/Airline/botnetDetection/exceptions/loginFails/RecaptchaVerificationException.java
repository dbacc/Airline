package com.project.Airline.botnetDetection.exceptions.loginFails;

import org.springframework.web.bind.annotation.ExceptionHandler;

public class RecaptchaVerificationException extends Exception {

	private static final long serialVersionUID = 1L;

	public RecaptchaVerificationException(String message) {

		super(message + " test");
	}
	
	@ExceptionHandler(value = RecaptchaVerificationException.class)
	public String handleRecaptchaVerificationException(RecaptchaVerificationException e) {
		
		System.out.println("Avise2");
		return "redirect:login?recaptcha_error";
	}
}
