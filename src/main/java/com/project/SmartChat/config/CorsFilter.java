package com.project.SmartChat.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
public class CorsFilter extends OncePerRequestFilter {

    @Value("${cors.allowed.origins}")
    private String allowedOrigins;

    public CorsFilter() {
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        System.out.println("----------------------------------------------------------------------");
        System.out.println("Inside CorsFilter.doFilterInternal");
        System.out.println("----------------------------------------------------------------------");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        res.setHeader("Access-Control-Allow-Origin", allowedOrigins);
        res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        res.setHeader("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
        res.setHeader("Access-Control-Allow-Credentials", "true");

        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            System.out.println("----------------------------------------------------------------------");
            res.setStatus(HttpServletResponse.SC_OK);
            System.out.println("Inside CorsFilter.doFilterInternal If");
            System.out.println("This is a preflight request, so, allow it and return from here");
            System.out.println("----------------------------------------------------------------------");
            return;
        } else {

            System.out.println("----------------------------------------------------------------------");
            System.out.println("Inside CorsFilter.doFilterInternal else");
            System.out.println("This is after preflight request, so, go to the next filter");
            System.out.println("----------------------------------------------------------------------");
            chain.doFilter(request, response);
        }
    }
}