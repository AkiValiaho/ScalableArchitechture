package com.akivaliaho;

import com.akivaliaho.event.EventInterestRegistrer;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.model.ModelCamelContext;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by akivv on 24.4.2017.
 */
@Slf4j
public class ExchangeToServiceEvent implements Processor {
    private final EventInterestRegistrer eventInterestRegistrer;
    private final ProcessPreparator processPreparator;
    private final ExchangeDestinationAwareFactory exchangeDestionationAwareFactory;
    private CamelContext context;
    private String configHolderRoutingKey = Addresses.CONFIGURATIONSERVICE_DEFAULT.getValue();
    private ModelCamelContext camelContext;

    public ExchangeToServiceEvent(EventInterestRegistrer eventInterestRegistrer ,ExchangeDestinationAwareFactory exchangeDestinationAwareFactory,ProcessPreparator processPreparator, CamelContext context) {
        this.eventInterestRegistrer = eventInterestRegistrer;
        this.processPreparator = processPreparator;
        this.context = context;
        this.exchangeDestionationAwareFactory = exchangeDestinationAwareFactory;

    }

    public void sendInterestRequest() {
        try {
            exchangeDestionationAwareFactory.createExchangeSendingOperation(ExchangeOperation.REQUEST_INTERESTED_PARTIES_FROM_CONFIG)
                                            .sendExchange(null, configHolderRoutingKey, context);
            defaultExchangeTools.requestInterestedParties(configHolderRoutingKey, context);
        } catch (IOException e) {
            //TODO Handle exception
            e.printStackTrace();
        }
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        PreProcessData preprocessData = processPreparator
                //TODO These steps are not necessary I think, simplify them to a single call
                .feedExchange(exchange)
                .invoke()
                .getPreprocessData();
        if (!handleDeclarationOfInterest(exchange, preprocessData.getServiceEvent())) {
            exchangeDestionationAwareFactory.createExchangeSendingOperation(ExchangeOperation.INTERESTED_PARTIES)
                                            .sendExchange(exchange,
                                                    preprocessData.getInterestedParties(),
                                                    preprocessData.getServiceEvent(),
                                                    preprocessData.getServiceEventResult());
        }
    }

    private boolean handleDeclarationOfInterest(Exchange exchange, ServiceEvent serviceEvent) {
        if (serviceEvent.getEventName().equals("declarationOfInterests")) {
            registerInterests(exchange, serviceEvent);
            return true;
        }
        if (serviceEvent.getEventName().equals("com.akivaliaho.RequestInterestedPartiesEventResult")) {
            registerOnInitInterestResult(exchange, serviceEvent);
            return true;
        }
        return false;
    }

    private void registerOnInitInterestResult(Exchange exchange, ServiceEvent serviceEvent) {
        eventInterestRegistrer.registerPollInterestResults(serviceEvent);
    }

    private void registerInterests(Exchange exchange, ServiceEvent serviceEvent) {
        ServiceEvent o = (ServiceEvent) ((ArrayList) serviceEvent.getParameters()[0]).get(0);
        eventInterestRegistrer.registerInterests(serviceEvent);
<<<<<<< Updated upstream
        //TODO Brittle logic here, better refactor this whole Interest-business with Strategy pattern to avoid unnecessary complexity
        if (o.getEventName().equals("com.akivaliaho.ConfigurationPollEventResult")) {
            sendPollResultToConfigModule(exchange, serviceEvent);
        } else if (configHolderRoutingKey != null && !configHolderRoutingKey.isEmpty()) {
            exchangeTools.sendPollResult(eventInterestRegistrer.getEventInterestMap(), exchange, configHolderRoutingKey);
        }
    }

    private void sendPollResultToConfigModule(Exchange exchange, ServiceEvent serviceEvent) {
        //Send an configHolderRoutingKey about every interest back to the configuration holder
        configHolderRoutingKey = (String) serviceEvent.getParameters()[1];
        //TODO Rethink this a bit, transitive access of a class, not good design
        exchangeTools.sendPollResult(eventInterestRegistrer.getEventInterestMap(), exchange, configHolderRoutingKey);
=======
        exchangeDestionationAwareFactory.createExchangeSendingOperation(ExchangeOperation.POLL_RESULT_TO_CONFIG_MODULE)
                                        .sendExchange(exchange, );
>>>>>>> Stashed changes
    }

    public void setCamelContext(CamelContext camelContext) {
        this.context = camelContext;
    }
}
