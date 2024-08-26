package com.project.SmartChat.config;


import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.SmartChat.service.JwtService;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtRequestFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Log all headers
        Enumeration<String> headerNames = request.getHeaderNames();

        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                System.out.println(headerName + ": " + request.getHeader(headerName));
            }
        }        
        final String requestTokenHeader = request.getHeader("Authorization");
        System.out.println("Header: " + requestTokenHeader);
        System.out.println("HeaderTest: " + request.getHeader("Auth"));

        // Skip specific URLs from checking token
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/api/users/auth/register") ||
            requestURI.startsWith("/api/users/auth/login") ||
            requestURI.startsWith("/ws/")) {
            chain.doFilter(request, response);
            return;
        }


        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            System.out.println("Inside 1st if");
            jwtToken = requestTokenHeader.substring(7);
            try {
                System.out.println("Inside try");
                username = jwtService.extractUsername(jwtToken);
                System.out.println("after extractusername");
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (jwtService.validateToken(jwtToken, userDetails)){
                    System.out.println("inside 2nd if");
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        System.out.println("Goting to set securityContextHolder");
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;
                        usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken); 
                    } else {
                        System.out.println("securityContextHolder is already set");
                    }
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid token, IlleggalArg e: " + e);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("JWT Token is invalid!");
                return;
            } catch (ExpiredJwtException e) {
                System.out.println("Expired token, e: " + e);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("JWT Token has expired, please regenerate it!");
                return;
            } catch (Exception e) {
                System.out.println("Invalid token, Exception e: " + e);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("JWT Token is invalid");
                return;
            }
            
        } else {
            System.out.println("JWT Token not exist");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("JWT Token doesn't exist!");
            return;
        }
        chain.doFilter(request, response);
    }
}

