package com.akivaliaho;

/**
 * Created by akivv on 6.7.2017.
 */
public class CheckIfPalindromeEvent extends ServiceEvent {
    public CheckIfPalindromeEvent(String stringToCheck) {
        saveEvent(CheckIfPalindromeEvent.class.getName(), stringToCheck);
    }
}

