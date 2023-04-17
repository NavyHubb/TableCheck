package com.green.tablecheck.controller;

import com.green.tablecheck.domain.dto.TokenDto;
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
    public ResponseEntity<TokenDto> mangerSignIn(@RequestBody SignInForm form) {
        return ResponseEntity.ok(TokenDto.from(signInService.managerSignIn(form)));
    }

    @PostMapping("/customer")
    public ResponseEntity<TokenDto> customerSignIn(@RequestBody SignInForm form) {
        return ResponseEntity.ok(TokenDto.from(signInService.customerSignIn(form)));
    }

}
