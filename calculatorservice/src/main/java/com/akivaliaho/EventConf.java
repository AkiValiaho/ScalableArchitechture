package com.akivaliaho;

import com.akivaliaho.config.annotations.Interest;
import com.akivaliaho.config.annotations.Interests;

/**
 * Created by vagrant on 4/28/17.
 */
@Interests(
		interests = {
				@Interest(value = "com.akivaliaho.service.events.CalculateSuperHardSumEvent")
		}
)
public class EventConf {
}
