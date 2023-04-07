package com.green.tablecheck.service.manager;

import com.green.tablecheck.domain.form.AddShopForm;
import com.green.tablecheck.domain.model.Manager;
import com.green.tablecheck.domain.model.Shop;
import com.green.tablecheck.exception.CustomException;
import com.green.tablecheck.exception.ErrorCode;
import com.green.tablecheck.repository.ManagerRepository;
import com.green.tablecheck.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ShopService {

    private final ShopRepository shopRepository;
    private final ManagerRepository managerRepository;

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

        return shop;
    }

}
