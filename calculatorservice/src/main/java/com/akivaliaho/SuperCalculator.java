package com.akivaliaho;

import com.akivaliaho.config.annotations.Interest;
import org.springframework.stereotype.Component;

/**
 * Created by akivv on 1.5.2017.
 */
@Component
public class SuperCalculator {
	@Interest(value = "com.akivaliaho.service.events.CalculateSuperHardSumEvent")
	public CalculateSuperHardSumResultEvent calculateSuperHard(Integer one, Integer two) {
		int i = one + two;
		return new CalculateSuperHardSumResultEvent(i);
	}

}
