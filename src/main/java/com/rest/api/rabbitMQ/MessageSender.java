package com.rest.api.rabbitMQ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {

    private static final Logger log = LoggerFactory.getLogger(MessageSender.class);

    /**
     * @param rabbitTemplate
     * @param exchange
     * @param routingKey
     * @param data
     */

    public void sendMessage(RabbitTemplate rabbitTemplate, String exchange, String routingKey, Object data) {

        log.info("다음의 정보로 RabbitMQ 메세지를 보냄 : routingKey {}. Message= {}", routingKey, data);
        rabbitTemplate.convertAndSend(exchange, routingKey, data);
        log.info("The message has been sent to the queue.");
    }
}
