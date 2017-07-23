package com.akivaliaho;

import com.akivaliaho.services.application.AmqpConnection;
import com.akivaliaho.services.application.ExchangeService;
import com.akivaliaho.services.application.FromMQRouteBuilder;
import com.akivaliaho.services.domain.ServiceEventSendingTemplate;
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
        AmqpConnection amqpConnection = new AmqpConnection();
        ByteTools byteTools = new ByteTools();
        ServiceEventSendingTemplate serviceEventSendingTemplate = new ServiceEventSendingTemplate(amqpConnection);
        ExchangeService exchangeService = new ExchangeService(serviceEventSendingTemplate, byteTools);
        FromMQRouteBuilder routeBuilder = new FromMQRouteBuilder(exchangeService);
        main.addRouteBuilder(routeBuilder);
        main.run(args);
    }

}

