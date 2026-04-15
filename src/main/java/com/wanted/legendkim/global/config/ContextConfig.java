package com.wanted.legendkim.global.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.wanted.legendkim")
public class ContextConfig {

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();
        // 우리가 사용할 ModelMapper 설정하기
        modelMapper.getConfiguration()
                // private 필드에 접근할 수 있게 권한 부여
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                // DTO 필드와 Entity 필드 매칭 가능 여부 설정
                .setFieldMatchingEnabled(true);

        return modelMapper;
    }
}
