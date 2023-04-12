package com.green.tablecheck.service;

import com.green.tablecheck.domain.model.Reservation;
import com.green.tablecheck.domain.type.AttendType;
import com.green.tablecheck.exception.CustomException;
import com.green.tablecheck.exception.ErrorCode;
import com.green.tablecheck.repository.ReservationRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class KioskService {

    private final ReservationRepository reservationRepository;

    @Transactional(readOnly = true)
    public String attend(Long shopId, String phone) {
        Reservation reservation = reservationRepository.findByShopId(shopId).stream()
                                            .filter(r -> r.getCustomer().getPhone().equals(phone))
                                            .findFirst()
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RESERVATION));

        if (reservation.getDeadline().isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.RESERVATION_CLOSED);
        }

        return reservation.getCode();
    }

    public String checkCode(Long shopId, String phone, String code) {
        Reservation reservation = reservationRepository.findByShopId(shopId).stream()
            .filter(r -> r.getCustomer().getPhone().equals(phone))
            .findFirst()
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RESERVATION));

        if (reservation.getCode().equals(code)) {
            reservation.setAttendType(AttendType.ATTEND);
            reservationRepository.save(reservation);

            return "코드가 확인되었습니다.";
        } else {
            throw new CustomException(ErrorCode.WRONG_CODE);
        }
    }

}
