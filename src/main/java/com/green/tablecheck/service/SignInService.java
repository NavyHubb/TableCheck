package com.green.tablecheck.service;

import com.green.tablecheck.config.JwtAuthenticationProvider;
import com.green.tablecheck.domain.form.SignInForm;
import com.green.tablecheck.domain.model.Customer;
import com.green.tablecheck.domain.model.Manager;
import com.green.tablecheck.domain.type.UserType;
import com.green.tablecheck.exception.CustomException;
import com.green.tablecheck.exception.ErrorCode;
import com.green.tablecheck.repository.CustomerRepository;
import com.green.tablecheck.repository.ManagerRepository;
import com.green.tablecheck.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInService {

    private final ManagerRepository managerRepository;
    private final CustomerRepository customerRepository;
    private final JwtAuthenticationProvider provider;

    public String managerSignIn(SignInForm form) {
        Manager m = managerRepository.findByEmail(form.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MANAGER));

        // 비밀번호 검증
        if (!PasswordUtil.checkPassword(form.getPassword(), m.getPassword())) {
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }

        // 토큰 발행 및 반환
        return provider.createToken(m.getEmail(), m.getId(), UserType.MANAGER);
    }

    public String customerSignIn(SignInForm form) {
        Customer c = customerRepository.findByEmail(form.getEmail())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CUSTOMER));

        // 비밀번호 검증
        if (!PasswordUtil.checkPassword(form.getPassword(), c.getPassword())) {
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }

        // 2. 토큰 발행 및 반환
        return provider.createToken(c.getEmail(), c.getId(), UserType.CUSTOMER);
    }

}
