package com.project.Airline.botnetDetection.models;

import java.time.LocalTime;

import com.project.Airline.models.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "traffic_profiles")
public class TrafficProfile extends BaseEntity {

	@Column(nullable = false, unique = true)
	private LocalTime hour;
	
	@Column(nullable = false)
	private Long traffic;
}
