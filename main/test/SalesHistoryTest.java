package main.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Currency;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.*;
import main.royalties.RoyaltyPercentage;

class SalesHistoryTest {
	static Channel channel;
	static Book book;
	static Sale sale;
	static Person person;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		channel = new Channel("Channel", true);
		SalesHistory.get().addChannel(channel);
		book = new Book("Title");
		SalesHistory.get().addBook(book);
		sale = new Sale(channel, "US", "Jan 2009", book, 8, 0.7, 10, 0.5, 53.2, Currency.getInstance("USD"));
		SalesHistory.get().addSale(sale);
		person = new Person("Name");
		SalesHistory.get().addPerson(person);
		
		channel.addRoyalty(book, person, new RoyaltyPercentage(0.5)); 
		//The above has to be after adding the royaltyholder to SalesHistory otherwise it didn't work
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
		assertEquals(26.6, person.getBalance());
	}

	@Test
	void testAddSale() {
		assertEquals(sale, SalesHistory.get().getSalesHistory().get(0));
	}

	@Test
	void testAddRoyaltyHolder() {
		Person person2 = null;
		for (Person p : SalesHistory.get().getListRoyaltyHolders()) {
			person2 = p;
		}
		assertEquals(person, person2);
	}

	@Test
	void testAddBook() {
		assertEquals(book, SalesHistory.get().getBook("Title"));
	}

	@Test
	void testAddChannel() {
		assertEquals(channel, SalesHistory.get().getChannel("Channel"));
	}
	
	@Test
	void testGetBook() {
		book.addTitle("TESTTITLE");
		assertEquals(book, SalesHistory.get().getBook("TESTTITLE"));
		assertNull(SalesHistory.get().getBook("NONEXISTENT"));
	}
	
	@Test
	void testGetPerson() {
		person.addName("TESTNAME");
		assertEquals(person, SalesHistory.get().getPerson("TESTNAME"));
		assertNull(SalesHistory.get().getPerson("NONEXISTENT"));

	}
	
	@Test
	void testGetListMonths() {
		assertEquals(1, SalesHistory.get().getListMonths().size());
	}
	
	@Test
	void testGetListAuthors() {
		book.setTranslator(person);
		assertEquals(1, SalesHistory.get().getListAuthors().size());
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
