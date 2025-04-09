package com.powerbi.api;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Power BI API application.
 * This class initializes and starts the Spring Boot application.
 * It also enables autoconfiguration and scans for components within the specified base package.
 */
@SpringBootApplication(scanBasePackages = {"com.powerbi.api"})
@EnableAutoConfiguration
public class ApiApplication {
	/**
     * Main method that serves as the entry point for the Power BI API application.
     * It initializes the Spring Boot application context and starts the application.
     *
     * @param args command-line arguments passed to the application
     */
	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();  // Loads .env file
		SpringApplication.run(ApiApplication.class, args);
	}
}
