package com.codegym;

import com.codegym.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)

public class StoryProjectApplication {

	public static void main(String[] args) {

		SpringApplication.run(StoryProjectApplication.class, args);
	}

}
