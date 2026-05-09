package com.Petre_Daria.banking_app.service;

import com.Petre_Daria.banking_app.dto.UserRegisterRequestDTO;
import com.Petre_Daria.banking_app.dto.UserResponseDTO;
import com.Petre_Daria.banking_app.dtoMapper.UserMapper;
import com.Petre_Daria.banking_app.model.User;
import com.Petre_Daria.banking_app.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // metoda folosită de controllerul /auth/register
    public UserResponseDTO register(UserRegisterRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Register data is null");
        }

        String email = dto.email() == null ? null : dto.email().trim().toLowerCase();
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use");
        }

        // mapare DTO -> entitate
        User user = userMapper.dtoToUser(dto);

        // salvează în DB
        User saved = userRepository.save(user);

        // mapare entitate -> response DTO (fără password)
        return userMapper.userToDto(saved);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}