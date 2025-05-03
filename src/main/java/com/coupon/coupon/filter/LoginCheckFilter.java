package com.coupon.coupon.filter;

import com.coupon.coupon.exception.CouponErrorCode;
import com.coupon.coupon.exception.CouponResponse;
import com.coupon.coupon.exception.CouponException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;

import java.io.IOException;

@Order()
public class LoginCheckFilter implements Filter {
    private static final String[] whiteListURLs = {"/login", "/signup"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            for (String url : whiteListURLs) {
                if (requestURI.equals(url)) {
                    chain.doFilter(request, response);  // 화이트리스트에 있는 URL은 필터를 통과
                    return;
                }
            }

            if (httpRequest.getSession(false) == null)  {
                throw new CouponException(CouponErrorCode.LOGIN_REQUIRED);
            }
            chain.doFilter(request, response);  // 다음 필터가 있으면 필터를 호출하고, 필터가 없으면 서블릿을 호출
        } catch (Exception e) {
            if ( e instanceof CouponException) {
                CouponException couponException = (CouponException) e;
                sendErrorResponse(httpResponse, couponException.getErrorCode());
            } else {
                sendErrorResponse(httpResponse, CouponErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
    }

    private void sendErrorResponse(HttpServletResponse response, CouponErrorCode errorCode) throws IOException {
        CouponResponse errorResponse = new CouponResponse(errorCode.getStatus(), errorCode.getMessage(), null);
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBody = objectMapper.writeValueAsString(errorResponse);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(responseBody);
    }
}
