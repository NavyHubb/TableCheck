package com.green.tablecheck.domain.form;

import javax.validation.constraints.Size;
import lombok.Getter;

@Getter
public class AddShopForm {

    private String name;

    @Size(max = 100, message = "매장 설명은 100자 이하로 작성해주세요.")
    private String description;
    private String address;
    private int tableCount;

}
