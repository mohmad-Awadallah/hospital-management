package com.hospital.config;

import java.security.SecureRandom;
import java.util.Base64;

public class KeyGenerator {
    public static void main(String[] args) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[64]; // 64 بايت = 512 بت
        secureRandom.nextBytes(key);

        // ترميز Base64 URL-safe
        String encodedKey = Base64.getUrlEncoder().encodeToString(key);
        System.out.println("المفتاح المولد: " + encodedKey);
    }
}


