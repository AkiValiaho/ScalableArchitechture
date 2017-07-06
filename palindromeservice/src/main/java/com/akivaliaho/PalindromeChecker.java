package com.akivaliaho;

import com.akivaliaho.config.annotations.Interest;
import org.springframework.stereotype.Component;

/**
 * Created by akivv on 1.5.2017.
 */
@Component
public class PalindromeChecker {
    @Interest(receives = CheckIfPalindromeEvent.class)
    public CheckIfPalindromeEventResult checkifPalindrome(String toCheck) {
        return new CheckIfPalindromeEventResult(isPalindrome(toCheck));
    }
    boolean isPalindrome(String s) {
        int n = s.length();
        for (int i = 0; i < (n/2); ++i) {
            if (s.charAt(i) != s.charAt(n - i - 1)) {
                return false;
            }
        }

        return true;
    }
}
