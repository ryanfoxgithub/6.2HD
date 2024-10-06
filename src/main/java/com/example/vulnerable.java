package com.exmaple; // Note: There's a typo in the package name.

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class vulnerable extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(vulnerable.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(vulnerable.class, args);
    }
}
