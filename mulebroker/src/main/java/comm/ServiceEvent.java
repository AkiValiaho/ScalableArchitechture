package comm;

import java.io.Serializable;

/**
 * Created by vagrant on 4/5/17.
 */
public class ServiceEvent implements Serializable {
    private Long id;
    private String eventName;
    private Object[] parameters;

    public ServiceEvent() {

    }

    public ServiceEvent(ServiceEvent event) {
        this.id = event.getId();
        this.eventName = event.getEventName();
        this.parameters = event.getParameters();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public void saveEvent(String eventName, Object... params) {
        this.eventName = eventName;
        this.parameters = params;
    }

    public EventParams getEventParams() {
        return new EventParams(eventName, parameters);
    }

    class EventParams {
        String eventName;
        Object[] params;

        public EventParams(String eventName, Object[] params) {
            this.eventName = eventName;
            this.params = params;
        }

        public String getEventName() {
            return eventName;
        }

        public void setEventName(String eventName) {
            this.eventName = eventName;
        }

        public Object[] getParams() {
            return params;
        }

        public void setParams(Object[] params) {
            this.params = params;
        }
    }
}
