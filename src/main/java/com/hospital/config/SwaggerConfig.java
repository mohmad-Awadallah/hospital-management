package com.hospital.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Profile("dev") // يعمل فقط في بيئة التطوير
@Configuration
public class SwaggerConfig {


}