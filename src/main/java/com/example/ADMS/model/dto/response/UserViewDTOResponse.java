package com.example.ADMS.model.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserViewDTOResponse {
    Long id;
    String firstname;
    String lastname;
    String username;
    String email;
    Boolean active;
    String avatar;
    Boolean gender;
    LocalDate dateOfBirth;
    String phone;
    String district;
    String province;
    String school;
    LocalDateTime createdAt;
    List<Long> roles;
    String className;
    Long totalPosted;
}
