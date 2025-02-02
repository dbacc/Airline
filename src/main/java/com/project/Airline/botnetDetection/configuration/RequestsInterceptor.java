package com.project.Airline.botnetDetection.configuration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.project.Airline.botnetDetection.models.IpTraffic;
import com.project.Airline.botnetDetection.models.Traffic;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class RequestsInterceptor implements HandlerInterceptor {

	private Traffic traffic = new Traffic();
	private List<IpTraffic> ipTrafficList = new ArrayList<IpTraffic>();
	
	public RequestsInterceptor() {

		traffic.setTraffic(0L);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		if(traffic.getTraffic() == 0L) {
			traffic.setDay(LocalDate.now());
			traffic.setMinute(LocalTime.now().withNano(0).withSecond(0));
		}
		traffic.setTraffic(traffic.getTraffic() + 1);
		
		boolean isIpInList = false;
		for(IpTraffic ipTraffic : ipTrafficList) {
			
			if(ipTraffic.getIp().equals(request.getRemoteAddr())) {
				isIpInList = true;
				ipTraffic.setTraffic(traffic.getTraffic() + 1L);
			}
		}
		if(!isIpInList) {
			ipTrafficList.add(new IpTraffic(request.getRemoteAddr(), LocalTime.now().withNano(0).withSecond(0), 1L));
		}
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
}
