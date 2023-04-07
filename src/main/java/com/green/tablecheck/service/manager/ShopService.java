package com.green.tablecheck.service.manager;

import com.green.tablecheck.domain.form.AddShopForm;
import com.green.tablecheck.domain.model.Manager;
import com.green.tablecheck.domain.model.Shop;
import com.green.tablecheck.exception.CustomException;
import com.green.tablecheck.exception.ErrorCode;
import com.green.tablecheck.repository.ManagerRepository;
import com.green.tablecheck.repository.ShopRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.Trie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ShopService {

    private final Trie trie;

    private final ShopRepository shopRepository;
    private final ManagerRepository managerRepository;

    // 상점 등록
    public Shop addShop(Long managerId, AddShopForm form) {
        Manager manager = managerRepository.findById(managerId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MANAGER));

        // 이미 매장을 등록한 매니저인 경우
        if (shopRepository.findByManagerId(managerId).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_SHOP_EXIST);
        }

        // 새로운 shop 생성하여 manager 객체에 등록
        Shop shop = Shop.builder()
                        .name(form.getName())
                        .description(form.getDescription())
                        .address(form.getAddress())
                        .tableCount(form.getTableCount())
                        .build();
        manager.setShop(shop);

        shopRepository.save(shop);

        addAutocompleteKeyword(shop.getName());

        return shop;
    }

    // keyword를 prefix로 갖는 상점명들의 목록을 반환
    public List<String> searchShop(String keyword) {
        return (List<String>) this.trie.prefixMap(keyword).keySet()
            .stream().collect(Collectors.toList());
    }

    // Trie에 상점명 등록
    public void addAutocompleteKeyword(String keyword) {
        this.trie.put(keyword, null);
    }

}
