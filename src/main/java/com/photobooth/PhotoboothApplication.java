package com.photobooth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ============================================
 * Photobooth Application
 * ============================================
 * Main entry point for the Spring Boot application.
 * 
 * Features:
 * - REST API for Users, Sessions, Photos
 * - MySQL database with JPA/Hibernate
 * - Swagger documentation at /api/docs
 * ============================================
 */
@SpringBootApplication
public class PhotoboothApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhotoboothApplication.class, args);

        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║         Photobooth Backend is running!           ║");
        System.out.println("╠══════════════════════════════════════════════════╣");
        System.out.println("║    Server:    http://localhost:3000              ║");
        System.out.println("║    Swagger:   http://localhost:3000/api/docs     ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println();
    }
}
