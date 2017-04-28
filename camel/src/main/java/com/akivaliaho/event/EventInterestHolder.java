package com.akivaliaho.event;

import java.util.HashMap;
import java.util.List;

/**
 * Created by vagrant on 4/28/17.
 */
public class EventInterestHolder {
	private final HashMap<String, List<ServiceEvent>> eventInterestMap;

	public EventInterestHolder() {
		this.eventInterestMap = new HashMap<>();
	}

	public List<InterestedParty> getInterestedParties(ServiceEvent serviceEvent) {
		return null;
	}

	public void registerInterests(ServiceEvent serviceEvent) {
		this.eventInterestMap.put((String) serviceEvent.getEventParams().getParams()[1], (List<ServiceEvent>) serviceEvent.getParameters()[0]);
	}
}
