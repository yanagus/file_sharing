package ru.bellintegrator.filesharing.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Конфигурация Spring MVC
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * Конфигурация контроллера, обрабатывающего запросы авторизации
     */
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }

}
