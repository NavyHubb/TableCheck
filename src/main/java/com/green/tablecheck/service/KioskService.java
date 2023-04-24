package com.green.tablecheck.service;

import com.green.tablecheck.domain.model.Reservation;
import com.green.tablecheck.domain.type.ApprovalType;
import com.green.tablecheck.domain.type.AttendType;
import com.green.tablecheck.exception.CustomException;
import com.green.tablecheck.exception.ErrorCode;
import com.green.tablecheck.repository.ReservationRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class KioskService {

    private final ReservationRepository reservationRepository;

    /**
     * 방문 신청
     * 고객이 매장의 키오스크를 통해 매장에 방문했음을 알리는 기능
     */
    public String attend(Long shopId, String phone) {
        // 오늘 날짜에 해당하는 예약 조회
        Reservation reservation = getReservationOrElseThrow(shopId, phone);

        if (!reservation.getApprovalType().equals(ApprovalType.APPROVED)) {
            throw new CustomException(ErrorCode.NOT_APPORVED);
        }

        if (reservation.getDeadline().isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.RESERVATION_CLOSED);
        }

        reservation.setAttendType(AttendType.CHECKING);
        reservationRepository.save(reservation);

        return "코드를 확인해주세요.";
    }

    private Reservation getReservationOrElseThrow(Long shopId, String phone) {
        Reservation reservation = reservationRepository.findByShopId(shopId).stream()
                                            .filter(r -> r.getCustomer().getPhone().equals(phone)
                                                && r.getDeadline().toLocalDate().isEqual(LocalDate.now()))
                                            .findFirst()
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RESERVATION));
        return reservation;
    }

    /**
     * 고객에게 할당된 예약 확인 코드를 확인함으로써 방문 처리 진행
     */
    public String checkCode(Long shopId, String phone, String code) {
        Reservation reservation = getReservationOrElseThrow(shopId, phone);

        if (reservation.getCode().equals(code)) {
            reservation.setAttendType(AttendType.ATTEND);
            reservationRepository.save(reservation);

            return "코드가 확인되었습니다.";
        } else {
            throw new CustomException(ErrorCode.WRONG_CODE);
        }
    }

}