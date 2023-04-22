package com.green.tablecheck.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    NOT_FOUND_MANAGER(HttpStatus.BAD_REQUEST, "존재하지 않는 매니저입니다."),
    NOT_FOUND_CUSTOMER(HttpStatus.BAD_REQUEST, "존재하지 않는 고객입니다."),
    NOT_FOUND_SHOP(HttpStatus.BAD_REQUEST, "존재하지 않는 매장입니다."),
    NOT_FOUND_RESERVATION(HttpStatus.BAD_REQUEST, "예약 내역이 없습니다."),

    ALREADY_REGISTERED_USER(HttpStatus.BAD_REQUEST, "이미 가입된 회원입니다."),
    SHOP_CLOSED(HttpStatus.BAD_REQUEST, "현재 영업 중이 아닙니다."),
    RESERVATION_CLOSED(HttpStatus.BAD_REQUEST, "마감된 예약입니다."),
    UNREGISTERED_SHOP(HttpStatus.BAD_REQUEST, "매장을 먼저 등록해주세요."),
    NOT_ATTEND(HttpStatus.BAD_REQUEST, "방문한 예약이 아닙니다."),
    NOT_IN_CHECKING(HttpStatus.BAD_REQUEST, "현재 코드 확인 상태가 아닙니다."),
    NOT_APPORVED(HttpStatus.BAD_REQUEST, "승인되지 않은 예약입니다."),
    WRONG_CODE(HttpStatus.BAD_REQUEST, "올바른 코드를 입력해주세요."),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "올바른 비밀번호를 입력해주세요."),
    RESERVATION_NOT_FOR_TODAY(HttpStatus.BAD_REQUEST, "오늘날짜로만 예약 신청할 수 있습니다."),

    ALREADY_SHOP_EXIST(HttpStatus.BAD_REQUEST, "이미 매장을 가지고 있는 매니저입니다."),
    ALREADY_RESERVATION_EXIST(HttpStatus.BAD_REQUEST, "이미 같은 날짜에 예약내역이 존재합니다."),
    ALREADY_REVIEW_EXIST(HttpStatus.BAD_REQUEST, "이미 리뷰를 작성하셨습니다."),
    ALREADY_CHECKED_RESERVATION(HttpStatus.BAD_REQUEST, "이미 확인 처리된 예약입니다."),

    NO_AUTHORIZATION(HttpStatus.BAD_REQUEST, "해당 예약에 접근 권한이 없습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String detail;

}
