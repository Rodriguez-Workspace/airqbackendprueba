package com.oxaira.airq.iam.interfaces.rest;

import com.oxaira.airq.iam.application.dto.RegisterUserRequest;
import com.oxaira.airq.iam.application.service.RegisterUserUseCase;
import com.oxaira.airq.iam.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;

    @PostMapping
    public User register(
            @RequestBody RegisterUserRequest request) {
        return registerUserUseCase.execute(request);
    }
}