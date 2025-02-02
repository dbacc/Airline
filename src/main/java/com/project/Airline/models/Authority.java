package com.project.Airline.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "authorities")
public class Authority extends BaseEntity {
	
	@OneToOne()
	@JoinColumn(name = "username", referencedColumnName = "username", nullable = false)
	private User user;
	
	@Column(nullable = false)
	private String role;
}
