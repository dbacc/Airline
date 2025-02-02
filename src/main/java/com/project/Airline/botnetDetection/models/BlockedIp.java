package com.project.Airline.botnetDetection.models;

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
@Table(name = "blocked_ips")
public class BlockedIp extends BaseEntity {

	@Column(nullable = false)
	private String ip;
	
}
