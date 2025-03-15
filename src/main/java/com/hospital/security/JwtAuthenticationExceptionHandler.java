package com.hospital.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * هذه الفئة تقوم بمعالجة استثناءات التوثيق (AuthenticationException) وإرجاع استجابة JSON مخصصة.
 * تُستخدم في التعامل مع أخطاء JWT وغيرها من أخطاء المصادقة.
 */
@Component
public class JwtAuthenticationExceptionHandler implements AuthenticationEntryPoint {

    // إنشاء ObjectMapper ثابت لتقليل تكلفة إنشاء الكائن في كل مرة
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * تُستدعى هذه الدالة عند فشل التوثيق، وتقوم بإرجاع استجابة HTTP تحمل تفاصيل الخطأ.
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // هنا نستخدم حالة HTTP UNAUTHORIZED بشكل افتراضي
        handleException(request, response, authException, HttpStatus.UNAUTHORIZED);
    }


    public void handle(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // تعيين الحالة كـ Unauthorized
            response.getWriter().write("Unauthorized: Invalid or expired JWT token.");
        } catch (IOException e) {
            // التعامل مع الأخطاء في الكتابة إلى الاستجابة
            e.printStackTrace();
        }
    }

    /**
     * دالة معالجة الاستثناءات المخصصة، تقوم بتجميع تفاصيل الخطأ وإرجاعها في استجابة JSON.
     *
     * @param request   الطلب الحالي
     * @param response  الاستجابة التي سيتم إرسالها للعميل
     * @param ex        الاستثناء الذي حدث
     * @param status    حالة HTTP المراد إرجاعها
     * @throws IOException في حال حدوث خطأ أثناء الكتابة في الاستجابة
     */
    public void handleException(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException ex,
            HttpStatus status) throws IOException {

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("status", status.value());
        errorDetails.put("error", status.getReasonPhrase());
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("path", request.getRequestURI());
        errorDetails.put("timestamp", LocalDateTime.now().toString());

        if (ex instanceof JwtAuthenticationException jwtEx) {
            errorDetails.put("errorCode", jwtEx.getErrorCode());
        }

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(status.value());
        response.getWriter().write(mapper.writeValueAsString(errorDetails));
    }
}
