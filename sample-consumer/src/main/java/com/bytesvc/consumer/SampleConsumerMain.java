package com.bytesvc.consumer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableEurekaClient
@EnableFeignClients("com.bytesvc.consumer.feign")
@SpringBootApplication
@MapperScan(basePackages = "com.bytesvc.*.dao")
public class SampleConsumerMain {

    public static void main(String[] args) {
        new SpringApplication(SampleConsumerMain.class).run(args);
        //new SpringApplicationBuilder(SampleConsumerMain.class).bannerMode(Banner.Mode.OFF).web(true).run(args);
        System.out.println("springcloud-sample-consumer started!");
    }

}
