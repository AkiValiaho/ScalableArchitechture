package com.akivaliaho.service;

import com.akivaliaho.CheckIfPalindromeEvent;
import com.akivaliaho.CheckIfPalindromeEventResult;
import com.akivaliaho.config.annotations.Interest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Created by akivv on 6.7.2017.
 */
@Service
public class PalindromeService {
    @Interest(emits = CheckIfPalindromeEvent.class, receives = CheckIfPalindromeEventResult.class)
    public DeferredResult<Boolean> checkIfPalindrome(String stringToCheck) {
        return null;
    }
}
