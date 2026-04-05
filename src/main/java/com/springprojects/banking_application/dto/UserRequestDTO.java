package com.springprojects.banking_application.dto;

import com.springprojects.banking_application.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.*;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@Getter
//@Setter
public class UserRequestDTO {

    @NotBlank(message = "Field cannot be blank")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Field cannot be blank")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    private String otherName;

    @NotNull(message = "Biological gender MALE/FEMALE must be provided")
    private Gender gender;

    private String address;

    private String stateOfOrigin;

    private String email;


    private String password;

    private String phoneNumber;

    private String alternativePhoneNumber;
}
