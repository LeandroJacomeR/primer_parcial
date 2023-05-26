package com.procesos.negocio.parcial.service;

import com.procesos.negocio.parcial.dto.VehicleDTO;
import com.procesos.negocio.parcial.models.User;
import com.procesos.negocio.parcial.models.Vehicle;
import com.procesos.negocio.parcial.repository.UserRepository;
import com.procesos.negocio.parcial.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.persistence.NonUniqueResultException;
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
    private UserRepository userRepository;

    @Override
    public Boolean saveVehiclesFromApi(Long idVehicle, Long idUser) {

        User user = userRepository.findById(idUser).get();
        String url = baseUrl + "/" + idVehicle;
        ResponseEntity<VehicleDTO> response = restTemplate.getForEntity(url, VehicleDTO.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            VehicleDTO vehicleDTO = response.getBody();
            boolean vinExists = vehicleRepository.existsByCarVin(vehicleDTO.getCarVin());

                if (!vinExists) {
                    // El VIN ya existe en la base de datos
                    return false;
                }

                Vehicle vehicle = new Vehicle();
                vehicle.setCar(vehicleDTO.getCar());
                vehicle.setCarModel(vehicleDTO.getCarModel());
                vehicle.setCarColor(vehicleDTO.getCarColor());
                vehicle.setCarType(vehicleDTO.getCarType());
                vehicle.setCarFuel(vehicleDTO.getCarFuel());
                vehicle.setCarVin(vehicleDTO.getCarVin());
                vehicle.setUser(user);
                vehicleRepository.save(vehicle);

                return true;

        }
        return false;
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
            Vehicle existingVehicle = vehicleRepository.findByCarVin(vehicle.getCarVin());

            if (existingVehicle == null) {
                vehicleRepository.save(vehicle);
                return true;
            }else{
                return false;
            }

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
