package com.akivaliaho;

/**
 * Created by akivv on 6.7.2017.
 */
public class CheckIfPalindromeEventResult extends ServiceEventResult {
    public CheckIfPalindromeEventResult(boolean isPalindrome) {
        saveEvent(CheckIfPalindromeEventResult.class.getName(), isPalindrome);
    }
}
