package com.green.tablecheck.controller;

import com.green.tablecheck.service.KioskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kiosk")
@RequiredArgsConstructor
public class KioskController {

    private final KioskService kioskService;

    @PatchMapping("/attend")
    public ResponseEntity<String> attend(Long shopId, String phone) {
        return ResponseEntity.ok(kioskService.attend(shopId, phone));
    }

    @PatchMapping("/check")
    public ResponseEntity<String> checkCode(Long shopId, String phone, String code) {
        return ResponseEntity.ok(kioskService.checkCode(shopId, phone, code));
    }


}
