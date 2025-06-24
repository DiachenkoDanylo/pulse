package com.diachenko.dev_pulse_github_service.config;
/*  Dev_Pulse
    20.06.2025
    @author DiachenkoDanylo
*/

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
