package com.akivaliaho;

import com.akivaliaho.config.annotations.Interest;
import org.springframework.stereotype.Component;

/**
 * Created by vagrant on 5/18/17.
 */
@Component
public class ConfigurationService {
	@Interest(value = ConfigurationPollEvent.class)
	public void sendInterestPoll() {

	}
}
