package com.coupon.coupon.filter;

import com.coupon.coupon.exception.CustomErrorCode;
import com.coupon.coupon.exception.CustomErrorResponse;
import com.coupon.coupon.exception.CustomException;
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
                throw new CustomException(CustomErrorCode.LOGIN_REQUIRED);
            }
            chain.doFilter(request, response);  // 다음 필터가 있으면 필터를 호출하고, 필터가 없으면 서블릿을 호출
        } catch (Exception e) {
            if ( e instanceof CustomException) {
                CustomException customException = (CustomException) e;
                sendErrorResponse(httpResponse, customException.getErrorCode());
            } else {
                sendErrorResponse(httpResponse, CustomErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
    }

    private void sendErrorResponse(HttpServletResponse response, CustomErrorCode errorCode) throws IOException {
        CustomErrorResponse errorResponse = new CustomErrorResponse(errorCode.getStatus(), errorCode.getMessage());
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBody = objectMapper.writeValueAsString(errorResponse);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(responseBody);
    }
}
