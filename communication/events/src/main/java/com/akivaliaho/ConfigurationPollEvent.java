package com.akivaliaho;

/**
 * Created by vagrant on 5/18/17.
 */

public class ConfigurationPollEvent extends ServiceEvent {
	public ConfigurationPollEvent(String[] servicesToPoll) {
		saveEvent(ConfigurationPollEvent.class.getName(), servicesToPoll);
	}
}
