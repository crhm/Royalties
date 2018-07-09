package main.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import importing.sales.AmazonFileFormat;
import main.Book;
import main.Channel;
import main.Person;
import main.SalesHistory;
import main.royalties.IRoyaltyType;
import main.royalties.RoyaltyPercentage;

class ChannelTest {
	Channel instance1;
	Channel instance2;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	instance1 = new Channel("Name", new AmazonFileFormat());
	instance2 = new Channel("Name", new AmazonFileFormat(), true);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testChannelStringFileFormat() {
		assertNotNull(instance1);
		assertEquals("Name", instance1.getName());
		assertFalse(instance1.getSaleCurrencyIsAlwaysUSD());
		assertNotNull(instance1.getfileFormat());
	}

	@Test
	void testChannelStringFileFormatBoolean() {
		assertNotNull(instance2);
		assertEquals("Name", instance2.getName());
		assertTrue(instance2.getSaleCurrencyIsAlwaysUSD());
		assertNotNull(instance2.getfileFormat());
	}

	@Test
	void testAddRoyalty() {
		Book book = new Book("Title");
		Person person = new Person("Name");
		SalesHistory.get().addRoyaltyHolder(person);
		IRoyaltyType royalty = new RoyaltyPercentage(0.05);
		instance1.addRoyalty(book, person, royalty);
		assertEquals(royalty, instance1.getListRoyalties().get(book).get(person));
	}

	@Test
	void testAddHistoricalForex() {
		HashMap<String, Double> list = new HashMap<String, Double>();
		list.put("USD", 1.0);
		instance1.addHistoricalForex("Dec 2018", list);
		assertEquals(Double.valueOf(1), instance1.getHistoricalForex().get("Dec 2018").get("USD"));
	}

	@Test
	void testToString() {
		System.out.println(instance1);
	}

}
