package com.employee.dao;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    private String phoneNo;
    private String jobTitle;
    private String department;

    private LocalDate dateOfJoining;

    private String employmentType;

    private String emergencyContact;
}
