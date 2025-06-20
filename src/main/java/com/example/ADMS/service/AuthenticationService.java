package com.example.ADMS.service;


import com.example.ADMS.model.dto.request.*;
import com.example.ADMS.model.dto.response.AuthenticationDTOResponse;
import com.example.ADMS.model.dto.response.RegisterDTOResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;


public interface AuthenticationService {
    RegisterDTOResponse register(RegisterDTORequest request);

    AuthenticationDTOResponse login(AuthenticationDTORequest request) throws Exception;

    Boolean updateRegister(RegisterDTOUpdate request);

    String forgotPassword(UserEmailDTORequest request);

    Boolean changePasswordForgot(UserForgotPasswordDTORequest request);

    Boolean logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication);
}
