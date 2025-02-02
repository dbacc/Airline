package com.project.Airline.botnetDetection.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.Airline.botnetDetection.detection.ATODetection;
import com.project.Airline.botnetDetection.dto.BlockedIpDTOList;
import com.project.Airline.botnetDetection.services.BlockedIpService;

@Controller
@PreAuthorize("hasRole('ADMIN')")
public class AntiBotnetController {

	private final BlockedIpService blockedIpService;
	private final ATODetection atoDetection;
	
	@Autowired
	public AntiBotnetController(BlockedIpService blockedIpService, ATODetection atoDetection) {
		
		this.blockedIpService = blockedIpService;
		this.atoDetection = atoDetection;
	}
	
	@GetMapping("/antiBotnet")
    public String antiBotnet(Model model) {
        
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	model.addAttribute("isAuthenticated", (auth != null && auth.isAuthenticated() && auth instanceof UsernamePasswordAuthenticationToken));
    	
    	BlockedIpDTOList ipList = blockedIpService.getIpsDTO();
    	model.addAttribute("ipList", ipList);
    	model.addAttribute("ATODetected", atoDetection.getATODetected());
    	
    	return "antiBotnet";
    }
	
	@PostMapping("/antiBotnet/unblock")
    public String unblock(@ModelAttribute BlockedIpDTOList ipList, Model model) {

		blockedIpService.deleteIps(ipList);
    	
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	model.addAttribute("isAuthenticated", (auth != null && auth.isAuthenticated() && auth instanceof UsernamePasswordAuthenticationToken));
    	
    	BlockedIpDTOList newIpList = blockedIpService.getIpsDTO();
    	model.addAttribute("ipList", newIpList);
    	model.addAttribute("ATODetected", atoDetection.getATODetected());
    	
    	return "redirect:/antiBotnet";
    }
	
	@PostMapping("/antiBotnet/unblock_all")
	public String unblockAll(Model model) {
		
		blockedIpService.deleteAllIps();
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	model.addAttribute("isAuthenticated", (auth != null && auth.isAuthenticated() && auth instanceof UsernamePasswordAuthenticationToken));
    	
    	BlockedIpDTOList newIpList = blockedIpService.getIpsDTO();
    	model.addAttribute("ipList", newIpList);
    	model.addAttribute("ATODetected", atoDetection.getATODetected());
    	
    	return "redirect:/antiBotnet";
	}
	
	@PostMapping("/antiBotnet/recaptcha")
	public String recaptcha(Model model) {
		
		atoDetection.ATODetectedSwitch();
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	model.addAttribute("isAuthenticated", (auth != null && auth.isAuthenticated() && auth instanceof UsernamePasswordAuthenticationToken));
    	
    	BlockedIpDTOList newIpList = blockedIpService.getIpsDTO();
    	model.addAttribute("ipList", newIpList);
    	model.addAttribute("ATODetected", atoDetection.getATODetected());
    	
    	return "redirect:/antiBotnet";
	}
}
