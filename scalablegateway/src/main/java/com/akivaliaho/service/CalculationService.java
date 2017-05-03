package com.akivaliaho.service;

import com.akivaliaho.amqp.EventUtil;
import com.akivaliaho.service.events.CalculateHardSumEvent;
import com.akivaliaho.service.events.CalculateSuperHardSumEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Created by vagrant on 4/5/17.
 */
@Service
public class CalculationService {
	@Autowired
	EventUtil eventUtil;

	@Async
	public ListenableFuture<Integer> doHardCalculation(Integer number1, Integer number2, DeferredResult<Integer> gatewayDeferredResult) {
		return eventUtil.publishEvent(
				new CalculateHardSumEvent(number1, number2),
				gatewayDeferredResult);
	}

	@Async
	public ListenableFuture<Integer> doSuperHardcalculation(Integer number1, Integer number32, DeferredResult<Integer> gatewayDeferredResult) {
		return eventUtil.publishEvent(new CalculateSuperHardSumEvent(number1, number32), gatewayDeferredResult);
	}

}
