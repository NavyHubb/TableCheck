package com.green.tablecheck.service;

import com.green.tablecheck.domain.form.AddShopForm;
import com.green.tablecheck.domain.model.Manager;
import com.green.tablecheck.domain.model.Shop;
import com.green.tablecheck.domain.type.StatusType;
import com.green.tablecheck.exception.CustomException;
import com.green.tablecheck.exception.ErrorCode;
import com.green.tablecheck.repository.ManagerRepository;
import com.green.tablecheck.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.Trie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ManagerService {

    private final Trie trie;

    private final ShopRepository shopRepository;
    private final ManagerRepository managerRepository;

    // 상점 등록
    public String addShop(Long managerId, AddShopForm form) {
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
            .statusType(StatusType.CLOSED)
            .build();
        manager.setShop(shop);

        // Manager 엔티티가 Shop 엔티티에 영속성 전이 설정이 되어 있기 때문에
        // manager 객체에 shop 객체를 세팅한 뒤 manager를 영속화하면 여기에 연관되어 있는 shop까지 함께 영속화됨
        managerRepository.save(manager);

        addAutocompleteKeyword(shop.getName());

        return "매장 등록이 완료되었습니다.";
    }

    // Trie에 상점명 등록
    public void addAutocompleteKeyword(String keyword) {
        this.trie.put(keyword, null);
    }


    public String changeStatus(Long managerId) {
        Manager manager = managerRepository.findById(managerId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MANAGER));

        Shop shop = manager.getShop();
        if (shop == null) {
            throw new CustomException(ErrorCode.UNREGISTERED_SHOP);
        }

        if (shop.getStatusType().equals(StatusType.CLOSED)) {
            shop.setStatusType(StatusType.OPEN);

            return "영업을 시작합니다.";
        } else {
            shop.setStatusType(StatusType.CLOSED);

            return "영업을 마칩니다.";
        }
    }
}
