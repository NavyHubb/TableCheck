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

        // 2. 토큰 발행 및 반환
        return provider.createToken(m.getEmail(), m.getId(), UserType.MANAGER);
    }

    public String customerSignIn(SignInForm form) {
        Customer c = customerRepository.findByEmail(form.getEmail())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CUSTOMER));

        // 2. 토큰 발행 및 반환
        return provider.createToken(c.getEmail(), c.getId(), UserType.CUSTOMER);
    }

}
