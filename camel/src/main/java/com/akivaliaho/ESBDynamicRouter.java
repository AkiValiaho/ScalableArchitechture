package com.akivaliaho;

import org.apache.camel.Exchange;

/**
 * Created by vagrant on 4/28/17.
 */
public class ESBDynamicRouter {
	public String routeExchange(Exchange exchange) {
		Boolean invoked = (Boolean) exchange.getProperty("invoked");
		if (invoked == null) {
			//Create the routing string dynamically from exchange headers
			String routingKey = (String) exchange.getIn().getHeader("routingKey");
			exchange.setProperty("invoked", true);
			return "rabbitmq://localhost:5672/" + routingKey + "&username=hello&password=world&exchangeType=fanout&autoDelete=false";
		}
		return null;
	}
}
