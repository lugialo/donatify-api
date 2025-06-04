package com.lugialo.donatify.service;

import com.lugialo.donatify.dto.UserRegistrationDto;
import com.lugialo.donatify.dto.UserResponseDto;
import com.lugialo.donatify.model.User;

import java.util.Optional;

public interface UserService {
    UserResponseDto registerUser(UserRegistrationDto registrationDto);
    Optional<UserResponseDto> getUserById(long userId);
    Optional<UserResponseDto> getUserByEmail(String email);
    // Se houver outros métodos relacionados a usuários, eles podem ser adicionados aqui
    User getUserEntityById(long id);


}
