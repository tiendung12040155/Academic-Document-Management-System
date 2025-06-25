package com.example.ADMS.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationDTORequest {

    @NotBlank(message = "Email or username is mandatory")
    @Pattern(regexp = "^(?:\\w+|\\w+([+\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.[a-zA-z]{2,4})+)$",
            message = "This field must be email or username")
    String email;

    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = "^(?=.*[A-Za-z@])(?=.*[0-9])[A-Za-z0-9]{8,}$",
            message = "Password must contain at least 8 characters and include both letters and numbers")
    String password;

}
