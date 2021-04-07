package com.rest.api.rabbitMQ;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/test")
public class RabbitTestController {

    private static final Logger log = LoggerFactory.getLogger(MessageListener.class);

    private final RabbitTemplate rabbitTemplate;
    private RabbitConfigReader rabbitConfig;
    private MessageSender messageSender;

    public RabbitTestController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Autowired
    public void setApplicationConfig(RabbitConfigReader rabbitConfig) {
        this.rabbitConfig = rabbitConfig;
    }

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public MessageSender getMessageSender() {
        return messageSender;
    }

    public RabbitConfigReader getApplicationConfig() {
        return rabbitConfig;
    }

    @PostMapping(path = "/add")
    public ResponseEntity<?> sendMessage(@RequestParam("userMsg") String userMsg) throws InterruptedException {

        for (int i = 0; i < 1000; i ++){

            // 라우팅키는 나중에 따로 받아서 각 키별로 보낼 수 있다. 여기서는 고정.
            String routingKey = getApplicationConfig().getTest1AppRoutingKey();
            String exchange = getApplicationConfig().getTest1AppExchange();

            UserDetails user = new UserDetails();
            user.setSupplierId("UserSender");
            user.setSupplierName(userMsg + i);
            user.setSupplierUrl("www.google.com");
            messageSender.sendMessage(rabbitTemplate, exchange, routingKey, user);

        }

        Thread.sleep(3000);


        for (int i = 0; i < 1000; i ++){

            // 라우팅키는 나중에 따로 받아서 각 키별로 보낼 수 있다. 여기서는 고정.
            String routingKey = getApplicationConfig().getTest2AppRoutingKey();
            String exchange = getApplicationConfig().getTest2AppExchange();

            messageSender.sendMessage(rabbitTemplate, exchange, routingKey, "TEST2 :: " + i);

        }


        /* Sending to Message Queue */
        try {
            return new ResponseEntity<String>(ApplicationConstant.IN_QUEUE, HttpStatus.OK);

        } catch (Exception ex) {

            log.error("Exception occurred while sending message to the queue. Exception= {}", ex);
            return new ResponseEntity(ApplicationConstant.MESSAGE_QUEUE_SEND_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
