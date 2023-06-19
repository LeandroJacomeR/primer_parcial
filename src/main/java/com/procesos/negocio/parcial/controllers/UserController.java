package com.procesos.negocio.parcial.controllers;

import com.procesos.negocio.parcial.models.User;
import com.procesos.negocio.parcial.service.UserService;
import com.procesos.negocio.parcial.utils.ApiResponse;
import com.procesos.negocio.parcial.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    private ApiResponse apiResponse;

    Map data = new HashMap();

    @GetMapping(value = "/{id}")
    public ResponseEntity findUserById(@PathVariable Long id){
        try{
            apiResponse = new ApiResponse(Constants.REGISTER_FOUND, userService.getUser(id));
            return new ResponseEntity(apiResponse, HttpStatus.OK);
        }catch (Exception e){
            apiResponse = new ApiResponse(Constants.REGISTER_NOT_FOUND, "");
            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "")
    public ResponseEntity saveUser(@RequestBody User user){
        Boolean userRes = userService.createUser(user);

        if (userRes){
            apiResponse = new ApiResponse(Constants.REGISTER_CREATE, "");
            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        }
        apiResponse = new ApiResponse(Constants.REGISTER_BAD, user);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "")
    public ResponseEntity allUser(){
        try{
            apiResponse = new ApiResponse(Constants.REGISTER_LIST, userService.allUsers());
            return new ResponseEntity(apiResponse, HttpStatus.OK);
        }catch (Exception e){
            apiResponse = new ApiResponse(Constants.REGISTER_NOT_FOUND, "");
            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        try {
            Boolean result = userService.updateUser(id, user);
            if (result) {
                apiResponse = new ApiResponse(Constants.REGISTER_UPDATE, "");
                return new ResponseEntity<>(apiResponse, HttpStatus.OK);
            } else {
                apiResponse = new ApiResponse(Constants.REGISTER_NOT_FOUND, "");
                return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            apiResponse = new ApiResponse(Constants.REGISTER_ERROR_UPDATE, e.getMessage());
            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
