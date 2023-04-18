package com.procesos.negocio.parcial.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String car;
    @Column(name = "car_model")
    private String carModel;
    @Column(name = "car_color")
    private String carColor;
    @Column(name = "car_type")
    private String carType;
    @Column(name = "car_fuel")
    private String carFuel;
    @Column(name = "car_vin")
    private String carVin;
}
