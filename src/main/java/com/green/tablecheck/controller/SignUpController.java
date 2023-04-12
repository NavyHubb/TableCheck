package com.green.tablecheck.controller;

import com.green.tablecheck.domain.form.SignUpForm;
import com.green.tablecheck.exception.ErrorResponse;
import com.green.tablecheck.service.SignUpService;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signUp")
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpService signUpService;

    // 매니저 회원가입
    @PostMapping("/manager")
    public ResponseEntity<Object> signUpManger(@RequestBody @Valid SignUpForm form, Errors errors) {

        // 유효성 검사에서 발생한 에러 메세지 모음
        if (errors.hasErrors()) {
            List<ErrorResponse> errorResponses = new ArrayList<>();

            errors.getAllErrors().stream().forEach(e -> errorResponses.add(ErrorResponse.of(e)));

            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(signUpService.managerSignUp(form));
    }

    // 고객 회원가입
    @PostMapping("/customer")
    public ResponseEntity<String> signUpCustomer(@RequestBody @Valid SignUpForm form) {
        return ResponseEntity.ok(signUpService.customerSignUp(form));
    }

}
