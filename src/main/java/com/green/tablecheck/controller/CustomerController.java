package com.green.tablecheck.controller;

import com.green.tablecheck.config.JwtAuthenticationProvider;
import com.green.tablecheck.domain.dto.UserVo;
import com.green.tablecheck.domain.form.ReservationForm;
import com.green.tablecheck.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final JwtAuthenticationProvider provider;

    private final String TOKEN_NAME = "X-AUTH-TOKEN";

    @PostMapping("/reserve")
    public ResponseEntity<String> reserveShop(
        @RequestHeader(name = TOKEN_NAME) String token,
        @RequestParam Long shopId,
        @RequestBody ReservationForm form) {
        UserVo vo = provider.getUserVo(token);  // customer
        return ResponseEntity.ok(customerService.reserveShop(shopId, vo.getId(), form));
    }

}
