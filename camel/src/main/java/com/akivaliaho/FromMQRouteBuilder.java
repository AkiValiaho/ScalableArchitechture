package com.akivaliaho;

import org.apache.camel.builder.RouteBuilder;

/**
 * A Camel Java DSL Router
 */
public class FromMQRouteBuilder extends RouteBuilder {

    /**
     * Let's configure the Camel routing rules using Java code...
     */
    public void configure() {

        from("rabbitmq://localhost/")
        // here is a sample which processes the input files
        // (leaving them in place - see the 'noop' flag)
        // then performs content based routing on the message using XPath
        from("file:src/data?cdnoop=true")
                .choice()
                .when(xpath("/person/city = 'London'"))
                .log("UK message")
                .to("file:target/messages/uk")
                .otherwise()
                .log("Other message")
                .to("file:target/messages/others");
    }

}
