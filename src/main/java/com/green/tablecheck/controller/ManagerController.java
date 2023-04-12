package com.green.tablecheck.controller;

import com.green.tablecheck.config.JwtAuthenticationProvider;
import com.green.tablecheck.domain.dto.ShopDto;
import com.green.tablecheck.domain.dto.UserVo;
import com.green.tablecheck.domain.form.AddShopForm;
import com.green.tablecheck.service.manager.ManagerService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<ShopDto> addShop(@RequestHeader(name = TOKEN_NAME) String token,
        @RequestBody @Valid AddShopForm form) {
        UserVo vo = provider.getUserVo(token);  // manager
        return ResponseEntity.ok(ShopDto.from(managerService.addShop(vo.getId(), form)));
    }

}
