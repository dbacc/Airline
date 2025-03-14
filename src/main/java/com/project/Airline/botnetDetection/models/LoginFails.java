package com.project.Airline.botnetDetection.models;

import java.time.LocalDate;
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
@Table(name = "login_fails")
public class LoginFails extends BaseEntity {

	@Column(nullable = false)
	private LocalDate day;
	
	@Column(nullable = false)
	private LocalTime hour;
	
	@Column(nullable = false)
	private Long failCount;
	
}
