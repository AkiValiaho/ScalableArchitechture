package com.akivaliaho.amqp;

import com.akivaliaho.config.ConfigEnum;
import com.akivaliaho.config.ConfigurationHolder;
import lombok.Getter;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by akivv on 18.6.2017.
 */
@Component
public class AmqpEntitiesConstructor {
    private final ConfigurationHolder configurationHolder;
    private final AmqpContainerInitialiser amqpContainerInitializer;
    private Map<String, String> messagingConfiguration;
    @Getter
    private FanoutExchange mq_from_esb_exchange;
    @Getter
    private TopicExchange mq_to_esb_exchange;
    private RabbitAdmin admin;
    private Queue serviceQueue;
    private Queue toESBQueue;
    private String serviceName;
    @Getter
    private RabbitTemplate template;

    @Autowired
    public AmqpEntitiesConstructor(ConfigurationHolder configurationHolder, AmqpContainerInitialiser amqpContainerInitializer) {
        this.configurationHolder = configurationHolder;
        this.amqpContainerInitializer = amqpContainerInitializer;
    }

    public void constructAmqpEntities() {
        messagingConfiguration = configurationHolder.getMessagingConfiguration();
        CachingConnectionFactory connectionFactory = createConnectionFactory();
        // set up the queue, exchange, binding on the broker
        declareComponents(connectionFactory);
        amqpContainerInitializer.initAmqpContainer(messagingConfiguration, admin, serviceQueue, toESBQueue, connectionFactory);
        this.template = new RabbitTemplate(connectionFactory);
    }

    private void declareComponents(CachingConnectionFactory connectionFactory) {
        admin = new RabbitAdmin(connectionFactory);
        createQueues();
        createExchanges();
        declareComponents();
        declareBindings();
    }

    private void declareBindings() {
        admin.declareBinding(
                BindingBuilder.bind(serviceQueue).to(mq_from_esb_exchange));
    }

    private void declareComponents() {
        admin.declareExchange(mq_from_esb_exchange);
        admin.declareExchange(mq_to_esb_exchange);
        admin.declareQueue(serviceQueue);
        admin.declareQueue(toESBQueue);
    }

    private void createExchanges() {
        mq_from_esb_exchange = new FanoutExchange(messagingConfiguration.get(ConfigEnum.MQ_FROM_ESB_ESCHANGE), true, false);
        mq_to_esb_exchange = new TopicExchange(messagingConfiguration.get(ConfigEnum.MQ_TO_ESB_EXCHANGE), true, false);
    }

    private void createQueues() {
        //Create dedicated serviceQueue and toESBQueue
        serviceQueue = new Queue(messagingConfiguration.get(ConfigEnum.SERVICEQUEUE), true);
        toESBQueue = new Queue(messagingConfiguration.get(ConfigEnum.MQ_TO_ESB_QUEUE), true);
    }

    private CachingConnectionFactory createConnectionFactory() {

        //Init connection to the master broker
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(messagingConfiguration.get("mq.address"), Integer.parseInt(messagingConfiguration.get("mq.port")));
        //Set username and password
        connectionFactory.setUsername(messagingConfiguration.get("mq.username"));
        connectionFactory.setPassword(messagingConfiguration.get("mq.password"));
        return connectionFactory;
    }

    public String getServiceName() {
        return amqpContainerInitializer.getServiceNameRoutingKey();
    }
}
