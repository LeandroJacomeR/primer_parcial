package com.procesos.negocio.parcial.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
public class VehicleDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String car;
    private String carModel;
    private String carColor;
    private String carType;
    private String carFuel;
    private String carVin;
}
