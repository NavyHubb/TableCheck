package com.green.tablecheck.controller;

import com.green.tablecheck.domain.dto.ShopDto;
import com.green.tablecheck.domain.form.AddShopForm;
import com.green.tablecheck.service.manager.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @PostMapping
    public ResponseEntity<ShopDto> addShop(@RequestParam Long managerId, @RequestBody AddShopForm form) {
        return ResponseEntity.ok(ShopDto.from(shopService.addShop(managerId, form)));
    }

}
