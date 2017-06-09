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
        ExchangeTools exchangeTools = new ExchangeTools();
        EventAndRoutingKeyHolder eventAndRoutingKeyHolder = new EventAndRoutingKeyHolder();
        ExchangeToServiceEvent exchangeToServiceEvent = new ExchangeToServiceEvent(new EventInterestRegistrer(eventAndRoutingKeyHolder), exchangeTools, new ProcessPreparator(new EventInterestRegistrer(eventAndRoutingKeyHolder), exchangeTools), main.getOrCreateCamelContext());
        FromMQRouteBuilder routeBuilder = new FromMQRouteBuilder(exchangeToServiceEvent, new EventNotifierRegisterer(new RoutesReadyEventNotifier(exchangeToServiceEvent)));
        main.addRouteBuilder(routeBuilder);
        main.run(args);
    }

}

