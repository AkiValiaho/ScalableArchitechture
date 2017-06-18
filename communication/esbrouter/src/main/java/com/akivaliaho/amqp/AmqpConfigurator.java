package com.akivaliaho.amqp;

import com.akivaliaho.event.AsyncQueue;
import com.akivaliaho.event.LocalEventDelegator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by akivv on 18.6.2017.
 */
@Component
@Slf4j
public class AmqpConfigurator {
    private final LocalEventDelegator localEventDelegator;
    private final AsyncQueue asyncQueue;
    private final AmqpEntitiesConstructor amqpEntitiesConstructor;
    @Getter
    private RabbitTemplate template;
    @Getter
    private String serviceName;

    @Autowired
    public AmqpConfigurator(LocalEventDelegator localEventDelegator, AsyncQueue asyncQueue, AmqpEntitiesConstructor amqpEntitiesConstructor) {
        this.localEventDelegator = localEventDelegator;
        this.asyncQueue = asyncQueue;
        this.amqpEntitiesConstructor = amqpEntitiesConstructor;
    }

    public String getMq_to_esb_exchange() {
        return amqpEntitiesConstructor.getMq_to_esb_exchange().getName();
    }


    public void configureAmqp(ESBRouter esbRouter) {
        amqpEntitiesConstructor.constructAmqpEntities();
        this.serviceName = amqpEntitiesConstructor.getServiceName();
        // send something
        this.template = amqpEntitiesConstructor.getTemplate();
        //Initialize localEventDelegatorlocalEventDelegator
        EventUtil eventUtil = new EventUtil(esbRouter, asyncQueue);
        localEventDelegator.setEventUtil(eventUtil);
    }
}
