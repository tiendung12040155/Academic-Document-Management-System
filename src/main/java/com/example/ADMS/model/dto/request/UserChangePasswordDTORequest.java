package com.example.ADMS.model.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserChangePasswordDTORequest {
    String currentPassword;
    String newPassword;
    String confirmationPassword;
}
