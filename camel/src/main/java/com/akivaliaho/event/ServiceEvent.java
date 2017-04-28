package com.akivaliaho.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by vagrant on 4/5/17.
 */
@NoArgsConstructor
public class ServiceEvent implements Serializable {
	@Getter
	@Setter
	private Long id;
	@Getter
	@Setter
	private String eventName;
	@Getter
	private Object[] parameters;

	public ServiceEvent(String eventName) {
		this.eventName = eventName;
	}

	public ServiceEvent(ServiceEvent event) {
		this.id = event.getId();
		this.eventName = event.getEventName();
		this.parameters = event.getParameters();
	}

	public void saveEvent(String eventName, Object... params) {
		this.eventName = eventName;
		this.parameters = params;
	}

	public EventParams getEventParams() {
		return new EventParams(eventName, parameters);
	}

	@AllArgsConstructor
	class EventParams {
		@Getter
		String eventName;
		@Getter
		Object[] params;
	}
}
