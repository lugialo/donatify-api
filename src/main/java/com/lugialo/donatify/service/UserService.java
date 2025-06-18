package com.lugialo.donatify.service;

import com.lugialo.donatify.dto.AdminUserUpdateDto;
import com.lugialo.donatify.dto.UserProfileUpdateDto;
import com.lugialo.donatify.dto.UserRegistrationDto;
import com.lugialo.donatify.dto.UserResponseDto;
import com.lugialo.donatify.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    // --- Métodos Públicos / de Usuário ---
    UserResponseDto registerUser(UserRegistrationDto registrationDto);

    Optional<UserResponseDto> getUserById(long userId);

    Optional<UserResponseDto> getUserByEmail(String email);

    // --- Métodos Internos / de Serviço ---
    User getUserEntityById(long id);

    User findUserEntityByEmail(String email);

    // --- Métodos de Administrador ---
    List<UserResponseDto> getAllUsers();

    UserResponseDto updateUserByAdmin(Long userId, AdminUserUpdateDto updateDto);

    void deleteUser(Long userId);

    /**
     * Atualiza o perfil do usuário autenticado.
     * @param userEmail O email do usuário vindo do token de autenticação.
     * @param updateDto Os dados a serem atualizados.
     * @return O DTO do usuário com as informações atualizadas.
     */
    UserResponseDto updateUserProfile(String userEmail, UserProfileUpdateDto updateDto);
}
