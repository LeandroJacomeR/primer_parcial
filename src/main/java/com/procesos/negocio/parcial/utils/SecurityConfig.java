package com.procesos.negocio.parcial.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {
    @Autowired
    private JWTUtil jwtUtil;
    public Boolean validateToken(String token){
        try{
            if(jwtUtil.getKey(token) != null){
                return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }
}
