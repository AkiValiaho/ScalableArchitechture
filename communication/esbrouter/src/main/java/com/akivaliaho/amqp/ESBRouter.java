package com.akivaliaho.amqp;

import com.akivaliaho.ConfigurationPollEventResult;
import com.akivaliaho.ServiceEvent;
import com.akivaliaho.ServiceEventResult;
import com.akivaliaho.config.ConfigEnum;
import com.akivaliaho.config.ConfigurationHolder;
import com.akivaliaho.event.AsyncQueue;
import com.akivaliaho.event.InterestEvent;
import com.akivaliaho.event.LocalEventDelegator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by akivv on 10.4.2017.
 */
@Component
@Slf4j
public class ESBRouter {
    private final ConfigurationHolder configurationHolder;
    private final LocalEventDelegator localEventDelegator;
    private final AsyncQueue asyncQueue;
    @Autowired
    ApplicationContext applicationContext;
    private String serviceName;
    private RabbitTemplate template;
    private String routingKey;
    private FanoutExchange mq_from_esb_exchange;
    private TopicExchange mq_to_esb_exchange;

    @Autowired
    public ESBRouter(ConfigurationHolder configurationHolder, LocalEventDelegator localEventDelegator, AsyncQueue asyncQueue) {
        this.configurationHolder = configurationHolder;
        this.localEventDelegator = localEventDelegator;
        this.asyncQueue = asyncQueue;
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
        EventUtil eventUtil = new EventUtil(this, asyncQueue);
        localEventDelegator.setEventUtil(eventUtil);
        informAboutInterests();
    }

    public void informAboutInterests() {
        InterestEvent event = new InterestEvent(configurationHolder.getInterests()
                .stream()
                .map(s -> new ServiceEvent(s))
                .collect(Collectors.toList()), configurationHolder.getMessagingConfiguration().get(ConfigEnum.MQ_FROM_ESB_ESCHANGE));
        localEventDelegator.pluginInterests(configurationHolder.getInterestMap());
        routeEvent(new ServiceEvent(event));
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
            public void handleMessage(byte[] bytes) {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                    ServiceEvent serviceEvent = (ServiceEvent) objectInputStream.readObject();
                    handleMessage(serviceEvent);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            public void handleMessage(ServiceEvent foo) {
                log.info("Got message: {}", foo.getEventName());
                //Is it a ConfigurationPollResult?
                if (foo instanceof ConfigurationPollEventResult) {
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

    private void declareBindings(RabbitAdmin admin, Queue service_queue, FanoutExchange mq_from_esb_exchange) {
        admin.declareBinding(
                BindingBuilder.bind(service_queue).to(mq_from_esb_exchange));
    }

    private void declare(RabbitAdmin admin, Queue fromBrokerQueue, Queue toBrokerQueue, FanoutExchange masterBrokerIncomingExchange, TopicExchange masterBrokerOutboundExchange) {
        admin.declareExchange(masterBrokerIncomingExchange);
        admin.declareExchange(masterBrokerOutboundExchange);
        admin.declareQueue(toBrokerQueue);
        admin.declareQueue(fromBrokerQueue);
    }

    public <V> V routeEvent(ServiceEvent event) {
        //Send message to an exchange which routes it to the camel esb
        log.info("Routing event to the master ESB");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(bos);
            objectOutputStream.writeObject(event);
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setHeader("serviceName", serviceName);
            Message message = new Message(bos.toByteArray(), messageProperties);
            this.template.convertAndSend(mq_to_esb_exchange.getName(), "master", message);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
