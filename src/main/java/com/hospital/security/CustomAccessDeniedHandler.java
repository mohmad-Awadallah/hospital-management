package com.hospital.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Enumeration;
import java.util.stream.Collectors;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        // تسجيل الوقت والتاريخ
        LocalDateTime now = LocalDateTime.now();
        logger.warn("وقت الحدث: {}", now);

        // تسجيل تفاصيل الطلب
        logger.warn("تم رفض الوصول للموارد: {}", request.getRequestURI());
        logger.warn("طريقة الطلب: {}, عنوان الآي بي: {}", request.getMethod(), request.getRemoteAddr());

        // تسجيل الرأسيات (Headers)
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            logger.warn("الرأسية: {} = {}", headerName, request.getHeader(headerName));
        }

        // تسجيل المعلمات (Parameters)
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            logger.warn("المعلمة: {} = {}", paramName, request.getParameter(paramName));
        }

        // تسجيل اسم المستخدم
        String username = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "مستخدم غير مسجل";
        logger.warn("المستخدم الذي حاول الوصول: {}", username);

        // تسجيل الأدوار المخصصة للمستخدم
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            String roles = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(", "));
            logger.warn("الأدوار المخصصة للمستخدم: {}", roles);
        }

        // تسجيل سبب الرفض
        logger.warn("سبب الرفض: {}", accessDeniedException.getMessage());

        // تعيين نوع المحتوى على أنه JSON مع الترميز UTF-8
        response.setContentType("application/json; charset=UTF-8");

        // تعيين حالة الاستجابة إلى محظور (403)
        response.setStatus(HttpStatus.FORBIDDEN.value());

        // كتابة رسالة الخطأ في الاستجابة
        try (PrintWriter writer = response.getWriter()) {
            writer.write("{\n" +
                    "  \"timestamp\": \"" + now + "\",\n" +
                    "  \"status\": 403,\n" +
                    "  \"error\": \"Forbidden\",\n" +
                    "  \"message\": \"لا توجد صلاحيات للوصول إلى هذا المورد. تأكد من أن لديك الأدوار الصحيحة.\",\n" +
                    "  \"path\": \"" + request.getRequestURI() + "\",\n" +
                    "  \"user\": \"" + username + "\",\n" +
                    "  \"roles\": \"" + (authentication != null ? authentication.getAuthorities() : "غير متوفر") + "\"\n" +
                    "}");
        }
    }
}