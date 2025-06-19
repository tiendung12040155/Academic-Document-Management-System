package com.example.ADMS.entity;

import com.example.ADMS.entity.type.TokenType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@Table(name = "token_tbl")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String token;

    @Enumerated(EnumType.STRING)
    TokenType tokenType;

    Boolean expired;

    Boolean revoked;

    LocalDateTime createdAt;

    LocalDateTime expiredAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;


}
