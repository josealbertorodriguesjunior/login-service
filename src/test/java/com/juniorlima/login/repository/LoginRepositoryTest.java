package com.juniorlima.login.repository;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.juniorlima.login.model.LoginModel;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LoginRepositoryTest {
	@Autowired
	private LoginRepository loginRepository;
	private static final Long ID = 1L;
	private static final Long COUNT = 1L;
	private static final String TOKEN = "TOKENDETESTE";
	private static final String PASSWORD = "senhadasenha";
	private static final String EMAIL = "josealbertorodriguesjunior@gmail.com";

	@Before
	public void setUp() throws Exception {
		LoginModel login = new LoginModel();
		login.setToken(TOKEN);
		login.setPassword(PASSWORD);
		login.setEmail(EMAIL);
		this.loginRepository.save(login);
	}

	@After
	public final void tearDown() {
		this.loginRepository.deleteAll();
	}

	@Test
	public void testFindByToken() {
		LoginModel login = this.loginRepository.findByToken(TOKEN);
		assertEquals(TOKEN, login.getToken());
	}
	
	@Test
	public void testFindByUserId() {
		LoginModel login = this.loginRepository.findUserById(ID);
		assertEquals(ID, login.getId());
	}
	
	@Test
	public void testCountByEmailAndPassword() {
		Long count = this.loginRepository.countByEmailAndPassword(EMAIL, PASSWORD);
		assertEquals(COUNT, count);
	}
}
