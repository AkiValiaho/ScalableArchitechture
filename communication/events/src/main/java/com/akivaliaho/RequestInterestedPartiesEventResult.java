package com.akivaliaho;

import java.util.List;
import java.util.Map;

/**
 * Created by akivv on 8.6.2017.
 */
public class RequestInterestedPartiesEventResult extends ServiceEventResult {
    public RequestInterestedPartiesEventResult(Map<ServiceEvent, List<String>> eventInterestMap) {
        saveEvent(RequestInterestedPartiesEventResult.class.getName(), eventInterestMap);
    }
}
