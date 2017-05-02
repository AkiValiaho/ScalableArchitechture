package com.akivaliaho.event;

import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vagrant on 5/2/17.
 */
@Component
public class AsyncQueue {
	List<AsyncResult<?>> asyncList = new ArrayList<>();

	public void addWaitingResult(AsyncResult<?> vAsyncResult) {
		this.asyncList.add(vAsyncResult);
	}
}
