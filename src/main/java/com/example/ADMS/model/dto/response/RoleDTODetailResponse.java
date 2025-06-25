package com.example.ADMS.model.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleDTODetailResponse {
    Long roleId;
    String roleName;
    String description;
    LocalDateTime createdAt;
    Boolean active;
    String creator;
    List<PermissionDTOResponse> permissions;
}
