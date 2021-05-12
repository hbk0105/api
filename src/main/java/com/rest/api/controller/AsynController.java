package com.rest.api.controller;

import com.rest.api.service.AsynService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AsynController {

    private Logger logger = LoggerFactory.getLogger(AsynController.class);

    @Autowired
    private AsynService asynService;

    @GetMapping("/api/async")
    public String goAsync() {
        asynService.onAsync();
        String str = "Async Hello Spring Boot Async!!";
        logger.info(str);
        logger.info("==================================");
        return str;
    }

    @GetMapping("/api/sync")
    public String goSync() {
        asynService.onSync();
        String str = "Sync Hello Spring Boot Sync!!";
        logger.info(str);
        logger.info("==================================");
        return str;
    }



}
