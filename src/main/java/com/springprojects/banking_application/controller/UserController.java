package com.springprojects.banking_application.controller;

import com.springprojects.banking_application.dto.*;
import com.springprojects.banking_application.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Operation(
            summary="Create New User Account",
            description = "Create a new user account"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP status 200 CREATED"
    )
    @PostMapping("/newUser")
    public BankResponseDTO createAccount(@Valid @RequestBody UserRequestDTO userRequestDTO)
    {
        return userService.createAccount(userRequestDTO);
    }
    @Operation(
            summary="Check Balance",
            description = "Enquiry for account balance"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP status 200 CREATED"
    )

    @GetMapping("/balanceEnquiry")
    public BankResponseDTO balanceEnquiry(@RequestBody EnquiryRequestDTO enquiryRequest)
    {
        return userService.balanceEnquiry(enquiryRequest);
    }

    @Operation(
            summary="Check Name of Account Holder",
            description = "Enquiry for account holder's name through their account number"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP status 200 CREATED"
    )
    @GetMapping("/nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequestDTO enquiryRequestDTO)
    {
        return userService.nameEnquiry(enquiryRequestDTO);
    }

    @Operation(
            summary="Credit Money into Account",
            description = "Credit money into an account"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP status 200 CREATED"
    )
    @PostMapping("/credit")
    public BankResponseDTO creditIntoAccount(@RequestBody CreditDebitRequestDTO creditDebitRequestDTO)
    {
        return userService.creditRequest(creditDebitRequestDTO);
    }

    @Operation(
            summary="Debit Amount From an Account",
            description = "Debit money from an account"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP status 200 CREATED"
    )
    @PostMapping("/debit")
    public BankResponseDTO debitIntoAccount(@RequestBody CreditDebitRequestDTO creditDebitRequestDTO)
    {
        return userService.debitRequest(creditDebitRequestDTO);
    }
    @Operation(
            summary="UPI Transfer",
            description = "Transfer Money from One Account to Another"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP status 200 CREATED"
    )

    @PostMapping("/transfer")
    public BankResponseDTO transfer(@RequestBody TransferDTO transferDTO)
    {
        return userService.transferRequest(transferDTO);
    }
}
