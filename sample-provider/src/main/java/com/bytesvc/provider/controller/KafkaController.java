package com.bytesvc.provider.controller;

import com.bytesvc.provider.KafkaSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {
    @Autowired
    private KafkaSender kafkaSender;

    @RequestMapping("/sendMsg")
    public void sendMsg(String msg) {
        kafkaSender.send(msg);
    }
}
