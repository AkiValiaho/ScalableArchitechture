package com.akivaliaho.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vagrant on 5/2/17.
 */
@Component
@Slf4j
public class AsyncQueue {
	List<DeferredResult<?>> asyncList = new ArrayList<>();

	public void addWaitingResult(DeferredResult<?> vAsyncResult) {
		this.asyncList.add(vAsyncResult);
	}

	public void solveResult(ServiceEvent foo) {
		log.debug("Got ServiceEvent: {}", foo);
	}

}
