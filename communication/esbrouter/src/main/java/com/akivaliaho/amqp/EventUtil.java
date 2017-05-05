package com.akivaliaho.amqp;

import com.akivaliaho.event.AsyncQueue;
import com.akivaliaho.event.ServiceEvent;
import com.akivaliaho.event.ServiceEventResult;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Created by vagrant on 4/5/17.
 */
@Component
public class EventUtil {

    private final ESBRouter esbRouter;
    private final AsyncQueue asyncQueue;

    public EventUtil(ESBRouter esbRouter, AsyncQueue asyncQueue) {
        this.esbRouter = esbRouter;
        this.asyncQueue = asyncQueue;
    }

    public <V> AsyncResult<V> publishEvent(ServiceEvent event, DeferredResult<Integer> gatewayDeferredResult) {
        AsyncResult<V> vAsyncResult = new AsyncResult<>(esbRouter.routeEvent(new ServiceEvent(event)));
        asyncQueue.addWaitingResult(gatewayDeferredResult, event.getParameters(), event.getEventName());
        return vAsyncResult;
    }

    public void publishEvent(ServiceEvent invoke) {
        esbRouter.routeEvent(new ServiceEvent(invoke));
    }

    public void publishEventResult(ServiceEventResult invoke) {
        esbRouter.routeEvent(new ServiceEvent(invoke));
    }
}
