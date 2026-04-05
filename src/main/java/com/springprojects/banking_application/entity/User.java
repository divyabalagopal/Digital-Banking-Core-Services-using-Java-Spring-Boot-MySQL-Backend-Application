package com.springprojects.banking_application.entity;

import com.springprojects.banking_application.enums.Gender;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;
    private String lastName;

    private String otherName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role; // USER / ADMIN

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
    private Gender gender;

    private String address;

    private String stateOfOrigin;

    private String accountNumber;

    private BigDecimal accountBalance;

    @Column(nullable = false, unique = true)
    private String email;

    private String phoneNumber;

    private String alternativePhoneNumber;

    private String status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime modifiedAt;
}
