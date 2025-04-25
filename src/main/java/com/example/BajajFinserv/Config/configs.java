package com.example.BajajFinserv.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class configs {
    @Bean
    public RestTemplate getrestTemplate() {
        return new RestTemplate();
    }
}
