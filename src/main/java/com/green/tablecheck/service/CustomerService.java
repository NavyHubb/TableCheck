package com.green.tablecheck.service;

import com.green.tablecheck.domain.form.ReservationForm;
import com.green.tablecheck.domain.model.Customer;
import com.green.tablecheck.domain.model.Reservation;
import com.green.tablecheck.domain.model.Shop;
import com.green.tablecheck.domain.type.StatusType;
import com.green.tablecheck.exception.CustomException;
import com.green.tablecheck.exception.ErrorCode;
import com.green.tablecheck.repository.CustomerRepository;
import com.green.tablecheck.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {

    private final ShopRepository shopRepository;
    private final CustomerRepository customerRepository;

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
            .deadline(form.getDateTime().minusMinutes(10))  // 예약 시간의 10분 전을 마감시각으로 설정
            .peopleCount(form.getPeopleCount())
            .code(generateCode())
            .build();

        shop.getReservation().add(reservation);
        shopRepository.save(shop);

        return "예약이 완료되었습니다.";
    }

    private String generateCode() {
        double min = 1000;
        double max = 10000;
        int code = (int) ((Math.random() * (max - min)) + min);

        return Integer.toString(code);
    }

}
