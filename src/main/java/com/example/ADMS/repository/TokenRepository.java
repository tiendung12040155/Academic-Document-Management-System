package com.example.ADMS.repository;

import com.example.ADMS.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("select t from Token t join t.user u " +
            "where u.active = true and (t.expired = false or t.revoked = false)")
    List<Token> findAllValidToken();

    Optional<Token> findByToken(String token);
}
