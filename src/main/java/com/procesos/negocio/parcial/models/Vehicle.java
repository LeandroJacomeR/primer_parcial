package com.procesos.negocio.parcial.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String car;
    @Column(name = "car_model", nullable = false)
    private String carModel;
    @Column(name = "car_color", nullable = false)
    private String carColor;
    @Column(name = "car_type", nullable = false)
    private String carType;
    @Column(name = "car_fuel", nullable = false)
    private String carFuel;
    @Column(name = "car_vin", length = 13, nullable = false)
    private String carVin;



    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    private User user;
}
