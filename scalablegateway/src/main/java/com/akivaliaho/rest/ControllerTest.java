package com.akivaliaho.rest;

import com.akivaliaho.config.annotations.Interest;
import com.akivaliaho.service.CalculationService;
import com.akivaliaho.service.events.CalculateSuperHardSumEvent;
import mockit.Mock;
import mockit.MockUp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;

/**
 * Created by akivv on 6.5.2017.
 */
@Configuration
public class ControllerTest {

    @Bean
    public CalculationService calculationService() {
        CalculationService calculationServiceMockUp = new MockUp<CalculationService>() {
            @Mock
            @Async
            @Interest(value = "com.akivaliaho.CalculateSuperHardSumResultEvent", emit = CalculateSuperHardSumEvent.class)
            public void doSuperHardcalculation(Integer number1, Integer number32) {
                //DO Nothing
                System.out.println(number1);
            }
        }.getMockInstance();
        return calculationServiceMockUp;
    }


}
