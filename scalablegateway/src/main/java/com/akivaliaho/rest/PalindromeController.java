package com.akivaliaho.rest;

import com.akivaliaho.service.PalindromeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Created by akivv on 6.7.2017.
 */
@RestController
@RequestMapping("/api")
public class PalindromeController {
    @Autowired
    PalindromeService palindromeService;

    @RequestMapping(name = "/checkifPalindrome", method = RequestMethod.POST)
    public DeferredResult<Boolean> checkifPalindrome(String stringToCheck) {
        return palindromeService.checkIfPalindrome(stringToCheck);
    }
}
