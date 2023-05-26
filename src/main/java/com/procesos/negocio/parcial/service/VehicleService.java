package com.procesos.negocio.parcial.service;

import com.procesos.negocio.parcial.models.Vehicle;

import java.util.List;

public interface VehicleService {

    List<Vehicle> getAllVehicles();
    Vehicle getVehicle(Long id);
    Boolean createVehicle(Vehicle vehicle);
    Boolean updateVehicle(Long id, Vehicle vehicle);

    Boolean deleteAllVehicles();

    Boolean saveVehiclesFromApi(Long idVehicle, Long idUser);
}
