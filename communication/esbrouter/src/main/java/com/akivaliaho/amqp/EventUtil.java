package com.akivaliaho.amqp;

import com.akivaliaho.event.ServiceEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by vagrant on 4/5/17.
 */
@Component
public class EventUtil {

    @Autowired
    ESBRouter esbRouter;

    public <V> V publishEvent(ServiceEvent event) {
        return esbRouter.routeEvent(new ServiceEvent(event));
    }
}
