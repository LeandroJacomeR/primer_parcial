package com.procesos.negocio.parcial.controllers;

import com.procesos.negocio.parcial.models.Vehicle;
import com.procesos.negocio.parcial.service.VehicleService;
import com.procesos.negocio.parcial.utils.ApiResponse;
import com.procesos.negocio.parcial.utils.Constants;
import com.procesos.negocio.parcial.utils.JWTUtil;
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

    @Autowired
    private JWTUtil jwtUtil;

    private ApiResponse apiResponse;

    @GetMapping("")
    public ResponseEntity<List<Vehicle>> getAllCars(@RequestHeader(value = "Authorization") String token){
        //System.out.println(token);
        try {
            if(!validateToken(token)){
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
    public ResponseEntity<Void> saveVehicles(Long idVehicle, Long idUser, @RequestHeader(value = "Authorization") String token) {
        if(!validateToken(token)){
            return new ResponseEntity(Constants.TOKEN_INVALID, HttpStatus.UNAUTHORIZED);
        }
        vehicleService.saveVehiclesFromApi();
        return new ResponseEntity(Constants.EXTERNAL_DATA_API, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity findVehiclesById(@PathVariable("id") Long id,
                                           @RequestHeader(value = "Authorization") String token){
        try{
            if(!validateToken(token)){
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

        if(!validateToken(token)){
            return new ResponseEntity(Constants.TOKEN_INVALID, HttpStatus.UNAUTHORIZED);
        }
        if (vehicleRes){
            apiResponse = new ApiResponse(Constants.REGISTER_CREATE, "");
            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        }
        apiResponse = new ApiResponse(Constants.REGISTER_BAD, vehicle);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateVehicle(@PathVariable("id") Long id,
                                                @RequestBody Vehicle vehicle,
                                                @RequestHeader(value = "Authorization") String token) {
        try {
            if(!validateToken(token)){
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

    @DeleteMapping("/del")
    public ResponseEntity deleteAll(@RequestHeader(value = "Authorization") String token){

        try {
            if(!validateToken(token)){
                return new ResponseEntity(Constants.TOKEN_INVALID, HttpStatus.UNAUTHORIZED);
            }
            Boolean result = vehicleService.deleteAllVehicles();
            if (!result) {
                apiResponse = new ApiResponse(Constants.REGISTER_NOT_FOUND, "");
                return new ResponseEntity<>(apiResponse, HttpStatus.NO_CONTENT);
            }
            apiResponse = new ApiResponse(Constants.REGISTER_DELETE, "");
            return new ResponseEntity<>(apiResponse, HttpStatus.NO_CONTENT);
        }catch (Exception e) {
            apiResponse = new ApiResponse(Constants.REGISTER_ERROR_DELETE, e.getMessage());
            return new ResponseEntity(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Boolean validateToken(String token){
        try{
            if(jwtUtil.getKey(token) != null){
                return true;
            }
            return  false;
        }catch (Exception e){
            return  false;
        }
    }
}
