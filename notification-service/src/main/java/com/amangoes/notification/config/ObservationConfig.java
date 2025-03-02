package com.amangoes.notification.config;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

@Configuration
@RequiredArgsConstructor
public class ObservationConfig {

    private final ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory;

    @PostConstruct
    public void setObservationKafkaTemplate(){
        kafkaListenerContainerFactory.getContainerProperties().setObservationEnabled(true);
    }

    // this class is used to when we have to allow observation
    @Bean
    ObservedAspect  observedAspect(ObservationRegistry registry){
        return new ObservedAspect(registry);
    }
}
