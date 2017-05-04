package com.akivaliaho.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by vagrant on 4/5/17.
 */
@NoArgsConstructor
public class ServiceEventResult extends ServiceEvent {
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private String eventName;
    @Getter
    private Object[] parameters;
    @Getter
    @Setter
    private Object[] originalParameters;

    public ServiceEventResult(String eventName) {
        this.eventName = eventName;
    }

    public ServiceEventResult(ServiceEvent event) {
        this.id = event.getId();
        this.eventName = event.getEventName();
        this.parameters = event.getParameters();
    }

    @Override
    public int hashCode() {
        return 31 * eventName.hashCode();
    }

    public void saveEvent(String eventName, Object... params) {
        this.eventName = eventName;
        this.parameters = params;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ServiceEvent) {
            return ((ServiceEvent) obj).getEventName() == this.getEventName();
        }
        return false;
    }

    @AllArgsConstructor
    class EventParams {
        @Getter
        String eventName;
        @Getter
        Object[] params;
    }
}