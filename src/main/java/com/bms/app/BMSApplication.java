package com.bms.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class BMSApplication {

    public static void main(String[] args) {
        SpringApplication.run(BMSApplication.class, args);
    }

    @GetMapping("/")
    public String home() {
        return "BMS DevOps Application is Running!";
    }

    @GetMapping("/api/status")
    public String status() {
        return "BMS System Status: UP";
    }

    @GetMapping("/api/building")
    public String building() {
        return "Building: BMS Smart Building | Status: Operational";
    }

    @GetMapping("/api/sensors")
    public String sensors() {
        return "Sensors: Temperature, Humidity, Power, HVAC - All Operational";
    }
}
