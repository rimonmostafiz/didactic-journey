package io.github.rimonmostafiz;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("classpath:application.properties")
@ActiveProfiles("dev")
class DidacticJourneyApplicationTests {

	private static final Logger log = LoggerFactory.getLogger(DidacticJourneyApplicationTests.class);

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	public void passwordEncoderTest() {
		String password = "password";
		String encodedPassword = passwordEncoder.encode(password);
		log.debug("password : {}", passwordEncoder.encode(encodedPassword));
		Assertions.assertNotNull(encodedPassword);
	}
}
