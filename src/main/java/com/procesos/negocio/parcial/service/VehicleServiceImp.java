package com.procesos.negocio.parcial.service;

import com.procesos.negocio.parcial.models.Vehicle;
import com.procesos.negocio.parcial.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class VehicleServiceImp implements VehicleService{

    @Value("${external.api.url}")
    private String baseUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Override
    public void saveVehiclesFromApi() {
        ResponseEntity<Vehicle[]> response = restTemplate.getForEntity(baseUrl, Vehicle[].class);
        Vehicle[] vehicles = response.getBody();

        for (Vehicle vehicle : vehicles) {
            vehicleRepository.save(vehicle);
        }
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    @Override
    public Vehicle getVehicle(Long id) {
        return vehicleRepository.findById(id).get();
    }

    @Override
    public Boolean createVehicle(Vehicle vehicle) {
        try {
            vehicleRepository.save(vehicle);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean updateVehicle(Long id, Vehicle vehicle) {
        try {
            Vehicle vehicleBD = vehicleRepository.findById(id).get();

            vehicleBD.setCar(vehicle.getCar());
            vehicleBD.setCarModel(vehicle.getCarModel());
            vehicleBD.setCarColor(vehicle.getCarColor());
            vehicleBD.setCarType(vehicle.getCarType());
            vehicleBD.setCarFuel(vehicle.getCarFuel());
            vehicleBD.setCarVin(vehicle.getCarVin());

            Vehicle vehicleUp = vehicleRepository.save(vehicleBD);
            return true;
        }catch (Exception e) {
            return false;
        }
    }
}
