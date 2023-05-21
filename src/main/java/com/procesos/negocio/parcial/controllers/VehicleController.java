package com.procesos.negocio.parcial.controllers;

import com.procesos.negocio.parcial.models.Vehicle;
import com.procesos.negocio.parcial.service.VehicleService;
import com.procesos.negocio.parcial.utils.ApiResponse;
import com.procesos.negocio.parcial.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parcial/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    private ApiResponse apiResponse;

    @GetMapping("")
    public ResponseEntity<List<Vehicle>> getAllCars(){
        try {
            apiResponse = new ApiResponse(Constants.REGISTER_LIST, vehicleService.getAllVehicles());
            return new ResponseEntity(apiResponse, HttpStatus.OK);
        }catch (Exception e){
            apiResponse = new ApiResponse(Constants.REGISTER_NOT_FOUND, "");
            return new ResponseEntity(apiResponse, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/save/api")
    public ResponseEntity<Void> saveVehicles() {
        vehicleService.saveVehiclesFromApi();
        return new ResponseEntity(Constants.EXTERNAL_DATA_API, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity findVehiclesById(@PathVariable("id") Long id){
        try{
            apiResponse = new ApiResponse(Constants.REGISTER_FOUND, vehicleService.getVehicle(id));
            return new ResponseEntity(apiResponse, HttpStatus.OK);
        }catch (Exception e){
            apiResponse = new ApiResponse(Constants.REGISTER_NOT_FOUND, "");
            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("")
    public ResponseEntity saveVehicle(@RequestBody Vehicle vehicle){
        Boolean vehicleRes = vehicleService.createVehicle(vehicle);

        if (vehicleRes){
            apiResponse = new ApiResponse(Constants.REGISTER_CREATE, "");
            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        }
        apiResponse = new ApiResponse(Constants.REGISTER_BAD, vehicle);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateVehicle(@PathVariable("id") Long id, @RequestBody Vehicle vehicle) {
        try {
            Boolean result = vehicleService.updateVehicle(id, vehicle);
            if (result) {
                apiResponse = new ApiResponse(Constants.REGISTER_UPDATE, "");
                return new ResponseEntity(apiResponse, HttpStatus.OK);
            } else {
                apiResponse = new ApiResponse(Constants.REGISTER_NOT_FOUND, "");
                return new ResponseEntity(apiResponse, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            apiResponse = new ApiResponse(Constants.REGISTER_ERROR_UPDATE, e.getMessage());
            return new ResponseEntity(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/del")
    public ResponseEntity deleteAll(){
        Boolean result = vehicleService.deleteAllVehicles();
        try {
            if (!result) {
                apiResponse = new ApiResponse(Constants.REGISTER_NOT_FOUND, "");
                return new ResponseEntity<>(apiResponse, HttpStatus.NO_CONTENT);
            }else {
                apiResponse = new ApiResponse(Constants.REGISTER_DELETE, "");
                return new ResponseEntity<>(apiResponse, HttpStatus.NO_CONTENT);
            }
        }catch (Exception e) {
            apiResponse = new ApiResponse(Constants.REGISTER_ERROR_DELETE, e.getMessage());
            return new ResponseEntity(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
