package com.green.tablecheck.controller;

import com.green.tablecheck.domain.dto.ShopDto;
import com.green.tablecheck.service.manager.ShopService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;


    @GetMapping
    public ResponseEntity<List<String>> searchShop(@RequestParam String shopName) {
        return ResponseEntity.ok(shopService.searchShop(shopName));
    }

    @GetMapping("/detail")
    public ResponseEntity<ShopDto> getShop(@RequestParam String shopName) {
        return ResponseEntity.ok(ShopDto.from(shopService.getShop(shopName)));
    }



}
