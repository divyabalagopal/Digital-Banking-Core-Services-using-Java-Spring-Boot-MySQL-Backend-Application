package com.springprojects.banking_application.service;

import com.springprojects.banking_application.dto.LoginDTO;
import com.springprojects.banking_application.dto.UserRequestDTO;
import com.springprojects.banking_application.entity.User;
import com.springprojects.banking_application.repository.UserRepo;
import com.springprojects.banking_application.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public String register(UserRequestDTO request) {
        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setGender(request.getGender());
        user.setRole("READER");

        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }


        userRepo.save(user);

        return "User registered successfully";
    }

    @Override
    public String login(LoginDTO loginDTO) {
        User user = userRepo.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid Password");
        }

        String token = jwtService.generateToken(user.getEmail());
        String extractedEmail = jwtService.extractEmail(token);

        return token;
    }
}
