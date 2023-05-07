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
import java.time.LocalDate;
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

    /**
     * 매장 예약하기
     */
    public String reserveShop(Long shopId, Long customerId, ReservationForm form) {
        // 객체 조회
        Customer customer = getCustomerOrElseThrow(customerId);
        Shop shop = getShopOrElseThrow(shopId);

        // 매장이 현재 영업 중이 아닌 경우
        if (!isOpen(shop)) {
            throw new CustomException(ErrorCode.SHOP_CLOSED);
        }

        // 예약 날짜가 오늘 날짜가 아닌 경우
        // 예약은 당일예약만 가능
        if (!isReservationForToday(form)) {
            throw new CustomException(ErrorCode.RESERVATION_NOT_FOR_TODAY);
        }

        // 이미 같은 날에 예약이 존재하는 경우
        Optional<Reservation> oldReservation = reservationRepository.findByShopIdAndCustomerId(shopId,
                customerId)
            .filter(r -> r.getDeadline().toLocalDate().isEqual(form.getDateTime().toLocalDate()));
        if (oldReservation.isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_RESERVATION_EXIST);
        }

        // 예약 생성 및 연관관계 설정
        createAndSetReservation(form, customer, shop);

        return "예약 신청이 완료되었습니다.";
    }

    private void createAndSetReservation(ReservationForm form, Customer customer, Shop shop) {
        Reservation reservation = Reservation.builder()
            .shop(shop)
            .customer(customer)
            .deadline(form.getDateTime().minusMinutes(10))  // 예약 시간의 10분 전을 마감시각으로 설정
            .peopleCount(form.getPeopleCount())
            .code(generateCode())
            .attendType(AttendType.DEFAULT)
            .approvalType(ApprovalType.WAITING)
            .build();

        shop.addReservation(reservation);  // 연관관계 편의 메서드
        shopRepository.save(shop);
    }

    private static boolean isReservationForToday(ReservationForm form) {
        return form.getDateTime().toLocalDate().equals(LocalDate.now());
    }

    private static boolean isOpen(Shop shop) {
        return shop.getStatusType().equals(StatusType.OPEN);
    }

    private Shop getShopOrElseThrow(Long shopId) {
        return shopRepository.findById(shopId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SHOP));
    }

    private Customer getCustomerOrElseThrow(Long customerId) {
        return customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CUSTOMER));
    }

    /**
     * 네 자리 수 랜덤 생성
     */
    private String generateCode() {
        double min = 1000;
        double max = 10000;
        int code = (int) ((Math.random() * (max - min)) + min);

        return Integer.toString(code);
    }

    public String getCode(Long reservationId, Long customerId) {
        Reservation reservation = getReservationOrElseThrow(reservationId);

        // 해당 예약 건을 예약한 당사자 고객이 맞는지 확인
        if (isRightCustomer(customerId, reservation)) {
            throw new CustomException(ErrorCode.NO_AUTHORIZATION);
        }

        // 고객이 키오스크에서 방문 확인을 완료했는지 확인
        if (reservation.getAttendType() != AttendType.CHECKING) {
            throw new CustomException(ErrorCode.NOT_IN_CHECKING);
        }

        return reservation.getCode();
    }

    private static boolean isRightCustomer(Long customerId, Reservation reservation) {
        return !Objects.equals(customerId, reservation.getCustomer().getId());
    }

    private Reservation getReservationOrElseThrow(Long reservationId) {
        return reservationRepository.findById(reservationId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RESERVATION));
    }

    /**
     * 리뷰 작성하기
     * 예약에 참석(ATTEND)했고, 아직 리뷰를 작성하지 않은 고객에 대해서만 진행
     */
    public String review(Long reservationId, ReviewForm form) {
        Reservation reservation = getReservationOrElseThrow(reservationId);

        // 해당 예약의 예약자가 방문했는지 확인
        if (reservation.getAttendType() != AttendType.ATTEND) {
            throw new CustomException(ErrorCode.NOT_ATTEND);
        }

        // 이미 작성된 리뷰가 있는지 확인
        if (reservation.hasReview()) {
            throw new CustomException(ErrorCode.ALREADY_REVIEW_EXIST);
        }

        // 리뷰 생성 및 연관관계 설정
        createAndSetReview(form, reservation);

        return "리뷰가 등록되었습니다.";
    }

    private void createAndSetReview(ReviewForm form, Reservation reservation) {
        Review review = Review.builder()
            .star(form.getStar())
            .message(form.getMessage())
            .build();

        reservation.setReview(review);
        reservationRepository.save(reservation);
    }

}
