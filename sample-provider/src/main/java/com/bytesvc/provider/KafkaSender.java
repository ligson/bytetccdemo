package com.bytesvc.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
public class KafkaSender {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    //发送消息方法
    public void send(String txt) {
        Message message = new Message();
        message.setId(System.currentTimeMillis());
        message.setMsg(UUID.randomUUID().toString());
        message.setSendTime(new Date());
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(message);
            log.info("+++++++++++++++++++++  message = {}", json);
            kafkaTemplate.send("zhisheng", json);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }

    }
}
