package com.amangoes.product.config;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObservationConfig {
    // this class is used to when we have to allow observation
    @Bean
    ObservedAspect  observedAspect(ObservationRegistry registry){
        return new ObservedAspect(registry);
    }
}
