package com.example.controller;

import jakarta.servlet.http.HttpSession;

import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class CSRFGen {
    public static String generateCsrfToken(jakarta.servlet.http.HttpSession session) {
        // Generate a unique CSRF token
        String csrfToken = UUID.randomUUID().toString();
        session.setAttribute("csrfToken", csrfToken);  // Store CSRF token in session
        return csrfToken;
    }

    public static boolean validateCsrfToken(jakarta.servlet.http.HttpSession session, String tokenFromRequest) {
        // Retrieve the CSRF token from the session
        String sessionToken = (String) session.getAttribute("csrfToken");
        return sessionToken != null && sessionToken.equals(tokenFromRequest);
    }

    public static boolean validateReferrerHeader(HttpServletRequest request) {
        String refererHeader = request.getHeader("Referer");
        String expectedDomain = "http://localhost:8080/";  // Define expected domain
        return refererHeader != null && refererHeader.startsWith(expectedDomain);
    }
}