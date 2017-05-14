package com.akivaliaho;

import com.akivaliaho.event.ServiceEventResult;

/**
 * Created by akivv on 9.5.2017.
 */
public class AppendStringsEventResult extends ServiceEventResult {
    public AppendStringsEventResult(String appended) {
        saveEvent(this.getClass().getName(), appended);
    }
}
