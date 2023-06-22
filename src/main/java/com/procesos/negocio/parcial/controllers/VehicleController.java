package com.procesos.negocio.parcial.controllers;

import com.procesos.negocio.parcial.dto.VehicleRequestDTO;
import com.procesos.negocio.parcial.models.Vehicle;
import com.procesos.negocio.parcial.service.VehicleService;
import com.procesos.negocio.parcial.utils.ApiResponse;
import com.procesos.negocio.parcial.utils.Constants;
import com.procesos.negocio.parcial.utils.JWTUtil;
import com.procesos.negocio.parcial.utils.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicle")
@CrossOrigin
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private SecurityConfig securityConfig;

    private ApiResponse apiResponse;

    @GetMapping("")
    public ResponseEntity<List<Vehicle>> getAllCars(@RequestHeader(value = "Authorization") String token){
        //System.out.println(token);
        try {
            if(!securityConfig.validateToken(token)){
                return new ResponseEntity(Constants.TOKEN_INVALID, HttpStatus.UNAUTHORIZED);
            }
            apiResponse = new ApiResponse(Constants.REGISTER_LIST, vehicleService.getAllVehicles());
            return new ResponseEntity(apiResponse, HttpStatus.OK);
        }catch (Exception e){
            apiResponse = new ApiResponse(Constants.REGISTER_NOT_FOUND, "");
            return new ResponseEntity(apiResponse, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/save/api")
    public ResponseEntity<String> saveVehicles(@RequestBody VehicleRequestDTO requestDTO, @RequestHeader(value = "Authorization") String token) {
        if(!securityConfig.validateToken(token)){
            return new ResponseEntity(Constants.TOKEN_INVALID, HttpStatus.UNAUTHORIZED);
        }
        Boolean apiSave = vehicleService.saveVehiclesFromApi(requestDTO.getIdVehicle(), requestDTO.getIdUser());
        if (apiSave){
            return new ResponseEntity(Constants.REGISTER_CREATE, HttpStatus.CREATED);
        }
        return new ResponseEntity(Constants.REGISTER_BAD, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    public ResponseEntity findVehiclesById(@PathVariable("id") Long id,
                                           @RequestHeader(value = "Authorization") String token){
        try{
            if(!securityConfig.validateToken(token)){
                return new ResponseEntity(Constants.TOKEN_INVALID, HttpStatus.UNAUTHORIZED);
            }
            apiResponse = new ApiResponse(Constants.REGISTER_FOUND, vehicleService.getVehicle(id));
            return new ResponseEntity(apiResponse, HttpStatus.OK);
        }catch (Exception e){
            apiResponse = new ApiResponse(Constants.REGISTER_NOT_FOUND, "");
            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("")
    public ResponseEntity saveVehicle(@RequestBody Vehicle vehicle,
                                      @RequestHeader(value = "Authorization") String token){
        Boolean vehicleRes = vehicleService.createVehicle(vehicle);

        if(!securityConfig.validateToken(token)){
            return new ResponseEntity(Constants.TOKEN_INVALID, HttpStatus.UNAUTHORIZED);
        }
        if (vehicleRes){
            apiResponse = new ApiResponse(Constants.REGISTER_CREATE, vehicle);
            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        }
        apiResponse = new ApiResponse(Constants.REGISTER_BAD, "");
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateVehicle(@PathVariable("id") Long id,
                                                @RequestBody Vehicle vehicle,
                                                @RequestHeader(value = "Authorization") String token) {
        try {
            if(!securityConfig.validateToken(token)){
                return new ResponseEntity(Constants.TOKEN_INVALID, HttpStatus.UNAUTHORIZED);
            }
            Boolean result = vehicleService.updateVehicle(id, vehicle);

            if (result) {
                apiResponse = new ApiResponse(Constants.REGISTER_UPDATE, "");
                return new ResponseEntity(apiResponse, HttpStatus.OK);
            }
            apiResponse = new ApiResponse(Constants.REGISTER_NOT_FOUND, "");
            return new ResponseEntity(apiResponse, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            apiResponse = new ApiResponse(Constants.REGISTER_ERROR_UPDATE, e.getMessage());
            return new ResponseEntity(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteVehicle(@PathVariable("id") Long id, @RequestHeader(value = "Authorization") String token){
        try {
            Boolean result = vehicleService.deleteVehicle(id);
            if (result) {
                apiResponse = new ApiResponse(Constants.REGISTER_DELETE, "");
                return new ResponseEntity<>(apiResponse, HttpStatus.OK);
            } else {
                apiResponse = new ApiResponse(Constants.REGISTER_NOT_FOUND, "");
                return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            apiResponse = new ApiResponse(Constants.REGISTER_ERROR_DELETE, e.getMessage());
            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
