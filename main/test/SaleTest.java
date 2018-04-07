package main.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Currency;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import importing.sales.AmazonFileFormat;
import main.royalties.RoyaltyPercentage;
import main.*;

class SaleTest {
	static Sale instance1;
	static Book book;
	static Channel channel;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		channel = new Channel("Channel", new AmazonFileFormat(), true);
		SalesHistory.get().addChannel(channel);
		book = new Book("Title", null, "");
		SalesHistory.get().addBook(book);
		instance1 = new Sale(channel, "US", "Jan 2009", book, 8, 0.7, 10, 0.5, 53.2, Currency.getInstance("USD"));
		SalesHistory.get().addRoyaltyHolder(new Person("Name"));
		channel.addRoyalty(book, "Name", new RoyaltyPercentage(0.5));
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
	void testSale() {
		assertNotNull(instance1);
		assertEquals(channel, instance1.getChannel());
		assertEquals(book, instance1.getBook());
		assertEquals("US", instance1.getCountry());
		assertEquals("Jan 2009", instance1.getDate());
		assertEquals(Currency.getInstance("USD"), instance1.getCurrency());
		assertEquals(8, instance1.getNetUnitsSold());
		assertEquals(0.7, instance1.getRoyaltyTypePLP());
		assertEquals(10, instance1.getPrice());
		assertEquals(0.5, instance1.getDeliveryCost());
		assertEquals(53.2, instance1.getRevenuesPLP());
	}

	@Test
	void testCalculateRoyalties() {
		assertFalse(instance1.getRoyaltyHasBeenCalculated());
		instance1.calculateRoyalties();
		assertEquals(26.6, SalesHistory.get().getListRoyaltyHolders().get("Name").getBalance());
		assertTrue(instance1.getRoyaltyHasBeenCalculated());
	}

	@Test
	void testToString() {
		System.out.println(instance1);
	}

}
