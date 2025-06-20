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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserForgotPasswordDTORequest {
    String token;

    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = "^(?=.*[A-Za-z@])(?=.*[0-9])[A-Za-z0-9]{8,}$",
            message = "Password must contain at least 8 characters and include both letters and numbers")
    String newPassword;

    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = "^(?=.*[A-Za-z@])(?=.*[0-9])[A-Za-z0-9]{8,}$",
            message = "Password must contain at least 8 characters and include both letters and numbers")
    String confirmationPassword;
}
