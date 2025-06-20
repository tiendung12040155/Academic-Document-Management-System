package com.example.ADMS.utils;

import com.example.ADMS.entity.User;
import com.example.ADMS.exception.ApiException;
import com.example.ADMS.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserHelper {
    private final UserRepository userRepository;
    private final MessageException messageException;

    public User getUserLogin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userRepository.findByUsernameOrEmailActive(username)
                    .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND));
        }
        return null;
    }
}
