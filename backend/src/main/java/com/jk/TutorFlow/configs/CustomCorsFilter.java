package com.jk.TutorFlow.configs;

import com.jk.TutorFlow.models.Consts;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomCorsFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;

        res.setHeader("Access-Control-Allow-Origin", Consts.getFrontendURL());
        res.setHeader("Access-Control-Allow-Credentials", "true");
        res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        res.setHeader("Access-Control-Allow-Headers", "*");

        chain.doFilter(request, response);

        System.out.println("Final Response Headers:");
        System.out.println("Access-Control-Allow-Origin: " + res.getHeader("Access-Control-Allow-Origin"));
        System.out.println("Access-Control-Allow-Credentials: " + res.getHeader("Access-Control-Allow-Credentials"));
    }
}

