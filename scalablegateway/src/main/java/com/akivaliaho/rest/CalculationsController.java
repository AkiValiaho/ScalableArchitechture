package com.akivaliaho.rest;

import com.akivaliaho.service.CalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by vagrant on 4/5/17.
 */
@RestController
@RequestMapping("/api")
public class CalculationsController {
    @Autowired
    CalculationService calculationService;

    @RequestMapping(value = "/hardCalculation", method = RequestMethod.POST)
    public void doHardCalculation(@RequestParam Integer number1, Integer number2) {
        calculationService.callServiceMethod("doHardCalculation", number1, number2);
    }

    @RequestMapping(value = "/doSuperHardCalculation", method = RequestMethod.POST)
    public void doSuperHardCalculation(@RequestParam Integer number1, Integer number32) {
        calculationService.doSuperHardcalculation(number1, number32);
    }
}
