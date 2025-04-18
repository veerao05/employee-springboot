package com.employee.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class EmployeeDto {
    private Long id;
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;
    private String lastName;
    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Please provide a valid email address")
    private String email;
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Phone number must be between 7 and 15 digits and can include a leading '+'")
    private String phoneNo;
    @NotBlank(message = "Job title is required")
    @Size(max = 100, message = "Job title must not exceed 100 characters")
    private String jobTitle;
    @NotBlank(message = "Department is required")
    @Size(max = 100, message = "Department must not exceed 100 characters")
    private String department;

    private LocalDate dateOfJoining;

    @NotBlank(message = "Employment type is required")
    @Pattern(regexp = "(Full-Time|Part-Time|Contract|Internship)",
            message = "Employment type must be one of: Full-Time, Part-Time, Contract, Internship")
    private String employmentType;
    @NotBlank(message = "Emergency contact is required")
    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Emergency contact number must be between 7 and 15 digits and can include a leading '+'")
    private String emergencyContact;

}
