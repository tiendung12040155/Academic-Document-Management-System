package com.example.ADMS.entity;

import com.example.ADMS.entity.type.MethodType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@Table(name = "system_permission_tbl")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SystemPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String name;
    Boolean active;
    LocalDateTime createdAt;
    String path;
    @Enumerated(EnumType.STRING)
    MethodType methodType;
    String description;
    Long userId;

    @OneToMany(mappedBy = "permission")
    List<UserRolePermission> userRolePermissionList;
}
