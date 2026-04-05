package com.springprojects.banking_application.service;

import com.springprojects.banking_application.dto.LoginDTO;
import com.springprojects.banking_application.dto.UserRequestDTO;

public interface AuthService {
    String register(UserRequestDTO userRequestDTO);

    String login(LoginDTO loginDTO);
}
