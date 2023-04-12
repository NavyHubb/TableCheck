package com.green.tablecheck.config.filter;

import com.green.tablecheck.config.JwtAuthenticationProvider;
import com.green.tablecheck.domain.dto.UserVo;
import com.green.tablecheck.repository.ManagerRepository;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@WebFilter(urlPatterns = "/manager/*")
@RequiredArgsConstructor
public class ManagerFilter implements Filter {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final ManagerRepository managerRepository;

    // 요청을 처리하기 전에 필터에서 토큰 인증
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("X-AUTH-TOKEN");

        // 유효성 검사
        if (!jwtAuthenticationProvider.validateToken(token)) {
            throw new ServletException("Invalid Access");
        }

        // 토큰이 담고 있는 사용자 정보가 존재하는 건지 확인
        UserVo vo = jwtAuthenticationProvider.getUserVo(token);
        managerRepository.findByIdAndEmail(vo.getId(), vo.getEmail()).orElseThrow(
            () -> new ServletException("NOT_FOUND_MANAGER")
        );

        chain.doFilter(request, response);
    }
}