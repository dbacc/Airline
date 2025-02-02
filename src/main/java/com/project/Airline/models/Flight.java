package com.project.Airline.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.AccessLevel;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;

@Entity
@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Flight extends BaseEntity {
	
	@Column(nullable = false)
	private String departurePlace;
	
	@Column(nullable = false)
	private String arrivalPlace;
	
	@Column(nullable = false)
	private LocalDateTime departureTime;
	
	@Column(nullable = false)
	private LocalDateTime arrivalTime;
	
	@ManyToOne()
	@JoinColumn(name = "plane_id", referencedColumnName = "id")
	private Plane plane;
	
}