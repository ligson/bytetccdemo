package com.bytesvc.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class SampleProviderMain {

    public static void main(String[] args) {
        new SpringApplication(SampleProviderMain.class).run(args);
        System.out.println("springcloud-sample-provider started!");
    }

}
