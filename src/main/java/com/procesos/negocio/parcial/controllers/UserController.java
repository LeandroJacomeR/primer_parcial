package com.procesos.negocio.parcial.controllers;

import com.procesos.negocio.parcial.models.User;
import com.procesos.negocio.parcial.service.UserService;
import com.procesos.negocio.parcial.utils.ApiResponse;
import com.procesos.negocio.parcial.utils.Constants;
import com.procesos.negocio.parcial.utils.SecurityConfig;
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

    @Autowired
    private SecurityConfig securityConfig;

    @GetMapping(value = "/{id}")
    public ResponseEntity findUserById(@PathVariable Long id, @RequestHeader(name = "Authorization") String token){
        if(!securityConfig.validateToken(token)){
            return new ResponseEntity("Token no valido!", HttpStatus.UNAUTHORIZED);
        }
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
        //System.out.println(user);

        if (userRes){
            apiResponse = new ApiResponse(Constants.REGISTER_CREATE, user);
            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        }
        apiResponse = new ApiResponse(Constants.REGISTER_BAD, "");
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "")
    public ResponseEntity allUser(@RequestHeader(name = "Authorization") String token){
        if(!securityConfig.validateToken(token)){
            return new ResponseEntity("Token no valido!", HttpStatus.UNAUTHORIZED);
        }
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

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") Long id, @RequestHeader(value = "Authorization") String token) {
        try {
            Boolean result = userService.deleteUser(id);
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
