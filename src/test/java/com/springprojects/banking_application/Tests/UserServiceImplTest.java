package com.springprojects.banking_application.Tests;

import com.springprojects.banking_application.dto.BankResponseDTO;
import com.springprojects.banking_application.dto.UserRequestDTO;
import com.springprojects.banking_application.entity.User;
import com.springprojects.banking_application.enums.Gender;
import com.springprojects.banking_application.repository.UserRepo;
import com.springprojects.banking_application.service.EmailService;
import com.springprojects.banking_application.service.TransactionService;
import com.springprojects.banking_application.service.UserServiceImpl;
import com.springprojects.banking_application.utils.AccountUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Assertions;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepo userRepo;

    @Mock
    private TransactionService transactionService;

    @Mock
    private EmailService emailService;

    @Test
    void shouldCreateAccWhenUserNotExists() {

        UserRequestDTO dto = new UserRequestDTO();
        dto.setFirstName("Donna");
        dto.setLastName("B");
        dto.setEmail("donna121@test.com");
        dto.setGender(Gender.FEMALE);
        dto.setPhoneNumber("656866");

//        UserRequestDTO request = new UserRequestDTO();
//        request.setEmail("divya@gmail.com");

        when(userRepo.existsByEmail(anyString())).thenReturn(false);

        when(userRepo.save(ArgumentMatchers.any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BankResponseDTO bankResponseDTO= userService.createAccount(dto);
        Assertions.assertEquals(AccountUtils.ACCOUNT_CREATION_CODE,bankResponseDTO.getResponseCode());
    }


    @Test
    void shouldNotCreateAccWhenUserExists() {

        UserRequestDTO dto = new UserRequestDTO();
        dto.setFirstName("Donna");
        dto.setLastName("B");
        dto.setEmail("donna121@test.com");
        dto.setGender(Gender.FEMALE);
        dto.setPhoneNumber("656866");

        when(userRepo.existsByEmail(anyString())).thenReturn(true);

//        when(userRepo.save(ArgumentMatchers.any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BankResponseDTO bankResponseDTO= userService.createAccount(dto);
        Assertions.assertEquals(AccountUtils.ACCOUNT_EXISTS_CODE,bankResponseDTO.getResponseCode());
    }

//    public static void main(String[] args) {
//        UserServiceImplTest userServiceImplTest = new UserServiceImplTest();
//        userServiceImplTest.shouldCreateAccWhenUserNotExists();
//    }
}
