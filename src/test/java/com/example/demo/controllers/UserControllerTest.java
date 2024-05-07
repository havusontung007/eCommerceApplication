package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {
	@Autowired
	private UserController userController;
	@MockBean
	private UserRepository userRepository;
	@MockBean
	private CartRepository cartRepository;
	@MockBean
	private BCryptPasswordEncoder encoder;

	@Before
	public void setUp() {
		User user = new User();
		Cart cart = new Cart();
		user.setId(0);
		user.setUsername("username");
		user.setPassword("password");
		user.setCart(cart);
		when(userRepository.findByUsername("username")).thenReturn(user);
		when(userRepository.findById(0L)).thenReturn(java.util.Optional.of(user));
		when(userRepository.findByUsername("username1")).thenReturn(null);

	}

	/**
	 * Test create user
	 */
	@Test
	public void test_createUser_normalCase() {
		when(encoder.encode("password")).thenReturn("hashedPassword");
		CreateUserRequest r = new CreateUserRequest();
		r.setUsername("username");
		r.setPassword("password");
		r.setConfirmPassword("password");
		var response = userController.createUser(r);
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		User u = response.getBody();
		assertNotNull(u);
		assertEquals(0, u.getId());
		assertEquals("username", u.getUsername());
		assertEquals("hashedPassword", u.getPassword());

	}

	/**
	 * Test create user with short password
	 */
	@Test
	public void test_createUser_abnormalCase01() {
		CreateUserRequest r = new CreateUserRequest();
		r.setUsername("username");
		r.setPassword("abc");
		r.setConfirmPassword("abc");
		var response = userController.createUser(r);
		assertNotNull(response);
		assertEquals(400, response.getStatusCodeValue());
	}

	/**
	 * Test create user with mismatch password
	 */
	@Test
	public void test_createUser_abnormalCase02() {
		CreateUserRequest r = new CreateUserRequest();
		r.setUsername("test");
		r.setPassword("abcdefghijklmnopqrstuvxyz");
		r.setConfirmPassword("abcdefghijklmnopqrstuvxyy");
		var response = userController.createUser(r);
		assertNotNull(response);
		assertEquals(400, response.getStatusCodeValue());
	}

	/**
	 * Test find user by name
	 */
	@Test
	public void test_findUserByName_normalCase01() {
		var response = userController.findByUserName("username");
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		User u = response.getBody();
		assertNotNull(u);
		assertEquals("username", u.getUsername());
	}

	/**
	 * Test find user by name not found
	 */
	@Test
	public void test_findUserByName_normalCase02() {
		var response = userController.findByUserName("username1");
		assertNotNull(response);
		assertEquals(404, response.getStatusCodeValue());
	}

	/**
	 * Test find user by id
	 */
	@Test
	public void test_findUserById_normalCase01() {
		var response = userController.findById(0L);
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		User u = response.getBody();
		assertNotNull(u);
		assertEquals(0, u.getId());
	}

	/**
	 * Test find user by id not found
	 */
	@Test
	public void test_findUserById_normalCase02() {
		var response = userController.findById(1L);
		assertNotNull(response);
		assertEquals(404, response.getStatusCodeValue());
	}

}
