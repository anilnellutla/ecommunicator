package com.ecommunicator.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * eCommunicator Server — Spring Boot entry point.
 *
 * Run:  java -jar ecommunicator-server.jar
 *       (or)  mvn spring-boot:run  inside ecommunicator-server/
 *
 * Default port: 8080  (override with --server.port=XXXX)
 */
@SpringBootApplication
@EnableScheduling
public class EcommunicatorServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommunicatorServerApplication.class, args);
    }
}
