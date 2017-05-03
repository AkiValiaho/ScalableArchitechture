package com.akivaliaho.rest;

import com.akivaliaho.AsyncWrapper;
import com.akivaliaho.service.CalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Created by vagrant on 4/5/17.
 */
@RestController
@RequestMapping("/api")
public class CalculationsController {
	@Autowired
	CalculationService calculationService;
	@Autowired
	AsyncWrapper asyncWrapper;

	@RequestMapping(value = "/hardCalculation", method = RequestMethod.POST)
	public DeferredResult<Integer> doHardCalculation(@RequestParam Integer number1, Integer number2) {
		DeferredResult<Integer> integerDeferredResult = new DeferredResult<>();
		calculationService.doHardCalculation(number1, number2, integerDeferredResult);
		return integerDeferredResult;
	}

	@RequestMapping(value = "/doSuperHardCalculation", method = RequestMethod.POST)
	public DeferredResult<Integer> doSuperHardCalculation(@RequestParam Integer number1, Integer number32) {
		DeferredResult<Integer> integerDeferredResult = new DeferredResult<>();
		calculationService.doSuperHardcalculation(number1, number32, integerDeferredResult);
		return integerDeferredResult;
	}
}
