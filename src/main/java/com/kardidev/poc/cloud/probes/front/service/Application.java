package com.kardidev.poc.cloud.probes.front.service;

import java.lang.invoke.MethodHandles;
import java.util.stream.Stream;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import com.kardidev.poc.cloud.probes.front.service.config.AppConfig;

@SpringBootApplication
@Import(AppConfig.class)
public class Application {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            Thread.sleep(5000);

            log.info("Project beans loaded:");
            String[] beanNames = ctx.getBeanDefinitionNames();
            Stream.of(beanNames)
                    .filter(name -> name.startsWith("com.kardidev."))
                    .sorted()
                    .forEach(System.out::println);
        };
    }

    @PreDestroy
    public void onExit() throws InterruptedException {
        log.info("Project is being shut down...");
        Thread.sleep(5000);
        log.info("Project finished successfully");
    }
}
