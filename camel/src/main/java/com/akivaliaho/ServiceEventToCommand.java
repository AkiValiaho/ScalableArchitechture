package com.akivaliaho;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;

import java.io.IOException;

/**
 * Created by vagrant on 6/25/17.
 */

public class ServiceEventToCommand {

    private final CommandDelegating commandDelegating;
    private CamelContext context;

    public ServiceEventToCommand(CommandDelegating commandDelegating) {
        this.commandDelegating = commandDelegating;
    }

    public void sendPollResultToConfigModule(Exchange exchange, ServiceEvent serviceEvent) {
        //Send an configHolderRoutingKey about every interest back to the configuration holder
        commandDelegating.sendPollResultToConfig(exchange);
    }

    public void sendInterestRequest() throws IOException {
        commandDelegating.sendInterestedPartiesRequest(context);
    }

    public void handleServiceEvent(Exchange exchange) throws IOException, ClassNotFoundException {
        commandDelegating.sendExchange(exchange);
    }


    public void setCamelContext(CamelContext camelContext) {
        this.context = camelContext;
    }
}
