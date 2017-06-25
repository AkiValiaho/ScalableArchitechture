package com.akivaliaho;

import com.akivaliaho.event.EventAndRoutingKeyHolder;
import com.akivaliaho.event.EventInterestRegistrer;
import org.apache.camel.main.Main;

/**
 * A Camel Application
 */
public class MainApp {

    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String... args) throws Exception {
        Main main = new Main();
        EventAndRoutingKeyHolder eventAndRoutingKeyHolder = new EventAndRoutingKeyHolder();
        EventInterestRegistrer eventInterestRegistrer = new EventInterestRegistrer(eventAndRoutingKeyHolder);
        InterestDeclarationHandler interestDeclarationHandler = new InterestDeclarationHandler(eventInterestRegistrer);
        InterestFlowHandling interestFlowHandling = new InterestFlowHandling(interestDeclarationHandler);
        ExchangeTools defaultExchangeTools = new ExchangeTools(new ExchangePropertyStrategyFactory().createStrategy(ExchangePropertyStrategyFactory.ExchangePropertyStrategy.DEFAULT_EXCHANGE_PROPERTIES), new ByteTools());
        ExchangeParser exchangeParser = new ExchangeParser(defaultExchangeTools);
        AmqpSending amqpSending = new AmqpSendingImpl(defaultExchangeTools, exchangeParser);
        ServiceEventSending serviceEventSending = new ServiceEventSendingImpl(eventInterestRegistrer, amqpSending, interestFlowHandling);
        ExchangeConverting exchangeConverting = new ExchangeConvertingImpl(exchangeParser);
        CommandDelegating commandDelegating = new CommandDelegatingImpl(exchangeConverting, serviceEventSending);
        ServiceEventToCommand serviceEventToCommand = new ServiceEventToCommand(commandDelegating);

        ExchangeToServiceEvent exchangeToServiceEvent = new ExchangeToServiceEvent(serviceEventToCommand);
        FromMQRouteBuilder routeBuilder = new FromMQRouteBuilder(exchangeToServiceEvent, new EventNotifierRegisterer(new RoutesReadyEventNotifier(exchangeToServiceEvent)));

        main.addRouteBuilder(routeBuilder);
        main.run(args);
    }

}

