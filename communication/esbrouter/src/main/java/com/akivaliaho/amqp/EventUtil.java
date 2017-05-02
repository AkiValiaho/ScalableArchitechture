package com.akivaliaho.amqp;

import com.akivaliaho.event.AsyncQueue;
import com.akivaliaho.event.ServiceEvent;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

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

	public AsyncResult<?> publishEvent(ServiceEvent event) {
		AsyncResult<?> vAsyncResult = new AsyncResult<>(esbRouter.routeEvent(new ServiceEvent(event)));
		asyncQueue.addWaitingResult(vAsyncResult);
		return vAsyncResult;
	}
}
