package com.oxaira.airq.iam.interfaces.rest;

import com.oxaira.airq.iam.application.dto.LoginRequest;
import com.oxaira.airq.iam.application.dto.LoginResponse;
import com.oxaira.airq.iam.application.service.LoginUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final com.oxaira.airq.iam.application.service.ChangePasswordUseCase changePasswordUseCase;

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request) {

        return loginUseCase.execute(request);

    }

    @PutMapping("/password/change")
    public java.util.Map<String, String> changePassword(
            @RequestBody com.oxaira.airq.iam.application.dto.ChangePasswordRequest request) {
        
        String currentUserEmail = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        changePasswordUseCase.execute(currentUserEmail, request);
        return java.util.Map.of("message", "Contraseña actualizada correctamente");
    }
}