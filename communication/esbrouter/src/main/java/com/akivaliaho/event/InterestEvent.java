package com.akivaliaho.event;

import java.util.List;

/**
 * Created by vagrant on 4/28/17.
 */
public class InterestEvent extends ServiceEvent {
	public InterestEvent(List<ServiceEvent> eventInterests, String serviceName) {
		saveEvent("declarationOfInterests", eventInterests, serviceName);
	}
}
