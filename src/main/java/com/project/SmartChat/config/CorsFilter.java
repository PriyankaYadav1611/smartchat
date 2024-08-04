package com.project.SmartChat.config;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("----------------------------------------------------------------------");
        System.out.println("Inside CorsFilter.doFilter");
        System.out.println("----------------------------------------------------------------------");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        res.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
        res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        res.setHeader("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
        res.setHeader("Access-Control-Allow-Credentials", "true");

        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            System.out.println("----------------------------------------------------------------------");
            res.setStatus(HttpServletResponse.SC_OK);
            System.out.println("Inside CorsFilter.doFilter If");
            System.out.println("----------------------------------------------------------------------");
        } else {
            System.out.println("----------------------------------------------------------------------");
            System.out.println("Inside CorsFilter.doFilter else");
            System.out.println("----------------------------------------------------------------------");
            chain.doFilter(request, response);
        }
    }
}
