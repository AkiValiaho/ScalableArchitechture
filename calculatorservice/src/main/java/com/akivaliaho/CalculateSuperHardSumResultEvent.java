package com.akivaliaho;

import com.akivaliaho.event.ServiceEvent;

/**
 * Created by akivv on 1.5.2017.
 */
public class CalculateSuperHardSumResultEvent extends ServiceEvent {

	public CalculateSuperHardSumResultEvent(int i) {
		saveEvent(this.getClass().getName(), i);
	}
}
