package ru.itis.javalab.pdfgenerator.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan({"ru.itis.javalab.pdfgenerator"})
@PropertySource("classpath:application.properties")
public class ApplicationConfig {
}
