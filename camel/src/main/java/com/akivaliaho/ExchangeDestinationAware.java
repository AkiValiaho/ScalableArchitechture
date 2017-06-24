package com.akivaliaho;

import org.apache.camel.Exchange;

/**
 * Created by vagrant on 6/24/17.
 */
public interface ExchangeDestinationAware<E, A, T> {
    public void sendExchange(Exchange exchange, E e,A a, T t);
}
