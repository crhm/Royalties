package main.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Currency;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import importing.sales.AmazonFileFormat;
import main.*;
import main.royalties.RoyaltyPercentage;

class SalesHistoryTest {
	static Channel channel;
	static Book book;
	static Sale sale;
	static Person person;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		channel = new Channel("Channel", new AmazonFileFormat(), true);
		book = new Book("Title", null, "");
		sale = new Sale(channel, "US", "Jan 2009", book, 8, 0.7, 10, 0.5, 53.2, Currency.getInstance("USD"));
		person = new Person("Name");

		SalesHistory.get().addBook(book);
		SalesHistory.get().addSale(sale);
		SalesHistory.get().addRoyaltyHolder(person);
		channel.addRoyalty(book, "Name", new RoyaltyPercentage(0.5)); 
		//The above has to be after adding the royaltyholder to SalesHistory otherwise it didn't work
		SalesHistory.get().addChannel(channel);
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
	void testGet() {
		assertTrue((SalesHistory.get() instanceof SalesHistory));
	}

	@Test
	void testCalculateAllRoyalies() {
		SalesHistory.get().calculateAllRoyalies();
		assertEquals(26.6, SalesHistory.get().getListRoyaltyHolders().get("Name").getBalance());
	}

	@Test
	void testAddSale() {
		assertEquals(sale, SalesHistory.get().getSalesHistory().get(0));
	}

	@Test
	void testAddRoyaltyHolder() {
		assertEquals(person, SalesHistory.get().getListRoyaltyHolders().get("Name"));
	}

	@Test
	void testAddBook() {
		assertEquals(book, SalesHistory.get().getBook("Title"));
	}

	@Test
	void testAddChannel() {
		assertEquals(channel, SalesHistory.get().getListChannels().get("Channel"));
	}

//	@Test
//	void testSerialise() {
//		SalesHistory.get().serialise();
//	}
//
//	@Test
//	void testDeSerialise() {
//		SalesHistory.get().serialise();
//		SalesHistory.get().addBook(new Book("TEST", null, ""));
//		SalesHistory.get().deSerialise();
//		//Checking that it has overwritten the change that had not been saved, namely adding a second book to the list of PLP books
//		assertEquals(1, SalesHistory.get().getListPLPBooks().size());
//	}

}
