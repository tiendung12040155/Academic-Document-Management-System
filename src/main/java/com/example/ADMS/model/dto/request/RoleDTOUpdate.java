package com.example.ADMS.model.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleDTOUpdate {
    Long roleId;
    String roleName;
    String description;
    List<Long> permission;
}
