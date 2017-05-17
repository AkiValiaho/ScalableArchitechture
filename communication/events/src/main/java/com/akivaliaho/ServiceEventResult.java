package com.akivaliaho;

/**
 * Created by vagrant on 4/5/17.
 */
public class ServiceEventResult extends ServiceEvent {
    private Long id;
    private String eventName;
    private Object[] parameters;
    private Object[] originalParameters;
    private String originalEventName;

    public ServiceEventResult(String eventName) {
        this.eventName = eventName;
    }

    public ServiceEventResult(ServiceEvent event) {
        this.id = event.getId();
        this.eventName = event.getEventName();
        this.parameters = event.getParameters();
    }

    public ServiceEventResult() {
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventName() {
        return this.eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Object[] getParameters() {
        return this.parameters;
    }

    public Object[] getOriginalParameters() {
        return this.originalParameters;
    }

    public void setOriginalParameters(Object[] originalParameters) {
        this.originalParameters = originalParameters;
    }

    public String getOriginalEventName() {
        return this.originalEventName;
    }

    public void setOriginalEventName(String originalEventName) {
        this.originalEventName = originalEventName;
    }

    class EventParams {
        String eventName;
        Object[] params;

        @java.beans.ConstructorProperties({"eventName", "params"})
        public EventParams(String eventName, Object[] params) {
            this.eventName = eventName;
            this.params = params;
        }

        public String getEventName() {
            return this.eventName;
        }

        public Object[] getParams() {
            return this.params;
        }
    }
}