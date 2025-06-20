package com.example.ADMS.security.jwt;

import com.example.ADMS.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${application.security.jwt.access-token.expiration}")
    public Long ACCESS_TOKEN_EXPIRED;
    @Value("${application.security.jwt.refresh-token.expiration}")
    public Long REFRESH_TOKEN_EXPIRED;
    @Value("${application.security.jwt.secret-key}")
    String SECRET_KEY;

    public String generateToken(
            UserDetails userDetails,
            Collection<SimpleGrantedAuthority> authorities
    ) {
        Map<String, Object> extraClaims = new HashMap<>();

        extraClaims.put("userId", ((User) userDetails).getId());
        boolean isValid = ((User) userDetails).getFirstname() != null;
        extraClaims.put("isValid", isValid);
        if (isValid) {
            extraClaims.put("roles", authorities.stream().map(GrantedAuthority::getAuthority).toList());
        } else extraClaims.put("roles", Arrays.asList(new String[]{"OTHER"}));
        return Jwts
                .builder()
                .setSubject(userDetails.getUsername())
                .addClaims(extraClaims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRED))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public String[] getRolesFromToken(String token) {
        Claims claims = extractAllClaims(token);
        List<String> rolesList = claims.get("roles", List.class);
        return rolesList.toArray(new String[rolesList.size()]);
    }

    public boolean isTokenValid(String token, @NotNull UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    private @NotNull Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
