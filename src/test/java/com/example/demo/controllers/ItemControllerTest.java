package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemControllerTest {
	@Autowired
	private ItemController itemController;
	@MockBean
	private ItemRepository itemRepository;

	@Before
	public void setUp() {
		Item item = new Item();
		item.setId(1L);
		item.setName("Item");
		BigDecimal price = BigDecimal.valueOf(100);
		item.setPrice(price);
		item.setDescription("Item description");

		when(itemRepository.findAll()).thenReturn(Collections.singletonList(item));
		when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(item));
		when(itemRepository.findByName("Item")).thenReturn(Collections.singletonList(item));

	}

	/**
	 * Test get all items
	 */
	@Test
	public void test_getAllItem() {
		var response = itemController.getItems();
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		List<Item> items = response.getBody();
		assertNotNull(items);
		assertEquals(1, items.size());
	}

	/**
	 * Test get item by id
	 */
	@Test
	public void test_getItemById() {
		var response = itemController.getItemById(1L);
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		Item i = response.getBody();
		assertNotNull(i);
	}

	@Test
	public void test_getItemById_abnormalCase() {
		var response = itemController.getItemById(2L);
		assertNotNull(response);
		assertEquals(404, response.getStatusCodeValue());
	}

	/**
	 * Test get item by name
	 */
	@Test
	public void test_getItemByName() {
		var response = itemController.getItemsByName("Item");
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		List<Item> items = response.getBody();
		assertNotNull(items);
		assertEquals(1, items.size());
	}

	/**
	 * Test not found item by name
	 */
	@Test
	public void test_getItemByName_abnormalCase() {
		var response = itemController.getItemsByName("Item1");
		assertNotNull(response);
		assertEquals(404, response.getStatusCodeValue());
	}

}
