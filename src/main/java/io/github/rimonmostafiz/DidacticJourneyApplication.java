package io.github.rimonmostafiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class DidacticJourneyApplication {

	public static void main(String[] args) {
		SpringApplication.run(DidacticJourneyApplication.class, args);
	}

}
