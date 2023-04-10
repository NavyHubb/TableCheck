package com.green.tablecheck.controller;

import com.green.tablecheck.domain.form.SignInForm;
import com.green.tablecheck.service.SignInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signIn")
@RequiredArgsConstructor
public class SignInController {

    private final SignInService signInService;

    @PostMapping("/manager")
    public ResponseEntity<String> mangerSignIn(@RequestBody SignInForm form) {
        return ResponseEntity.ok(signInService.managerSignIn(form));
    }

    @PostMapping("/customer")
    public ResponseEntity<String> customerSignIn(@RequestBody SignInForm form) {
        return ResponseEntity.ok(signInService.customerSignIn(form));
    }

}
