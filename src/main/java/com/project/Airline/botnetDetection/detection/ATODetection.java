package com.project.Airline.botnetDetection.detection;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.project.Airline.botnetDetection.models.LoginFails;
import com.project.Airline.botnetDetection.services.LoginFailsService;
import com.project.Airline.botnetDetection.services.LoginProfileService;

@EnableAsync
@Component
public class ATODetection {
	
	private boolean ATODetected = false;
	
	private final LoginFailsService loginFailsService;
	private final LoginProfileService loginProfileService;
	
	@Autowired
	public ATODetection(LoginFailsService loginFailsService, LoginProfileService loginProfileService) {

		this.loginFailsService = loginFailsService;
		this.loginProfileService = loginProfileService;
	}
	
	@Async
	@Scheduled(cron = "0 0 * * * *") /* 0 * * * * */
	public void detection() {
		
		LocalTime time = LocalTime.now().withMinute(0).withSecond(0).withNano(0).minusHours(1); /*.minusHours(1)*/
		LocalDate day;
		if(time.equals(LocalTime.of(0, 0, 0))) {
			day = LocalDate.now().minusDays(1);
		} else {
			day = LocalDate.now();
		}
		
		List<LoginFails> currentLoginFails = loginFailsService.getLoginFailsByHour(time);
		currentLoginFails = currentLoginFails.stream()
				.filter(attempts -> attempts.getDay().equals(day)).collect(Collectors.toList());
		
		double loginFailsThreshold;
		try {
			loginFailsThreshold = loginProfileService.getLoginProfile(time).getFailAttempts() * 1.25;
		} catch(Exception e) {
			return;
		}
		
		if(!currentLoginFails.isEmpty()) {

			if (currentLoginFails.get(0).getFailCount() > loginFailsThreshold /* || currentLoginFails.get(0).getFailCount() > 1 */) {
				System.out.println("ATO attack detected");
				
				ATODetected = true;
			}
		}
	}

	public void ATODetectedSwitch() {
		
		ATODetected = !ATODetected;
	}
	
	public boolean getATODetected() {
		return ATODetected;
	}

	public void setATODetected(boolean atoDetected) {
		ATODetected = atoDetected;
	}
	
}
