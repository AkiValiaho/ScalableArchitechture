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
        DefaultExchangeTools defaultExchangeTools = new DefaultExchangeTools(new ExchangePropertyStrategyFactory().createStrategy(ExchangePropertyStrategyFactory.ExchangePropertyStrategy.DEFAULT_EXCHANGE_PROPERTIES), new ByteTools());


        EventAndRoutingKeyHolder eventAndRoutingKeyHolder = new EventAndRoutingKeyHolder();


        ExchangeToServiceEvent exchangeToServiceEvent = new ExchangeToServiceEvent(new EventInterestRegistrer(eventAndRoutingKeyHolder), defaultExchangeTools, new ProcessPreparator(new EventInterestRegistrer(eventAndRoutingKeyHolder), defaultExchangeTools), main.getOrCreateCamelContext());


        FromMQRouteBuilder routeBuilder = new FromMQRouteBuilder(exchangeToServiceEvent, new EventNotifierRegisterer(new RoutesReadyEventNotifier(exchangeToServiceEvent)));

        main.addRouteBuilder(routeBuilder);
        main.run(args);
    }

}

