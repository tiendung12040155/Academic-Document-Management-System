package com.example.ADMS.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileDTOUpdate {
    @NotBlank(message = "Firstname is mandatory")
    @Size(min = 2, max = 15, message = "Firstname has length greater than 1 and smaller than 16")
    String firstname;

    @NotBlank(message = "Lastname is mandatory")
    @Size(min = 2, max = 15, message = "Lastname has length greater than 1 and smaller than 16")
    String lastname;

    @NotNull(message = "Gender is mandatory")
    Boolean gender;

    @NotNull(message = "Date of birth is mandatory")
    LocalDate dateOfBirth;

    @NotBlank(message = "Phone is mandatory")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Phone just include 10 or 11 digit")
    String phone;

    @NotBlank(message = "District is mandatory")
    @Length(min = 5, message = "District has length greater than 4")
    String district;

    @NotBlank(message = "Province is mandatory")
    @Length(min = 5, message = "Province has length greater than 4")
    String province;

    @NotBlank(message = "Village is mandatory")
    @Length(min = 5, message = "Village has length greater than 4")
    String village;

    @NotBlank(message = "School name is mandatory")
    String school;

    @NotNull(message = "ClassId is mandatory")
    Long classId;
}
