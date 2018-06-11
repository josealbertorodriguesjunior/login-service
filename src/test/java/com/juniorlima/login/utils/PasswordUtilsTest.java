package com.juniorlima.login.utils;

import java.security.NoSuchAlgorithmException;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class PasswordUtilsTest {
	private static final String PASSWORD = "123456";
	Utils utils = new Utils();

	public boolean verifyPasswordCreation(String password) throws NoSuchAlgorithmException {
		boolean yes;
		if (utils.createHash(PASSWORD).length() > 0) {
			yes = true;
		} else {
			yes = false;
		}
		return yes;
	}

	@Test
	public void testGenerateHashFromPassword() throws Exception {
		assertTrue(verifyPasswordCreation(PASSWORD));
	}
}
