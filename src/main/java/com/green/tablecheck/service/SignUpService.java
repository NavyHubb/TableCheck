package com.green.tablecheck.service;

import com.green.tablecheck.domain.form.SignUpForm;
import com.green.tablecheck.domain.model.Customer;
import com.green.tablecheck.domain.model.Manager;
import com.green.tablecheck.exception.CustomException;
import com.green.tablecheck.exception.ErrorCode;
import com.green.tablecheck.repository.CustomerRepository;
import com.green.tablecheck.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private final ManagerRepository managerRepository;
    private final CustomerRepository customerRepository;

    // 회원가입
    public String managerSignUp(SignUpForm form) {
        if (isEmailExistManager(form.getEmail())) {
            throw new CustomException(ErrorCode.ALREADY_REGISTERED_USER);
        }

        managerRepository.save(Manager.from(form));

        return "회원가입이 완료되었습니다.";
    }

    // 이메일 중복여부 확인
    private boolean isEmailExistManager(String email) {
        return managerRepository.findByEmail(email).isPresent();
    }

    // 회원가입
    public String customerSignUp(SignUpForm form) {
        if (isEmailExistCustomer(form.getEmail())) {
            throw new CustomException(ErrorCode.ALREADY_REGISTERED_USER);
        }

        customerRepository.save(Customer.from(form));

        return "회원가입이 완료되었습니다.";
    }

    // 이메일 중복여부 확인
    private boolean isEmailExistCustomer(String email) {
        return customerRepository.findByEmail(email).isPresent();
    }

}
