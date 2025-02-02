package com.project.Airline.botnetDetection.countermeasure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.stereotype.Component;

import com.project.Airline.botnetDetection.models.BlockedIp;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BlockedIpsFilter implements Filter {

	private List<String> blockedIps = new ArrayList<>();
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
			throws IOException, ServletException {
		
		for(String ip : blockedIps) {
			
			if(ip.equals(request.getRemoteAddr())) {
				return;
			}
		}
		chain.doFilter(request, response);
	}

	public void addIps(List<String> ips) {
		
		blockedIps.addAll(ips);
	}
	
	public void updateIps(List<BlockedIp> ips) {
		
		blockedIps = ips.parallelStream().map(ip -> ip.getIp()).collect(Collectors.toList());;
	}
}
