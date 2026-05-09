package com.Petre_Daria.banking_app.dtoMapper;

import com.Petre_Daria.banking_app.dto.UserRegisterRequestDTO;
import com.Petre_Daria.banking_app.dto.UserResponseDTO;
import com.Petre_Daria.banking_app.model.Role;
import com.Petre_Daria.banking_app.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User dtoToUser(UserRegisterRequestDTO dto) {
        if (dto == null) return null;

        User user = new User();
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setBirthDate(dto.birthDate());
        user.setPhoneNumber(dto.phoneNumber());
        user.setRole(Role.USER);

        return user;
    }

    public UserResponseDTO userToDto(User user) {
        if (user == null) return null;

        return new UserResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getBirthDate(),
                user.getRole()
        );
    }
}