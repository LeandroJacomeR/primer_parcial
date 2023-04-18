package com.procesos.negocio.parcial.controllers;

import com.procesos.negocio.parcial.models.Vehicle;
import com.procesos.negocio.parcial.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/parcial")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/vehicles")
    public ResponseEntity<List<Vehicle>> getAllCars(){
        try {
            return new ResponseEntity<>(vehicleService.getAllVehicles(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity("No se encontraron vehiculos", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/save/vehicles")
    public ResponseEntity<Void> saveVehicles() {
        vehicleService.saveVehiclesFromApi();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/vehicle/{id}")
    public ResponseEntity findVehiclesById(@PathVariable("id") Long id){
        Map response = new HashMap();
        try{
            return new ResponseEntity(vehicleService.getVehicle(id), HttpStatus.OK);
        }catch (Exception e){
            response.put("status", "404");
            response.put("message","No se encontraron vehiculos!");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/vehicle")
    public ResponseEntity saveVehicle(@RequestBody Vehicle vehicle){
        Map response = new HashMap();
        Boolean vehicleRes = vehicleService.createVehicle(vehicle);

        if (vehicleRes){
            response.put("status", "201");
            response.put("message", "Se creo el vehiculo");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        response.put("status", "400");
        response.put("message", "Error al crear un vehiculo");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/vehicle/{id}")
    public ResponseEntity<String> updateVehicle(@PathVariable("id") Long id, @RequestBody Vehicle vehicle) {
        try {
            Boolean result = vehicleService.updateVehicle(id, vehicle);
            if (result) {
                return new ResponseEntity<>("Vehiculo con id " + id + " ha sido actualizado", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Vehiculo con id " + id + " no se encuentra registrado", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al actualizar el vehiculo: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
