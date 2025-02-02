package com.project.Airline.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
public class Plane extends BaseEntity {

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(length = 4)
    private Integer year;
    
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "plane")
    private List<Flight> flights;

}