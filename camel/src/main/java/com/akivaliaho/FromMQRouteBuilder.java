package com.akivaliaho;

import com.akivaliaho.event.EventInterestHolder;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;

/**
 * A Camel Java DSL Router
 */
public class FromMQRouteBuilder extends RouteBuilder {

	private final ExchangeToServiceEvent exchangeToServiceEvent;

	public FromMQRouteBuilder() {
		ProducerTemplate producerTemplate = getContext().createProducerTemplate();
		EventInterestHolder eventInterestHolder = new EventInterestHolder();
		this.exchangeToServiceEvent = new ExchangeToServiceEvent(producerTemplate, eventInterestHolder);
	}

	/**
	 * Let's configure the Camel routing rules using Java code...
	 */
	public void configure() {
		from("rabbitmq://localhost:5672/toBrokerExchange?routingKey=master&username=hello&password=world&exchangeType=topic&autoDelete=false")
				.process(exchangeToServiceEvent);
		//TODO Check if this works properly
		from("direct:fromESB")
				.to("rabbitmq://localhost:5672/fromBrokerExchange?routingKey=${in.headers.routingKey}&username=hello&password=world&exchangeType=topic&autoDelete=false");
	}


}
