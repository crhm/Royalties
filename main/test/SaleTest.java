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

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		Channel channel = new Channel("Channel", new AmazonFileFormat(), true);
		SalesHistory.get().addChannel(channel);
		Book book = new Book("Title", null, "");
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
	}

	@Test
	void testCalculateRoyalties() {
		instance1.calculateRoyalties();
		assertEquals(26.6, SalesHistory.get().getListRoyaltyHolders().get("Name").getBalance());
	}

	@Test
	void testToString() {
		System.out.println(instance1);
	}

}
