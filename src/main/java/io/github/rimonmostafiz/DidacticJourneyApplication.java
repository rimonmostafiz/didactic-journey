package io.github.rimonmostafiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableConfigurationProperties
@PropertySource({"classpath:application.properties"})
public class DidacticJourneyApplication {

	public static void main(String[] args) {
		SpringApplication.run(DidacticJourneyApplication.class, args);
	}

}
