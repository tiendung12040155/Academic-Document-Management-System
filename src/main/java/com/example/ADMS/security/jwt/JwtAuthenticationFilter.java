package com.example.ADMS.security.jwt;


import com.example.ADMS.entity.type.MethodType;
import com.example.ADMS.entity.type.ResourceType;
import com.example.ADMS.exception.CustomError;
import com.example.ADMS.repository.TokenRepository;
import com.example.ADMS.repository.UserRolePermissionRepository;
import com.example.ADMS.utils.Constants;
import com.example.ADMS.utils.MessageException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    JwtService jwtService;
    UserDetailsService userDetailsService;
    UserRolePermissionRepository userRolePermissionRepository;
    MessageException messageException;
    TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String url = request.getRequestURI();
        var data = new Object() {
            final String path = getPath(url);
            final MethodType methodType = MethodType.valueOf(request.getMethod().toUpperCase());
        };

        log.info("Path: {}, method: {}", data.path, request.getMethod());

        //check token exist from header
        final String authHeader = request.getHeader(AUTHORIZATION);
        final String jwt;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            //permit all api
            if (isPermissionApi(data.path)) {
                filterChain.doFilter(request, response);
            } else responseToClient(response, messageException.MSG_BEARER_NOT_FOUND);
            return;
        }
        if (isPermissionApi("/api/v1/auth/logout")
                && data.path.equalsIgnoreCase("/api/v1/auth/logout")) {
            filterChain.doFilter(request, response);
            return;
        }

        //get role from jwt
        String[] roles;
        jwt = authHeader.split(" ")[1].trim();
        try {
            roles = jwtService.getRolesFromToken(jwt);
        } catch (Exception ex) {
            responseToClient(response, messageException.MSG_TOKEN_EXPIRED);
            return;
        }
        //check permission by role from database
        boolean isPermission = Arrays.stream(roles).anyMatch(role -> {
            return (userRolePermissionRepository.needCheckPermission(data.path, data.methodType, role) != null);
        });
        log.info("Permission: {}", isPermission);
        if (!isPermission) {
            responseToClient(response, messageException.MSG_NO_PERMISSION);
            return;
        }
        //save SecurityContextHolder
        username = jwtService.extractUsername(jwt);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            var isTokenValid = tokenRepository.findByToken(jwt)
                    .map(t -> !t.getExpired() && !t.getRevoked())
                    .orElse(false);

            if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                Arrays.stream(roles).forEach(role -> {
                    authorities.add(new SimpleGrantedAuthority(role));
                });
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        authorities
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                responseToClient(response, messageException.MSG_TOKEN_INVALID);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    public boolean isPermissionApi(String url) {
        return Arrays.stream(Constants.LIST_PERMIT_ALL)
                .anyMatch(o -> {
                    String urlOriginal = o.replace("*", "");
                    urlOriginal = urlOriginal.endsWith("/") ?
                            urlOriginal.substring(0, urlOriginal.length() - 1) : urlOriginal;
                    return url.contains(urlOriginal);
                });
    }

    public String getPath(String path) {
        int index = path.lastIndexOf("/");
        String uri = path.substring(index);
        Pattern pattern = Pattern.compile("-?\\d+");
        Matcher matcher = pattern.matcher(uri);
        if (matcher.find()) {
            path = path.replace(uri, "");
        }

        //loại bỏ path variable là đuôi file
        for (ResourceType resourceType : ResourceType.values()) {
            String finish = path.substring(index);
            if (finish.contains(resourceType.toString().toLowerCase())) {
                return path.replace(uri, "");
            }
        }
        return path;
    }

    private void responseToClient(HttpServletResponse response, String message) throws IOException {
        CustomError customError = CustomError.builder()
                .code("401")
                .message(message)
                .build();
        response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        response.setStatus(HttpServletResponse.SC_ACCEPTED);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        response.getOutputStream().print(mapper.writeValueAsString(customError));
        response.flushBuffer();
    }
}
