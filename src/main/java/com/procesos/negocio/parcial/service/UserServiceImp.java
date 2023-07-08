package com.procesos.negocio.parcial.service;

import com.procesos.negocio.parcial.models.User;
import com.procesos.negocio.parcial.repository.UserRepository;
import com.procesos.negocio.parcial.utils.Constants;
import com.procesos.negocio.parcial.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getUser(Long id){
        return userRepository.findById(id).get();
    }

    @Override
    public Boolean createUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return true;
        }catch (Exception e){
            return false;
        }

    }

    @Override
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    @Override
    public Boolean updateUser(Long id, User user) {
        try {
            User userBD = userRepository.findById(id).get();

            userBD.setFirstName(user.getFirstName());
            userBD.setLastName(user.getLastName());
            userBD.setBirthday(user.getBirthday());
            userBD.setAddress(user.getAddress());
            userBD.setPassword(passwordEncoder.encode(user.getPassword()));
            User userUp = userRepository.save(userBD);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public Boolean deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public String login(User user) {
        Optional<User> userBD = userRepository.findByEmail(user.getEmail());
        if (userBD.isEmpty()){
            throw new RuntimeException(Constants.USER_NOT_FOUND);
        }
        if (!passwordEncoder.matches(user.getPassword(), userBD.get().getPassword())) {
            throw new RuntimeException(Constants.PASSWORD_INCORRECT);
        }

        return jwtUtil.create(String.valueOf(userBD.get().getId()),
                                String.valueOf(userBD.get().getEmail()));
    }
}





