import com.example.ADMS.model.dto.request.AuthenticationDTORequest;
import com.example.ADMS.model.dto.request.UserEmailDTORequest;
import com.example.ADMS.model.dto.request.UserForgotPasswordDTORequest;
import com.example.ADMS.service.AuthenticationService;
import com.example.ADMS.service.ConfirmationTokenService;
import com.example.ADMS.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(Constants.API_VERSION + "/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Authentication", description = "API for Authentication")
@CrossOrigin
public class AuthenticationController {
    AuthenticationService authenticationService;
    ConfirmationTokenService confirmationTokenService;

    @PostMapping("/login")
    @Operation(summary = "Login to basic authentication")
    public ResponseEntity<?> login(@Valid @RequestBody AuthenticationDTORequest request) throws Exception {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping("forgot-password")
    @Operation(summary = "Forgot password to send mail")
    public ResponseEntity<?> forgotPassword(@RequestBody UserEmailDTORequest request) {
        return ResponseEntity.ok(authenticationService.forgotPassword(request));
    }

    @GetMapping("/confirm")
    @Operation(summary = "Confirm token email for forgot password")
    public String confirm(@RequestParam(value = "token", required = true) String token) {
        return confirmationTokenService.goToForgotPassword(token);
    }

    @PostMapping("submit-forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody UserForgotPasswordDTORequest request) {
        return ResponseEntity.ok(authenticationService.changePasswordForgot(request));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        return ResponseEntity.ok(authenticationService.logout(request, response, authentication));
    }
}