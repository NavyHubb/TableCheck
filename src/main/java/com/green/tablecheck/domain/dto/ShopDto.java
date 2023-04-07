package com.green.tablecheck.domain.dto;

import com.green.tablecheck.domain.model.Shop;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopDto {
    private Long id;
    private String name;
    private String description;
    private String address;

    public static ShopDto from(Shop shop) {
        return ShopDto.builder()
            .id(shop.getId())
            .name(shop.getName())
            .description(shop.getDescription())
            .address(shop.getAddress())
            .build();
    }
}
