package io.github.rimonmostafiz;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class DidacticJourneyApplicationTests {

	@Autowired ResourceLoader resourceLoader;

	@Test
	void contextLoads() {
	}

	@Test
	void findMigrationFile() {
		String location = "classpath:db/migration";
		boolean exists = this.resourceLoader.getResource(location).exists();
		assertTrue(exists);
	}

}
