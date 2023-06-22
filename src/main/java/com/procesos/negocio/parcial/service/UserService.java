package com.procesos.negocio.parcial.service;

import com.procesos.negocio.parcial.models.User;

import java.util.List;

public interface UserService {
    User getUser(Long id);
    Boolean createUser(User user);
    List<User> allUsers();
    Boolean updateUser(Long id, User user);
    Boolean deleteUser(Long id);
    String login(User user);
}
