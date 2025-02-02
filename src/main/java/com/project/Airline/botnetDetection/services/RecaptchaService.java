package com.project.Airline.botnetDetection.services;

public interface RecaptchaService {

	boolean verifyRecaptcha(String recaptchaResponse);
}
