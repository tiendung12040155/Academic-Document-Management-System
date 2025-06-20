package com.example.ADMS.service.impl;

import com.example.ADMS.entity.*;
import com.example.ADMS.entity.type.TokenType;
import com.example.ADMS.exception.ApiException;
import com.example.ADMS.model.EmailInfo;
import com.example.ADMS.model.dto.request.*;
import com.example.ADMS.model.dto.response.AuthenticationDTOResponse;
import com.example.ADMS.model.dto.response.RegisterDTOResponse;
import com.example.ADMS.model.mapper.UserMapper;
import com.example.ADMS.repository.*;
import com.example.ADMS.security.jwt.JwtService;
import com.example.ADMS.service.AuthenticationService;
import com.example.ADMS.service.ConfirmationTokenService;
import com.example.ADMS.utils.DateTimeHelper;
import com.example.ADMS.utils.EmailHandler;
import com.example.ADMS.utils.EmailHtml;
import com.example.ADMS.utils.MessageException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.ADMS.utils.Constants.EMAIL_WAITING_EXPIRATION;
import static com.example.ADMS.utils.Constants.HOST_FRONT_END;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {
    UserRepository userRepository;
    AuthenticationManager authenticationManager;
    PasswordEncoder passwordEncoder;
    JwtService jwtService;
    ConfirmationTokenService confirmationTokenService;
    RoleRepository roleRepository;
    EmailHandler emailHandler;
    MessageException messageException;
    ConfirmTokenRepository confirmTokenRepository;
    EmailHtml emailHtml;
    TokenRepository tokenRepository;
    UserRoleRepository userRoleRepository;

    public void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    private AuthenticationDTOResponse buildDTOAuthenticationResponse(User user) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Set<UserRole> set = userRoleRepository.getUserRoleByActiveAndUser(user);
        Set<Role> roles = set.stream().map(UserRole::getRole)
                .filter(Role::getActive)
                .collect(Collectors.toSet());
        roles.forEach(s -> authorities.add(new SimpleGrantedAuthority(s.getName())));
        var accessToken = jwtService.generateToken(user, authorities);
        return AuthenticationDTOResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    /*

    Th1: email is not exist in database -> exception
    Th2: email valid + password invalid -> exception
    TH3: account was blocked -> exception
    Th4: user login successful -> jwt

    */
    @Override
    public AuthenticationDTOResponse login(AuthenticationDTORequest request) {
        var user = userRepository.findByUsernameOrEmailActive(request.getEmail())
                .orElseThrow(() -> ApiException.notFoundException("User is not found"));
        try {
            authenticate(user.getEmail(), request.getPassword());
        } catch (Exception e) {
            throw ApiException.forBiddenException("Access denied, You have no permission");
        }
        AuthenticationDTOResponse response = buildDTOAuthenticationResponse(user);
        saveUserToken(user, response.getAccessToken());
        return response;
    }

    public UserRole addRoleToUser(User user, Long roleId) {
        Role role = roleRepository.findByIdAndActiveTrue(roleId)
                .orElseThrow(() -> ApiException.notFoundException("Role is not found"));
        return UserRole.builder()
                .role(role)
                .createdAt(DateTimeHelper.getTimeNow())
                .active(true)
                .user(user)
                .build();
    }

    public void saveUserToken(User user, String accessToken) {
        var token = Token.builder()
                .user(user)
                .token(accessToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .createdAt(DateTimeHelper.getTimeNow())
                .expiredAt(DateTimeHelper.getTimeNow().plus(Duration.ofMillis(jwtService.ACCESS_TOKEN_EXPIRED)))
                .build();
        tokenRepository.save(token);
    }

    /*

    Th1: email is exist -> exception
    Th2: username is exist -> exception
    Th3: register successful -> return user id

    */
    @Override
    public RegisterDTOResponse register(RegisterDTORequest request) {
        if (userRepository.findByUsernameOrEmailActive(request.getEmail()).isPresent()) {
            throw ApiException.badRequestException(messageException.MSG_USER_EMAIL_EXISTED);
        }
        if (userRepository.findByUsernameOrEmailActive(request.getUsername()).isPresent()) {
            throw ApiException.badRequestException(messageException.MSG_USER_USERNAME_EXISTED);
        }
        User user = UserMapper.toUser(request, passwordEncoder);
        user.setUserRoleList(Set.of(addRoleToUser(user, 1L)));
        user = userRepository.save(user);
        return UserMapper.toRegisterDTOResponse(user);
    }

    /*

    Th1: user id has active is true -> exception
    Th3: update register successfully -> link
    Th4: phone is existed
    */
    @Override
    public Boolean updateRegister(RegisterDTOUpdate request) {
        if (userRepository.findByPhoneAndActiveTrue(request.getPhone()).isPresent()) {
            throw ApiException.badRequestException("Phone is existed");
        }
        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> ApiException.notFoundException("User is not found"));
        user = userRepository.save(UserMapper.toUser(request, user));
        return true;
    }

    /*

    Th1: email is not exist in database -> exception
    Th2: email is not active -> exception
    Th3: forgot password is successful -> link

    */

    @Override
    public String forgotPassword(UserEmailDTORequest request) {
        User user = userRepository.findByUsernameOrEmailActive(request.getEmail())
                .orElseThrow(() -> ApiException.notFoundException("User is not found"));

        //todo: create token send link to gmail
        String token = confirmationTokenService.generateTokenEmail(user);
        String link = HOST_FRONT_END + "/auth/forgot-password?token=" + token;

        String body = "Please click on the below link to change password in your account:";
        String subject = "Confirm your email to change password";
        String content = emailHtml.buildEmail(user.getUsername(), link, subject, body, EMAIL_WAITING_EXPIRATION);
        EmailInfo emailInfo = EmailInfo.builder()
                .to(request.getEmail())
                .content(content)
                .subject(subject)
                .build();
        emailHandler.send(emailInfo);
        return "confirm";
    }

    /*

    Th1: token is not found
    Th2: new password is not equal confirm password
    Th3: token has user not active
    Th4: confirm at is not null
    Th5: change forgot password is successful

     */
    @Override
    public Boolean changePasswordForgot(UserForgotPasswordDTORequest request) {
        ConfirmationToken confirmationToken = confirmTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> ApiException.notFoundException("Token is not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw ApiException.internalServerException(messageException.MSG_INTERNAL_SERVER_ERROR);
        }

        User user = userRepository
                .findByUsernameOrEmailActive(confirmationToken.getUser().getEmail())
                .orElseThrow(() -> ApiException.notFoundException("User is not found"));

        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw ApiException.badRequestException(messageException.MSG_USER_NOT_MATCH_PASSWORD);
        }

        confirmationToken.setConfirmedAt(DateTimeHelper.getTimeNow());
        confirmTokenRepository.save(confirmationToken);

        user.setPassword(passwordEncoder.encode(request.getConfirmationPassword()));
        user = userRepository.save(user);

        return true;
    }

    @Override
    public Boolean logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }
        String jwt = authHeader.split(" ")[1].trim();
        var storeToken = tokenRepository.findByToken(jwt).orElse(null);
        if (storeToken != null) {
            storeToken.setExpired(true);
            storeToken.setRevoked(true);
            tokenRepository.save(storeToken);
            SecurityContextHolder.clearContext();
        }
        return true;
    }
}
