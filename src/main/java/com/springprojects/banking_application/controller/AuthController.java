package com.springprojects.banking_application.controller;

import com.springprojects.banking_application.dto.LoginDTO;
import com.springprojects.banking_application.dto.UserRequestDTO;
import com.springprojects.banking_application.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRequestDTO request)
    {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO loginDTO)
    {
        String result = authService.login(loginDTO);

        if (result.equals("Invalid password") || result.equals("User not found")) {
            return ResponseEntity.status(401).body(result);
        }

        return ResponseEntity.ok(result);
    }
}
