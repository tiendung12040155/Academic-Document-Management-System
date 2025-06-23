package com.example.ADMS.controller;

import com.example.ADMS.model.dto.request.UserChangePasswordDTORequest;
import com.example.ADMS.model.dto.request.UserDTOFilter;
import com.example.ADMS.model.dto.request.UserDTOUpdate;
import com.example.ADMS.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.ADMS.utils.Constants.API_VERSION;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + "/users")
@Tag(name = "User", description = "API for User")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CrossOrigin
public class UserController {
    UserService userService;

    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody UserChangePasswordDTORequest request
    ) {
        return ResponseEntity.ok(userService.changePassword(request));
    }

    @GetMapping("/preview")
    public ResponseEntity<?> previewInfo() {
        return ResponseEntity.ok(userService.previewInfo());
    }

    @Operation(summary = "Search User")
    @GetMapping("/display")
    public ResponseEntity<?> searchUser(@ModelAttribute UserDTOFilter userDTOFilter) {
        return ResponseEntity.ok(userService.searchUser(userDTOFilter));
    }

    @Operation(summary = "Update User")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserDTOUpdate request, @PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(userService.updateUser(request, id));
    }

    @Operation(summary = "Reset Password User")
    @GetMapping("/reset/{id}")
    public ResponseEntity<?> resetPasswordUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.resetPasswordUser(id));
    }

    @Operation(summary = "Get user by Id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Change active User")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> changeActive(@PathVariable @NotEmpty Long id) {
        return ResponseEntity.ok(userService.changeActive(id));
    }
}
