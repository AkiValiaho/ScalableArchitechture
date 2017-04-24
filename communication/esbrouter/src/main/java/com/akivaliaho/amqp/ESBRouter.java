package com.akivaliaho.amqp;

import com.akivaliaho.config.ConfigEnum;
import com.akivaliaho.config.ConfigurationHolder;
import com.akivaliaho.event.LocalEventDelegator;
import com.akivaliaho.event.ServiceEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Created by akivv on 10.4.2017.
 */
@Component
@Slf4j
public class ESBRouter {
    private final ConfigurationHolder configurationHolder;
    private final LocalEventDelegator localEventDelegator;
    private String serviceName;
    private RabbitTemplate template;
    private String routingKey;
    private TopicExchange mq_from_esb_exchange;
    private TopicExchange mq_to_esb_exchange;

    @Autowired
    public ESBRouter(ConfigurationHolder configurationHolder, LocalEventDelegator localEventDelegator) {
        this.configurationHolder = configurationHolder;
        this.localEventDelegator = localEventDelegator;
    }

    @PostConstruct
    public void init() {
        Map<String, String> messagingConfiguration = configurationHolder.getMessagingConfiguration();
        //Init connection to the master broker
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(messagingConfiguration.get("mq.address"), Integer.parseInt(messagingConfiguration.get("mq.port")));
        //Set username and password
        connectionFactory.setUsername(messagingConfiguration.get("mq.username"));
        connectionFactory.setPassword(messagingConfiguration.get("mq.password"));
        // set up the queue, exchange, binding on the broker
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        //Declare dedicated serviceQueue and toESBQueue
        Queue serviceQueue = new Queue(messagingConfiguration.get(ConfigEnum.SERVICEQUEUE));
        admin.declareQueue(serviceQueue);
        Queue toESBQueue = new Queue(messagingConfiguration.get(ConfigEnum.MQ_TO_ESB_QUEUE));
        mq_from_esb_exchange = new TopicExchange(messagingConfiguration.get(ConfigEnum.MQ_FROM_ESB_ESCHANGE), true, false);
        mq_to_esb_exchange = new TopicExchange(messagingConfiguration.get(ConfigEnum.MQ_TO_ESB_EXCHANGE), true, false);
        bindingsInit(messagingConfiguration, admin, serviceQueue, toESBQueue);
        messageContainerInit(connectionFactory, serviceQueue);
        // send something
        this.template = new RabbitTemplate(connectionFactory);
        informMuleAboutRoutingKey(routingKey);
    }

    private void informMuleAboutRoutingKey(String routingKey) {
        //TODO Inform Mule about routing key
    }

    private void bindingsInit(Map<String, String> messagingConfiguration, RabbitAdmin admin, Queue serviceQueue, Queue toESBQueue) {
        declare(admin, serviceQueue, toESBQueue, mq_from_esb_exchange, mq_to_esb_exchange);
        routingKey = messagingConfiguration.get(ConfigEnum
                .SERVICEBASENAME);
        //Bind the incoming exchange to incoming queue
        declareBindings(admin, serviceQueue, mq_from_esb_exchange);
    }

    private void messageContainerInit(CachingConnectionFactory connectionFactory, Queue serviceQueue) {
        SimpleMessageListenerContainer container =
                new SimpleMessageListenerContainer(connectionFactory);
        Object listener = new Object() {
            public void handleMessage(ServiceEvent foo) {
                localEventDelegator.delegateEvent(foo);
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

    private void declareBindings(RabbitAdmin admin, Queue service_queue, TopicExchange mq_from_esb_exchange) {
        admin.declareBinding(
                BindingBuilder.bind(service_queue).to(mq_from_esb_exchange).with(routingKey));
    }

    private void declare(RabbitAdmin admin, Queue fromBrokerQueue, Queue toBrokerQueue, TopicExchange masterBrokerIncomingExchange, TopicExchange masterBrokerOutboundExchange) {
        admin.declareExchange(masterBrokerIncomingExchange);
        admin.declareExchange(masterBrokerOutboundExchange);
        admin.declareQueue(toBrokerQueue);
        admin.declareQueue(fromBrokerQueue);
    }

    public <V> V routeEvent(ServiceEvent event) {
        //Send message to an exchange which routes it to the camel esb
        log.info("Routing event to ESB");
        this.template.convertAndSend(mq_from_esb_exchange.getName(), routingKey, event);
        return null;
    }
}
