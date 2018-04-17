package main.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Currency;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import importing.sales.AmazonFileFormat;
import main.Book;
import main.Channel;
import main.ObjectFactory;
import main.Person;
import main.Sale;
import main.SalesHistory;

class ObjectFactoryTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testCreateBookNoExtraAuthors() {
		Book book1 = ObjectFactory.createBook("Title", "NewAuthor", "ISBN");
		assertEquals(book1, SalesHistory.get().getBook("Title"));
		assertEquals("NewAuthor", SalesHistory.get().getPerson("NewAuthor").getName());
		
		SalesHistory.get().addPerson(new Person("ExistingAuthor"));
		Book book2 = ObjectFactory.createBook("Title2", "ExistingAuthor", "ISBN2");
		assertEquals(book2, SalesHistory.get().getBook("Title2"));
	}

	@Test
	void testCreateBookWithAllAuthors() {
		SalesHistory.get().addPerson(new Person("ExistingAuthor2"));
		Book book3 = ObjectFactory.createBook("Title", "NONEXISTENT", "ExistingAuthor2", "ExistingAuthor2", null, null, "ISBN");
		assertEquals(SalesHistory.get().getPerson("ExistingAuthor2"), book3.getAuthor2());
		assertNull(book3.getAuthor1());
	}

	@Test
	void testCreatePerson() {
		Person person = ObjectFactory.createPerson("NewPerson");
		assertEquals(person, SalesHistory.get().getPerson("NewPerson"));
	}

	@Test
	void testCreateChannelDefaultBoolean() {
		Channel channel = ObjectFactory.createChannel("NewChannel", new AmazonFileFormat());
		assertEquals(channel, SalesHistory.get().getChannel("NewChannel"));
	}

	@Test
	void testCreateChannelCustomBoolean() {
		Channel channel2 = ObjectFactory.createChannel("NewChannel2", new AmazonFileFormat(), true);
		assertEquals(channel2, SalesHistory.get().getChannel("NewChannel2"));
		assertTrue(channel2.getSaleCurrencyIsAlwaysUSD());
	}

	@Test
	void testCreateSale() {
		Channel channel3 = ObjectFactory.createChannel("NewChannel3", new AmazonFileFormat(), true);
		Book book4 = ObjectFactory.createBook("Title4", "ExistingAuthor", "ISBN4");
		Sale sale = ObjectFactory.createSale(channel3, "US", "Jan 2009", book4, 10, 0.7, 10, 0, 70, Currency.getInstance("USD"));
		assertEquals(sale, SalesHistory.get().getSalesHistory().get(0));
	}

}
