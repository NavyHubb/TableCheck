package com.green.tablecheck.service.manager;

import com.green.tablecheck.domain.form.AddShopForm;
import com.green.tablecheck.domain.form.ReservationForm;
import com.green.tablecheck.domain.model.Customer;
import com.green.tablecheck.domain.model.Manager;
import com.green.tablecheck.domain.model.Reservation;
import com.green.tablecheck.domain.model.Shop;
import com.green.tablecheck.domain.type.StatusType;
import com.green.tablecheck.exception.CustomException;
import com.green.tablecheck.exception.ErrorCode;
import com.green.tablecheck.repository.CustomerRepository;
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
    private final CustomerRepository customerRepository;

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
                        .statusType(StatusType.CLOSED)
                        .build();
        manager.setShop(shop);

        // Manager 엔티티가 Shop 엔티티에 영속성 전이 설정이 되어 있기 때문에
        // manager 객체에 shop 객체를 세팅한 뒤 manager를 영속화하면 여기에 연관되어 있는 shop까지 함께 영속화됨
        managerRepository.save(manager);

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

    public Shop getShop(String shopName) {
        Shop shop = shopRepository.findByName(shopName)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SHOP));

        return shop;
    }

    public String reserveShop(Long shopId, Long customerId, ReservationForm form) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CUSTOMER));

        Shop shop = shopRepository.findById(shopId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SHOP));

        // 현재 영업 중이 아닌 경우
        if (shop.getStatusType().equals(StatusType.CLOSED)) {
            throw new CustomException(ErrorCode.SHOP_CLOSED);
        }

        Reservation reservation = Reservation.builder()
                                            .shop(shop)
                                            .customer(customer)
                                            .dateTime(form.getDateTime())
                                            .peopleCount(form.getPeopleCount())
                                            .build();

        shop.getReservation().add(reservation);
        shopRepository.save(shop);

        return "예약이 완료되었습니다.";
    }

}
