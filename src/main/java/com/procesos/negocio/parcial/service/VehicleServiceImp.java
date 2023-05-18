package com.procesos.negocio.parcial.service;

import com.procesos.negocio.parcial.models.Vehicle;
import com.procesos.negocio.parcial.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleServiceImp implements VehicleService{

    @Value("${external.api.url}")
    private String baseUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    @Override
    public void saveVehiclesFromApi() {

        List<Vehicle> vehicles = vehicleRepository.findAll();

        Vehicle[] response = restTemplate.getForObject(baseUrl, Vehicle[].class);

        List<String> localVins = vehicles.stream()
                .map(Vehicle::getCarVin)
                .collect(Collectors.toList());

        List<Vehicle> externalVehicles = Arrays.stream(response)
                .filter(vehicle -> !localVins.contains(vehicle.getCarVin()))
                .collect(Collectors.toList());

        vehicleRepository.saveAll(externalVehicles);
    }

    @Override
    public Boolean deleteAllVehicles() {
        try {
            vehicleRepository.deleteAll();
            return true;
        }catch (Exception e) {
            return false;
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

            Vehicle vehicleUp = vehicleRepository.save(vehicleBD);
            return true;
        }catch (Exception e) {
            return false;
        }
    }
}
