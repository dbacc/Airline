package com.project.Airline.botnetDetection.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RecaptchaResponse {

	private boolean success;
    private String challenge_ts;
    private String hostname;
}
