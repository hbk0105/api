package com.rest.api.kafka;

import com.rest.api.kafka.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;

// https://memorynotfound.com/spring-kafka-consume-producer-example/

@RestController
public class HomeController {

    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private Sender sender;

    @RequestMapping("get")
    public void test( @RequestParam(value="msg" , defaultValue = "") String msg){
        sender.send(msg);
    }

}