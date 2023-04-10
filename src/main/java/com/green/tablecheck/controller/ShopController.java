package com.green.tablecheck.controller;

import com.green.tablecheck.domain.dto.ShopDto;
import com.green.tablecheck.domain.form.AddShopForm;
import com.green.tablecheck.domain.form.ReservationForm;
import com.green.tablecheck.service.manager.ShopService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<ShopDto> addShop(@RequestParam Long managerId, @RequestBody @Valid AddShopForm form) {
        return ResponseEntity.ok(ShopDto.from(shopService.addShop(managerId, form)));
    }

    @GetMapping
    public ResponseEntity<List<String>> searchShop(@RequestParam String shopName) {
        return ResponseEntity.ok(shopService.searchShop(shopName));
    }

    @GetMapping("/detail")
    public ResponseEntity<ShopDto> getShop(@RequestParam String shopName) {
        return ResponseEntity.ok(ShopDto.from(shopService.getShop(shopName)));
    }

    @PostMapping("/reserve")
    public ResponseEntity<String> reserveShop(@RequestParam Long shopId,
        @RequestParam Long customerId, @RequestBody ReservationForm form) {
        return ResponseEntity.ok(shopService.reserveShop(shopId, customerId, form));  // TODO: jwt 구현해야되나
    }

}
