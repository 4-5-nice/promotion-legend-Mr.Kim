package com.wanted.legendkim.global.config;

import org.apache.catalina.session.StandardManager;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatSessionConfig
        implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        System.out.println("TomcatSessionConfig customize 실행됨");
        factory.addContextCustomizers((TomcatContextCustomizer) context -> {
            StandardManager manager = new StandardManager();
            manager.setPathname(null); // 세션 저장 파일 사용 안 함
            context.setManager(manager);

            System.out.println("Tomcat session persistence 비활성화 적용 완료");
        });
    }
}