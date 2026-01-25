package com.springprojects.banking_application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditDebitRequestDTO {
    private String accountNumber;
    private BigDecimal amount;
}
