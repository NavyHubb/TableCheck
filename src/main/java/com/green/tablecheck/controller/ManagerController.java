package com.green.tablecheck.controller;

import com.green.tablecheck.config.JwtAuthenticationProvider;
import com.green.tablecheck.domain.dto.UserVo;
import com.green.tablecheck.domain.form.AddShopForm;
import com.green.tablecheck.service.ManagerService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;
    private final JwtAuthenticationProvider provider;

    private final String TOKEN_NAME = "X-AUTH-TOKEN";

    @PostMapping("/shop")
    public ResponseEntity<String> addShop(@RequestHeader(name = TOKEN_NAME) String token,
        @RequestBody @Valid AddShopForm form) {
        UserVo vo = provider.getUserVo(token);
        return ResponseEntity.ok(managerService.addShop(vo.getId(), form));
    }

    @PatchMapping("/shop/{shopId}/status")
    public ResponseEntity<String> changeStatus(
        @RequestHeader(name = TOKEN_NAME) String token,
        @PathVariable Long shopId) {
        UserVo vo = provider.getUserVo(token);
        return ResponseEntity.ok(managerService.changeStatus(vo.getId()));
    }

    @PatchMapping("/reservation/{reservationId}/approval")
    public ResponseEntity<String> approveReservation(@RequestHeader(name = TOKEN_NAME) String token
    ,@PathVariable Long reservationId) {
        UserVo vo = provider.getUserVo(token);
        return ResponseEntity.ok(managerService.approveReservation(reservationId));
    }

    @PatchMapping("/reservation/{reservationId}/refusal")
    public ResponseEntity<String> declineReservation(@RequestHeader(name = TOKEN_NAME) String token
        ,@PathVariable Long reservationId) {
        UserVo vo = provider.getUserVo(token);
        return ResponseEntity.ok(managerService.refuseReservation(reservationId));
    }

}
