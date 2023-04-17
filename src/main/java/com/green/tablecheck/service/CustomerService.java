package com.green.tablecheck.service;

import com.green.tablecheck.domain.form.ReservationForm;
import com.green.tablecheck.domain.form.ReviewForm;
import com.green.tablecheck.domain.model.Customer;
import com.green.tablecheck.domain.model.Reservation;
import com.green.tablecheck.domain.model.Review;
import com.green.tablecheck.domain.model.Shop;
import com.green.tablecheck.domain.type.ApprovalType;
import com.green.tablecheck.domain.type.AttendType;
import com.green.tablecheck.domain.type.StatusType;
import com.green.tablecheck.exception.CustomException;
import com.green.tablecheck.exception.ErrorCode;
import com.green.tablecheck.repository.CustomerRepository;
import com.green.tablecheck.repository.ReservationRepository;
import com.green.tablecheck.repository.ShopRepository;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {

    private final ShopRepository shopRepository;
    private final CustomerRepository customerRepository;
    private final ReservationRepository reservationRepository;

    public String reserveShop(Long shopId, Long customerId, ReservationForm form) {
        // 객체 조회
        Customer customer = getCustomerOrElseThrow(customerId);
        Shop shop = getShopOrElseThrow(shopId);

        // 현재 영업 중이 아닌 경우
        if (shop.getStatusType().equals(StatusType.CLOSED)) {
            throw new CustomException(ErrorCode.SHOP_CLOSED);
        }

        // 이미 같은 날에 예약이 존재하는 경우
        Optional<Reservation> oldReservation = reservationRepository.findByShopIdAndCustomerId(shopId,
                customerId)
            .filter(r -> r.getDeadline().toLocalDate().isEqual(form.getDateTime().toLocalDate()));
        if (oldReservation.isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_RESERVATION_EXIST);
        }

        Reservation reservation = Reservation.builder()
            .shop(shop)
            .customer(customer)
            .deadline(form.getDateTime().minusMinutes(10))  // 예약 시간의 10분 전을 마감시각으로 설정
            .peopleCount(form.getPeopleCount())
            .code(generateCode())
            .attendType(AttendType.DEFAULT)
            .approvalType(ApprovalType.WAITING)
            .build();

        shop.getReservation().add(reservation);
        shopRepository.save(shop);

        return "예약 신청이 완료되었습니다.";
    }

    private Shop getShopOrElseThrow(Long shopId) {
        return shopRepository.findById(shopId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SHOP));
    }

    private Customer getCustomerOrElseThrow(Long customerId) {
        return customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CUSTOMER));
    }

    private String generateCode() {
        double min = 1000;
        double max = 10000;
        int code = (int) ((Math.random() * (max - min)) + min);

        return Integer.toString(code);
    }

    public String getCode(Long reservationId, Long customerId) {
        Reservation reservation = getReservationOrElseThrow(reservationId);
        Customer customer = getCustomerOrElseThrow(customerId);

        if (!Objects.equals(customerId, reservation.getCustomer().getId())) {
            throw new CustomException(ErrorCode.NO_AUTHORIZATION);
        }

        if (reservation.getAttendType() != AttendType.CHECKING) {
            throw new CustomException(ErrorCode.NOT_IN_CHECKING);
        }

        return reservation.getCode();
    }

    private Reservation getReservationOrElseThrow(Long reservationId) {
        return reservationRepository.findById(reservationId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RESERVATION));
    }

    public String review(Long reservationId, ReviewForm form) {
        Reservation reservation = getReservationOrElseThrow(reservationId);

        if (reservation.getAttendType() != AttendType.ATTEND) {
            throw new CustomException(ErrorCode.NOT_ATTEND);
        }

        if (reservation.hasReview()) {
            throw new CustomException(ErrorCode.ALREADY_REVIEW_EXIST);
        }

        Review review = Review.builder()
            .star(form.getStar())
            .message(form.getMessage())
            .build();

        reservation.setReview(review);
        reservationRepository.save(reservation);

        return "리뷰가 등록되었습니다.";
    }

    private boolean isReviewed(Reservation reservation) {
        try {
            if (reservation.getReview() != null) {
                throw new CustomException(ErrorCode.ALREADY_REVIEW_EXIST);
            }
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

}
