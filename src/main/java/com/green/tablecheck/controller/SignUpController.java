package com.green.tablecheck.controller;

import com.green.tablecheck.domain.form.SignUpForm;
import com.green.tablecheck.service.customer.SignUpCustomerService;
import com.green.tablecheck.service.manager.SignUpManagerService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signUp")
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpManagerService signUpManagerService;
    private final SignUpCustomerService signUpCustomerService;

    // 매니저 회원가입
    @PostMapping("/manager")
    public ResponseEntity<String> signUpManger(@RequestBody SignUpForm form) {  // TODO: @Valid 처리
        return ResponseEntity.ok(signUpManagerService.signUp(form));
    }

    // 고객 회원가입
    @PostMapping("/customer")
    public ResponseEntity<String> signUpCustomer(@RequestBody @Valid SignUpForm form) {
        return ResponseEntity.ok(signUpCustomerService.signUp(form));
    }


}
