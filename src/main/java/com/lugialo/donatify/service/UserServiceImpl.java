package com.lugialo.donatify.service;

import com.lugialo.donatify.dto.AdminUserUpdateDto;
import com.lugialo.donatify.dto.UserProfileUpdateDto;
import com.lugialo.donatify.dto.UserRegistrationDto;
import com.lugialo.donatify.dto.UserResponseDto;
import com.lugialo.donatify.model.Role;
import com.lugialo.donatify.model.User;
import com.lugialo.donatify.repository.OngRepository;
import com.lugialo.donatify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, OngRepository ongRepository)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

        newUser.setPhone(registrationDto.getPhone());
        newUser.setAddress(registrationDto.getAddress());
        // totalPoints é inicializado como 0 por padrão

        User savedUser = userRepository.save(newUser);
        return UserResponseDto.fromEntity(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public User findUserEntityByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com email: " + email));
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


    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponseDto updateUserByAdmin(Long userId, AdminUserUpdateDto updateDto) {
        User userToUpdate = getUserEntityById(userId);

        if (updateDto.getName() != null && !updateDto.getName().isEmpty()) {
            userToUpdate.setName(updateDto.getName());
        }
        if (updateDto.getNickname() != null) {
            userToUpdate.setNickname(updateDto.getNickname());
        }
        if (updateDto.getEmail() != null && !updateDto.getEmail().isEmpty()) {
            userToUpdate.setEmail(updateDto.getEmail());
        }
        if (updateDto.getRole() != null) {
            userToUpdate.setRole(updateDto.getRole());
        }

        User savedUser = userRepository.save(userToUpdate);

        return UserResponseDto.fromEntity(savedUser);
    }

    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("Usuário com ID " + userId + " não encontrado.");
        }
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional
    public UserResponseDto updateUserProfile(String userEmail, UserProfileUpdateDto updateDto) {
        // Busca o usuário pelo email do token, garantindo que ele só pode editar a si mesmo.
        User user = findUserEntityByEmail(userEmail);

        // Atualiza os campos básicos se eles foram fornecidos no DTO
        if (updateDto.getName() != null && !updateDto.getName().isEmpty()) {
            user.setName(updateDto.getName());
        }
        if (updateDto.getNickname() != null && !updateDto.getNickname().isEmpty()) {
            // Adicionar verificação se o novo nickname já não está em uso por outra pessoa
            user.setNickname(updateDto.getNickname());
        }
        if (updateDto.getPhone() != null) {
            user.setPhone(updateDto.getPhone());
        }
        if (updateDto.getAddress() != null) {
            user.setAddress(updateDto.getAddress());
        }

        // Lógica para alteração de senha
        if (updateDto.getNewPassword() != null && !updateDto.getNewPassword().isEmpty()) {
            // Se o usuário quer mudar a senha, ele DEVE fornecer a senha atual.
            if (updateDto.getCurrentPassword() == null || updateDto.getCurrentPassword().isEmpty()) {
                throw new IllegalArgumentException("Para definir uma nova senha, a senha atual é necessária.");
            }

            // Verifica se a senha atual fornecida corresponde à senha no banco de dados.
            if (!passwordEncoder.matches(updateDto.getCurrentPassword(), user.getPassword())) {
                throw new IllegalArgumentException("A senha atual está incorreta.");
            }

            // Se tudo estiver correto, atualiza para a nova senha (hasheada).
            user.setPassword(passwordEncoder.encode(updateDto.getNewPassword()));
        }

        User updatedUser = userRepository.save(user);
        return UserResponseDto.fromEntity(updatedUser);
    }




}
