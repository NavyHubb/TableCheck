package com.green.tablecheck.service.customer;

import com.green.tablecheck.domain.SignUpForm;
import com.green.tablecheck.domain.model.Customer;
import com.green.tablecheck.exception.CustomException;
import com.green.tablecheck.exception.ErrorCode;
import com.green.tablecheck.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpCustomerService {

    private final CustomerRepository customerRepository;

    // 회원가입
    public String signUp(SignUpForm form) {
        if (isEmailExist(form.getEmail())) {
            throw new CustomException(ErrorCode.ALREADY_REGISTERED_USER);
        }

        customerRepository.save(Customer.from(form));

        return "회원가입이 완료되었습니다.";
    }

    // 이메일 중복여부 확인
    private boolean isEmailExist(String email) {
        return customerRepository.findByEmail(email).isPresent();
    }

}
