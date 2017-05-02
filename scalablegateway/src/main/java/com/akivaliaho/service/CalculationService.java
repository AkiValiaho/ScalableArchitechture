package com.akivaliaho.service;

import com.akivaliaho.amqp.EventUtil;
import com.akivaliaho.service.events.CalculateHardSumEvent;
import com.akivaliaho.service.events.CalculateSuperHardSumEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * Created by vagrant on 4/5/17.
 */
@Service
public class CalculationService {
	@Autowired
	EventUtil eventUtil;

	@Async
	public ListenableFuture<Integer> doHardCalculation(Integer number1, Integer number2) {
		return (ListenableFuture<Integer>) eventUtil.publishEvent(
				new CalculateHardSumEvent(number1, number2)
		);
	}

	@Async
	public ListenableFuture<Integer> doSuperHardcalculation(Integer number1, Integer number32) {
		//TODO refactor this horrific looking initialization
		return (ListenableFuture<Integer>) eventUtil.publishEvent(new CalculateSuperHardSumEvent(number1, number32));
	}

}
