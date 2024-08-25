package com.project.SmartChat.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("----------------------------------------------------------------------");
        System.out.println("Inside CorsFilter.doFilter");
        System.out.println("----------------------------------------------------------------------");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        res.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
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
