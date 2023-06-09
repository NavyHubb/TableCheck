package com.green.tablecheck.service;

import com.green.tablecheck.domain.model.Shop;
import com.green.tablecheck.exception.CustomException;
import com.green.tablecheck.exception.ErrorCode;
import com.green.tablecheck.repository.ShopRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.Trie;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final Trie trie;

    private final ShopRepository shopRepository;

    /**
     * 매장 검색
     * keyword를 prefix로 갖는 상점명들의 목록을 반환
     */
    public List<String> searchShop(String keyword) {
        return (List<String>) this.trie.prefixMap(keyword).keySet()
            .stream().collect(Collectors.toList());
    }

    /**
     * 매장 조회
     */
    public Shop getShop(String shopName) {
        return shopRepository.findByName(shopName)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SHOP));
    }



}
