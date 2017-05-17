package com.akivaliaho;

import com.akivaliaho.config.annotations.Interest;
import org.springframework.stereotype.Component;

/**
 * Created by akivv on 9.5.2017.
 */
@Component
public class StringTool {


    @Interest(value = AppendStringsEvent.class)
    public AppendStringsEventResult stringAppend(String a, String b) {
        return new AppendStringsEventResult(a + b);
    }


}
