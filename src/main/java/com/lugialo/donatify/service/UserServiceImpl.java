package com.lugialo.donatify.service;

import com.lugialo.donatify.dto.UserRegistrationDto;
import com.lugialo.donatify.dto.UserResponseDto;
import com.lugialo.donatify.model.Ong;
import com.lugialo.donatify.model.Role;
import com.lugialo.donatify.model.User;
import com.lugialo.donatify.repository.OngRepository;
import com.lugialo.donatify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final OngRepository ongRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, OngRepository ongRepository)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.ongRepository = ongRepository;
    }

    @Override
    @Transactional
    public UserResponseDto registerUser(UserRegistrationDto registrationDto)
    {
        // Verifica se o usuário já existe pelo email
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        User newUser = new User();
        newUser.setName(registrationDto.getName());
        newUser.setEmail(registrationDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(registrationDto.getPassword())); // Hash da senha
        newUser.setNickname(registrationDto.getNickname());
        newUser.setRole(Role.USER);
        // Associa a ONG se um ID for fornecido
        if (registrationDto.getOngId() != null) {
            Ong ong = ongRepository.findById(registrationDto.getOngId())
                    .orElseThrow(() -> new IllegalArgumentException("ONG com ID " + registrationDto.getOngId() + " não encontrada."));
            newUser.setOng(ong);
        }
        newUser.setPhone(registrationDto.getPhone());
        newUser.setAddress(registrationDto.getAddress());
        // totalPoints é inicializado como 0 por padrão

        User savedUser = userRepository.save(newUser);
        return UserResponseDto.fromEntity(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponseDto> getUserById(long userId)
    {
        return userRepository.findById(userId)
                .map(UserResponseDto::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponseDto> getUserByEmail(String email)
    {
        return userRepository.findByEmail(email)
                .map(UserResponseDto::fromEntity);
    }

    @Override
    @Transactional
    public User getUserEntityById(long id)
    {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com ID: " + id));
    }

    // Método para atualizar os pontos do usuário
    @Transactional
    public void updateUserPoints(long userId, int pointsToAdd) {
        User user = getUserEntityById(userId);
        user.setTotalPoints(user.getTotalPoints() + pointsToAdd);
        userRepository.save(user);
    }




}
