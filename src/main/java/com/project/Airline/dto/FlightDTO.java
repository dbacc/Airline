package com.project.Airline.dto;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class FlightDTO {

	private Long id;
	private String departurePlace;
	private String arrivalPlace;
	private LocalDateTime departureTime;
	private LocalDateTime arrivalTime;
}