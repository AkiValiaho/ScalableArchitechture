package com.akivaliaho;

import java.util.List;
import java.util.Map;

/**
 * Created by akivv on 18.5.2017.
 */
public class ConfigurationPollEventResult extends ServiceEventResult {
    public ConfigurationPollEventResult(Map<ServiceEvent, List<String>> eventInterestMap) {
        saveEvent(ConfigurationPollEventResult.class.getName(), eventInterestMap);
    }
}
