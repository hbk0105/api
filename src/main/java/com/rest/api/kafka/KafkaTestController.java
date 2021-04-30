package com.rest.api.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

// https://memorynotfound.com/spring-kafka-consume-producer-example/
// https://cookiethecat2020.blogspot.com/2020/03/blog-post.html
/*
    cd C:\kafka_2.12-2.7.0\bin\windows

    2. cmd새창 실행
    2-1. cd D:\kafka_2.12-2.7.0 이동   bin\windows\zookeeper-server-start.bat config\zookeeper.properties 입력

    3. cmd 새창 실행
    3-1. cd D:\kafka_2.12-2.7.0 이동 bin\windows\kafka-server-start.bat config\server.properties 입력

*/

@RestController
public class KafkaTestController {

    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private Sender sender;

    @RequestMapping("/get")
    public void test( @RequestParam(value="msg" , defaultValue = "") String msg){
        try {
            sender.send(msg);
        }catch (Exception o_O){
            o_O.printStackTrace();
        }

    }


}