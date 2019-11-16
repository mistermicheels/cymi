package com.mistermicheels.cymi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.mistermicheels.cymi.config.security.SecurityProperties;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {
        // prevent automatic internal redirection to /error page on error
        ErrorMvcAutoConfiguration.class })
@EnableConfigurationProperties(SecurityProperties.class)
@EnableScheduling
public class CymiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CymiApplication.class, args);
    }

}
