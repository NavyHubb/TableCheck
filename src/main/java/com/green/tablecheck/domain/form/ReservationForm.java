package com.green.tablecheck.domain.form;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ReservationForm {

    private int peopleCount;
    private LocalDateTime dateTime;

}
