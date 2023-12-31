package com.example.Shopapp.controllers;

import com.example.Shopapp.components.LocalizationUtils;
import com.example.Shopapp.exceptions.DataNotFoundException;
import com.example.Shopapp.models.dtos.UserDto;
import com.example.Shopapp.models.dtos.UserLoginDto;
import com.example.Shopapp.models.entity.User;
import com.example.Shopapp.models.responses.LoginResponse;
import com.example.Shopapp.services.UserService;
import com.example.Shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(
            @Valid @RequestBody UserDto userDto,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errMess = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errMess);
            }
            // check password vs retypePassword
            if (!userDto.getPassword().equals(userDto.getRetypePassword())) {
                return ResponseEntity.badRequest().body("Password does not match");
            }
            return ResponseEntity.ok(userService.createUser(userDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage() + "123");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody UserLoginDto userLoginDto
    ) {
        // Kiểm tra thông tin đăng nhập và sinh token
        try {
            String token = userService.login(
                    userLoginDto.getPhoneNumber(),
                    userLoginDto.getPassword(),
                    userLoginDto.getRoleId()
            );
            // Trả về token trong response
            return ResponseEntity.ok(LoginResponse.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY))
                    .token(token)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    LoginResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_FAILED, e.getMessage()))
                            .build()
            );
        }

    }
}