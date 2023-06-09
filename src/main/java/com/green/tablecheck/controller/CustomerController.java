package com.green.tablecheck.controller;

import com.green.tablecheck.config.JwtAuthenticationProvider;
import com.green.tablecheck.domain.dto.TokenDto;
import com.green.tablecheck.domain.dto.UserVo;
import com.green.tablecheck.domain.form.ReservationForm;
import com.green.tablecheck.domain.form.ReviewForm;
import com.green.tablecheck.service.CustomerService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final JwtAuthenticationProvider provider;

    private final String TOKEN_NAME = "X-AUTH-TOKEN";

    @PostMapping("/reservation/shop/{shopId}")
    public ResponseEntity<String> reserveShop(
        @RequestHeader(name = TOKEN_NAME) String token,
        @PathVariable Long shopId,
        @RequestBody ReservationForm form) {
        UserVo vo = provider.getUserVo(token);
        return ResponseEntity.ok(customerService.reserveShop(shopId, vo.getId(), form));
    }

    @GetMapping("/reservation/{reservationId}/code")
    public ResponseEntity<String> getCode(
        @RequestHeader(name = TOKEN_NAME) String token, @PathVariable Long reservationId) {
        UserVo vo = provider.getUserVo(token);
        return ResponseEntity.ok(customerService.getCode(reservationId, vo.getId()));
    }

    @PostMapping("/reservation//{reservationId}/review")
    public ResponseEntity<String> review(
        @RequestHeader(name = TOKEN_NAME) String token,
        @PathVariable Long reservationId,
        @RequestBody ReviewForm reviewForm) {
        return ResponseEntity.ok(customerService.review(reservationId, reviewForm));
    }

    @PatchMapping("/token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        return ResponseEntity.ok(TokenDto.from(provider.refreshToken(request)));
    }

}
