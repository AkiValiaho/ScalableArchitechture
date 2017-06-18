package com.akivaliaho.amqp;

import com.akivaliaho.ConfigurationPollEventResult;
import com.akivaliaho.RequestInterestedPartiesEvent;
import com.akivaliaho.ServiceEvent;
import com.akivaliaho.ServiceEventResult;
import com.akivaliaho.config.ConfigEnum;
import com.akivaliaho.config.ConfigurationHolder;
import com.akivaliaho.event.AsyncQueue;
import com.akivaliaho.event.LocalEventDelegator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

/**
 * Created by akivv on 18.6.2017.
 */
@Component
@Slf4j
public class AmqpConfigurator {
    private final LocalEventDelegator localEventDelegator;
    private final AsyncQueue asyncQueue;
    private final ConfigurationHolder configurationHolder;
    @Getter
    private RabbitTemplate template;
    private FanoutExchange mq_from_esb_exchange;
    private TopicExchange mq_to_esb_exchange;
    @Getter
    private String serviceName;
    @Getter
    private String routingKey;
    @Autowired
    public AmqpConfigurator(LocalEventDelegator localEventDelegator, AsyncQueue asyncQueue, ConfigurationHolder configurationHolder) {
        this.localEventDelegator = localEventDelegator;
        this.asyncQueue = asyncQueue;
        this.configurationHolder = configurationHolder;
    }

    public String getMq_to_esb_exchange() {
        return mq_to_esb_exchange.getName();
    }

    private void declareBindings(RabbitAdmin admin, Queue service_queue, FanoutExchange mq_from_esb_exchange) {
        //TODO Declarations should be part of another composed class, this is a router after all
        admin.declareBinding(
                BindingBuilder.bind(service_queue).to(mq_from_esb_exchange));
    }

    private void declare(RabbitAdmin admin, Queue fromBrokerQueue, Queue toBrokerQueue, FanoutExchange masterBrokerIncomingExchange, TopicExchange masterBrokerOutboundExchange) {
        admin.declareExchange(masterBrokerIncomingExchange);
        admin.declareExchange(masterBrokerOutboundExchange);
        admin.declareQueue(toBrokerQueue);
        admin.declareQueue(fromBrokerQueue);
    }

    private void messageContainerInit(CachingConnectionFactory connectionFactory, Queue serviceQueue) {
        SimpleMessageListenerContainer container =
                new SimpleMessageListenerContainer(connectionFactory);
        Object listener = new Object() {
            public void handleMessage(byte[] bytes) throws InstantiationException {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                    ServiceEvent serviceEvent = (ServiceEvent) objectInputStream.readObject();
                    handleMessage(serviceEvent);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            public void handleMessage(ServiceEvent foo) throws InstantiationException {
                log.info("Got message: {}", foo.getEventName());
                //Is it a ConfigurationPollResult?
                if (foo instanceof ConfigurationPollEventResult) {
                    localEventDelegator.delegateEvent(foo);
                    return;
                }
                //Is it a request for the interested parties?
                if (foo instanceof RequestInterestedPartiesEvent) {
                    localEventDelegator.delegateEvent(foo);
                    return;
                }
                //Is it a Result event?
                if (foo.getEventName().toLowerCase().contains("result")) {
                    asyncQueue.solveResult(((ServiceEventResult) foo));
                } else {
                    localEventDelegator.delegateEvent(foo);
                }
            }

            public void handleMessage(Object object) {
                //Something is wrong because this is not an event
                log.error("Message received is malformed: {}", object);
            }
        };
        MessageListenerAdapter adapter = new MessageListenerAdapter(listener);
        container.setMessageListener(adapter);
        container.setQueueNames(serviceQueue.getName());
        container.start();
    }

    private void bindingsInit(Map<String, String> messagingConfiguration, RabbitAdmin admin, Queue serviceQueue, Queue toESBQueue) {
        //TODO Bindings should be part of another AMQP-related configuration class
        declare(admin, serviceQueue, toESBQueue, mq_from_esb_exchange, mq_to_esb_exchange);
        routingKey = messagingConfiguration.get(ConfigEnum
                .SERVICEBASENAME);
        //Bind the incoming exchange to incoming queue
        declareBindings(admin, serviceQueue, mq_from_esb_exchange);
    }

    public void configureAmqp(ESBRouter esbRouter) {
        //TODO This initializer is too damn long and hard to read (not going to even try), do something about it.
        Map<String, String> messagingConfiguration = configurationHolder.getMessagingConfiguration();
        //Init connection to the master broker
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(messagingConfiguration.get("mq.address"), Integer.parseInt(messagingConfiguration.get("mq.port")));
        //Set username and password
        connectionFactory.setUsername(messagingConfiguration.get("mq.username"));
        connectionFactory.setPassword(messagingConfiguration.get("mq.password"));
        // set up the queue, exchange, binding on the broker
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        //Declare dedicated serviceQueue and toESBQueue
        Queue serviceQueue = new Queue(messagingConfiguration.get(ConfigEnum.SERVICEQUEUE), true);
        admin.declareQueue(serviceQueue);
        Queue toESBQueue = new Queue(messagingConfiguration.get(ConfigEnum.MQ_TO_ESB_QUEUE), true);
        mq_from_esb_exchange = new FanoutExchange(messagingConfiguration.get(ConfigEnum.MQ_FROM_ESB_ESCHANGE), true, false);
        mq_to_esb_exchange = new TopicExchange(messagingConfiguration.get(ConfigEnum.MQ_TO_ESB_EXCHANGE), true, false);
        bindingsInit(messagingConfiguration, admin, serviceQueue, toESBQueue);
        messageContainerInit(connectionFactory, serviceQueue);
        this.serviceName = messagingConfiguration.get("mq.servicebasename");
        // send something
        this.template = new RabbitTemplate(connectionFactory);

        //Initialize localEventDelegatorlocalEventDelegator
        EventUtil eventUtil = new EventUtil(esbRouter, asyncQueue);
        localEventDelegator.setEventUtil(eventUtil);
    }
}
