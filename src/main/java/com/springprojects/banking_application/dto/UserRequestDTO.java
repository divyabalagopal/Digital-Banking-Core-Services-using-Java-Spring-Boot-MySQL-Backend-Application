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
    /**
     * Some documentation that can be added here to explain the purpose of the class and its fields.
     * This class represents a request DTO for user-related operations in the banking application.
     * It contains fields for user information such as first name, last name, gender, address, state of origin, email, password, phone number, and alternative phone number.
     * Validation annotations are used to enforce constraints on the fields, such as not being blank and having specific size limits.
     * The Gender enum is used to represent the biological gender of the user, which can be either MALE or FEMALE. The class is annotated with Lombok annotations to generate boilerplate code such as getters, setters, constructors, and builders.
     * The class can be used in various user-related operations, such as creating a new user, updating user information, or validating user input in the banking application.
     * Additional documentation can be added to explain the purpose of each field and any specific validation rules or constraints that apply to them.
     */



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
