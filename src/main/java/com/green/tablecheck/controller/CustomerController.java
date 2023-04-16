package com.green.tablecheck.controller;

import com.green.tablecheck.config.JwtAuthenticationProvider;
import com.green.tablecheck.domain.dto.UserVo;
import com.green.tablecheck.domain.form.ReservationForm;
import com.green.tablecheck.domain.form.ReviewForm;
import com.green.tablecheck.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @PostMapping("/reservation")
    public ResponseEntity<String> reserveShop(
        @RequestHeader(name = TOKEN_NAME) String token,
        @RequestParam Long shopId,
        @RequestBody ReservationForm form) {
        UserVo vo = provider.getUserVo(token);
        return ResponseEntity.ok(customerService.reserveShop(shopId, vo.getId(), form));
    }

    @GetMapping("/reservation/code")
    public ResponseEntity<String> getCode(
        @RequestHeader(name = TOKEN_NAME) String token, @RequestParam Long shopId) {
        UserVo vo = provider.getUserVo(token);
        return ResponseEntity.ok(customerService.getCode(shopId, vo.getId()));
    }

    @PostMapping("/reservation/review/{reservationId}")
    public ResponseEntity<String> review(
        @RequestHeader(name = TOKEN_NAME) String token,
        @PathVariable Long reservationId,
        @RequestBody ReviewForm reviewForm) {
        return ResponseEntity.ok(customerService.review(reservationId, reviewForm));
    }

}
