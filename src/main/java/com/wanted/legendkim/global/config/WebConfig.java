package com.wanted.legendkim.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/* comment.
    브라우저가 요청을 하면, Spring 에서 WebConfig 를 인식해 URL 패턴을
    매칭하게 된다. 우리가 설정한 디렉토리를 찾아서 파일을 반환해준다.
    이 과정이 없을 경우 Spring 은 해당 파일을 알 수 없다.
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/lectures/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }

}
