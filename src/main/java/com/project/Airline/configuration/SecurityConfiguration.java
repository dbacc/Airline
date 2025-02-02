package com.project.Airline.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableScheduling
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {
	
	private AuthenticationManager authenticationManager;
	
	private UserDetailsService userDetailsService;
	
	@Autowired
	public SecurityConfiguration(UserDetailsService userDetailsService) {

		this.userDetailsService = userDetailsService;
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(authz -> authz
					.requestMatchers("/antiBotnet/**").hasRole("ADMIN")
					.requestMatchers("/").permitAll()
					.anyRequest().permitAll()
			)
			.httpBasic(auth -> auth.authenticationEntryPoint(new CustomAuthenticationEntryPoint()));
			
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		
		return authenticationProvider;
	}
	
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        this.authenticationManager = http.getSharedObject(AuthenticationManagerBuilder.class)
            .authenticationProvider(authenticationProvider())
            .build();
        
        return this.authenticationManager;
    }
}
