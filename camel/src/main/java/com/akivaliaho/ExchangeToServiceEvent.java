package com.akivaliaho;

import com.akivaliaho.event.EventInterestHolder;
import com.akivaliaho.event.InterestedParty;
import com.akivaliaho.event.ServiceEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * Created by akivv on 24.4.2017.
 */
@Slf4j
public class ExchangeToServiceEvent implements Processor {
	private final ProducerTemplate producerTemplate;
	private final EventInterestHolder eventInterestHolder;

	public ExchangeToServiceEvent(ProducerTemplate producerTemplate, EventInterestHolder eventInterestHolder) {
		this.producerTemplate = producerTemplate;
		this.eventInterestHolder = eventInterestHolder;
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		byte[] body = (byte[]) exchange.getIn().getBody();
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
		ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
		ServiceEvent serviceEvent = (ServiceEvent) objectInputStream.readObject();
		if (serviceEvent.getEventName().equals("declarationOfInterests")) {
			eventInterestHolder.registerInterests(serviceEvent);
		}
		//TODO Pickup the listeners of this particular event before sending it back to the broker
		List<InterestedParty> interestedParties = eventInterestHolder.getInterestedParties(serviceEvent);
		if (interestedParties != null) {
			interestedParties
					.parallelStream()
					.forEach(event -> {
						//Send all the events through the producer template
						exchange.getIn().setBody(serviceEvent);
						exchange.getIn().setHeader("routingKey", event.getRoutingKey());
						producerTemplate.send("direct:fromESB", exchange);
					});
		}
	}
}
