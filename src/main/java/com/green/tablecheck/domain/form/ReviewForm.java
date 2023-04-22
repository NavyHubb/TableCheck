package com.green.tablecheck.domain.form;

import javax.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ReviewForm {

    @Size(min = 1, max = 5, message = "1 ~ 5 사이의 숫자를 입력해주세요.")
    private int star;

    private String message;

}
