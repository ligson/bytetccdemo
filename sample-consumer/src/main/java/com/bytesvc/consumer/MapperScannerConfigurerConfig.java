package com.bytesvc.consumer;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(MyBatisConfig.class)
public class MapperScannerConfigurerConfig {
    @Bean
    public static MapperScannerConfigurer configurer() throws Exception {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.bytesvc.*.dao");
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sessionFactory");
        return mapperScannerConfigurer;
    }
}
