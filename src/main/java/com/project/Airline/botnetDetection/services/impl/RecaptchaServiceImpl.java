package com.project.Airline.botnetDetection.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.project.Airline.botnetDetection.models.RecaptchaResponse;
import com.project.Airline.botnetDetection.services.RecaptchaService;

@Service
public class RecaptchaServiceImpl implements RecaptchaService {

	@Value("${google.recaptcha.secret-key}")
    private String recaptchaSecretKey;

    private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    public boolean verifyRecaptcha(String recaptchaRequest) {
        RestTemplate restTemplate = new RestTemplate();

        String url = RECAPTCHA_VERIFY_URL +
                "?secret=" + recaptchaSecretKey +
                "&response=" + recaptchaRequest;

        RecaptchaResponse captchaResponse = restTemplate.postForObject(url, null, RecaptchaResponse.class);

        return captchaResponse != null && captchaResponse.isSuccess();
    }
}
