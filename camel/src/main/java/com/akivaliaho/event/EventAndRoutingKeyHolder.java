package com.akivaliaho.event;

import com.akivaliaho.ServiceEvent;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by akivv on 22.5.2017.
 */
public class EventAndRoutingKeyHolder {
	@Getter
	@Setter
	private HashMap<ServiceEvent, List<String>> eventInterestMap;

	public EventAndRoutingKeyHolder() {
		this.eventInterestMap = new HashMap<>();
	}

	public void handleEvent(ServiceEvent event, String serviceRoutingKey) {
		checkNasties(event, serviceRoutingKey);
		//TODO Test the functionality of this class and check the parameters for
		//nasties.
		if (eventInterestMap.containsKey(event)) {
			addRoutingkeyToExistingEvent(serviceRoutingKey, event);
		} else {
			createNewEventAndRoutingKey(serviceRoutingKey, event);
		}
	}

	private void checkNasties(ServiceEvent event, String serviceRoutingKey) {
		checkNotNull(event);
		checkArgument(!Strings.isNullOrEmpty(serviceRoutingKey));
	}

	private void createNewEventAndRoutingKey(String serviceRoutingKey, ServiceEvent event) {
		//TODO Test these
		ArrayList<String> strings = new ArrayList<>();
		strings.add(serviceRoutingKey);
		eventInterestMap.put(event, strings);
	}

	private void addRoutingkeyToExistingEvent(String serviceRoutingKey, ServiceEvent event) {
		//TODO Test these
		List<String> strings = eventInterestMap.get(event);
		if (!strings.contains(serviceRoutingKey)) {
			strings.add(serviceRoutingKey);
		}
	}

	public List<String> get(ServiceEvent serviceEvent) {
		return eventInterestMap.get(serviceEvent);
	}
}
