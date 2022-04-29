package com.ahdark.code.link;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class LinkApplication {
    @Autowired
    HttpServletRequest httpServletRequest;

    public static void main(String[] args) {
        SpringApplication.run(LinkApplication.class, args);
    }

    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> containerCustomizer() {
        return (container -> {
            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/");
            container.addErrorPages(error404Page);
        });
    }
}
