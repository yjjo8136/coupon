package com.coupon.coupon.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

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

            if (httpRequest.getSession().getAttribute("currentUser") == null) {
                httpResponse.sendRedirect("/login");
                return; // 로그인 되어 있지 않으면 다음으로 진행하지 않음
            }
            chain.doFilter(request, response);  // 다음 필터가 있으면 필터를 호출하고, 필터가 없으면 서블릿을 호출
        } catch (Exception e) {
            httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

}
