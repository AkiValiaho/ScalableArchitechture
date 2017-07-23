package com.akivaliaho.amqp;

import com.akivaliaho.ServiceEvent;
import com.akivaliaho.ServiceEventDto;
import com.akivaliaho.config.ConfigEnum;
import com.akivaliaho.config.ConfigurationHolder;
import com.akivaliaho.event.InterestEvent;
import com.akivaliaho.event.LocalEventDelegator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.stream.Collectors;

/**
 * Created by akivv on 10.4.2017.
 */
@Component
@Slf4j
public class ESBRouter {
    private final ConfigurationHolder configurationHolder;
    private final LocalEventDelegator localEventDelegator;
    private final AmqpConfigurator amqpConfigurator;
    private String toEsbExchange;
    private RabbitTemplate template;

    @Autowired
    public ESBRouter(ConfigurationHolder configurationHolder, LocalEventDelegator localEventDelegator, AmqpConfigurator amqpConfigurator) {
        this.configurationHolder = configurationHolder;
        this.localEventDelegator = localEventDelegator;
        this.amqpConfigurator = amqpConfigurator;
    }

    @PostConstruct
    public void init() {
        amqpConfigurator.configureAmqp(this);
        this.toEsbExchange = amqpConfigurator.getMq_to_esb_exchange();
        this.template = amqpConfigurator.getTemplate();
        informAboutInterests();
    }

    public void informAboutInterests() {
        InterestEvent event = new InterestEvent(configurationHolder.getInterests()
                .stream()
                .map(s -> new ServiceEventDto("", null, null, null, s))
                .collect(Collectors.toList()), configurationHolder.getMessagingConfiguration().get(ConfigEnum.MQ_FROM_ESB_ESCHANGE));
        localEventDelegator.pluginInterests(configurationHolder.getInterestMap());
        routeEvent(new ServiceEvent(event));
    }


    public <V> V routeEvent(ServiceEvent event) {
        //Send message to an exchange which routes it to the camel esb
        log.info("Routing event to the master ESB");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(bos);
            objectOutputStream.writeObject(new ServiceEventDto(event.getOriginalEventName(), event.getOriginalParameters(), event.getId(),
                    event.getParameters(), event.getEventName()));
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setHeader("serviceName", amqpConfigurator.getServiceName());
            Message message = new Message(bos.toByteArray(), messageProperties);
            this.template.convertAndSend(toEsbExchange, "master", message);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
