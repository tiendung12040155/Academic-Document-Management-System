package com.example.ADMS.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterDTORequest {

    @NotBlank(message = "Username is mandatory")
    @Size(min = 4, max = 15, message = "Username has length greater than 3 and smaller than 16")
    String username;

    @NotBlank(message = "Email is mandatory")
    @Pattern(regexp = "^[a-zA-Z0-9.]+@(\\w+\\.)*(\\w+)$", message = "This field must be email")
    String email;

    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z0-9]{8,}$",
            message = "Password must contain at least 8 characters and include both letters and numbers")
    String password;

}
