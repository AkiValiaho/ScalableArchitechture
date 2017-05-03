package com.akivaliaho.event;

import lombok.extern.slf4j.Slf4j;
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
	List<DeferredResult<Object>> asyncList = new ArrayList<>();

	public void addWaitingResult(DeferredResult<?> vAsyncResult) {
		this.asyncList.add((DeferredResult<Object>) vAsyncResult);
	}

	public void solveResult(ServiceEventResult foo) {
		Object o = foo.getParameters()[0];
		asyncList.stream()
				.forEach(result -> result.setResult(o));
		log.debug("Got ServiceEvent: {}", foo);
		asyncList.clear();
	}

}
