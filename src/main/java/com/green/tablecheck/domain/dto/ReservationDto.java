package com.green.tablecheck.domain.dto;

import com.green.tablecheck.domain.model.Reservation;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {

    private String shopName;
    private String customerName;
    private LocalDateTime dateTime;

    public static ReservationDto from(Reservation reservation) {
        return ReservationDto.builder()
            .shopName(reservation.getShop().getName())
            .customerName(reservation.getCustomer().getName())
            .dateTime(reservation.getDeadline())
            .build();
    }

}
