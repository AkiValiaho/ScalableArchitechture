package com.akivaliaho;


import com.akivaliaho.event.EventAndRoutingKeyHolder;
import com.google.common.annotations.VisibleForTesting;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by vagrant on 5/23/17.
 */
public class EventAndRoutingKeyHolderTest {

	private EventAndRoutingKeyHolder eventAndRoutingKeyHolder;

	@Before
	public void init() {
		this.eventAndRoutingKeyHolder = new EventAndRoutingKeyHolder();
	}

	@Test(expected = NullPointerException.class)
	public void testNullParameterHandleEvent() {
		this.eventAndRoutingKeyHolder.handleEvent(null, "");

	}

	@Test(expected = IllegalArgumentException.class)
	public void testRoutingKeyEmpty() {
		ServiceEvent serviceEvent = new ServiceEvent("HellTestEvent");
		this.eventAndRoutingKeyHolder.handleEvent(serviceEvent, "");
	}

	@Test
	public void testhandleEventeventPresent() {
		ServiceEvent serviceEvent = new ServiceEvent("presentEvent");
		HashMap<ServiceEvent, List<String>> eventInterestMap = this.eventAndRoutingKeyHolder.getEventInterestMap();
		List<String> eventRoutingKeys = new ArrayList<>();
		eventRoutingKeys.add("helloWorld");
		eventInterestMap.put(serviceEvent, eventRoutingKeys);

		this.eventAndRoutingKeyHolder.handleEvent(serviceEvent, "notPresentYet");
		//Should have been added to the event routing key
		String s = eventRoutingKeys.get(1);
		assertEquals(s, "notPresentYet");
	}

	@Test
	public void testHandleEventNotPresentYet() {
		ServiceEvent serviceEvent = new ServiceEvent("NotYetPresent");
		this.eventAndRoutingKeyHolder.handleEvent(serviceEvent, "shouldBePresentNow");
		HashMap<ServiceEvent, List<String>> eventInterestMap = this.eventAndRoutingKeyHolder.getEventInterestMap();
		List<String> strings = eventInterestMap.get(serviceEvent);
		//Should contain the shouldBePresent routing key now
		String s = strings.get(0);
		assertEquals(s, "shouldBePresentNow");

	}

}