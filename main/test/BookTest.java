package main.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.Book;
import main.Person;

class BookTest {
	static Book instance1;
	static Book instance2;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		instance1 = new Book("Title", null, "ISBN");
		Person translator = new Person("Translator");
		instance2 = new Book("Title2", null, null, translator, null, null, "ISBN2");
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testBook() {
		assertNotNull(instance1);
		assertEquals("Title", instance1.getTitle());
		assertEquals(1, instance1.getListTitles().size());
		assertEquals(1, instance1.getIdentifiers().size());
		assertEquals(0, instance1.getTotalUnitsSold());
		assertNull(instance1.getAuthor1());
		assertNull(instance1.getAuthor2());
		assertNull(instance1.getTranslator());
		assertNull(instance1.getPrefaceAuthor());
		assertNull(instance1.getAfterwordAuthor());
		
		assertNotNull(instance2);
		assertEquals("Title2", instance2.getTitle());
		assertEquals(1, instance2.getListTitles().size());
		assertEquals(1, instance2.getIdentifiers().size());
		assertEquals(0, instance2.getTotalUnitsSold());
		assertNull(instance2.getAuthor1());
		assertNull(instance2.getAuthor2());
		assertNotNull(instance2.getTranslator());
		assertNull(instance2.getPrefaceAuthor());
		assertNull(instance2.getAfterwordAuthor());
		
		assertEquals(instance1.getBookNumber() + 1, instance2.getBookNumber());
	}

	@Test
	void testAddUnitsToTotalSold() {
		Book instance3 = new Book("TEST", null, "");
		instance3.addUnitsToTotalSold(9);
		assertEquals(9, instance3.getTotalUnitsSold());
		instance3.addUnitsToTotalSold(-8);
		assertEquals(1, instance3.getTotalUnitsSold());
	}

	@Test
	void testAddIdentifier() {
		Book instance4 = new Book("TEST", null, "");
		instance4.addIdentifier("ISBN-13");
		assertEquals(1, instance4.getIdentifiers().size());
		instance4.addIdentifier("E-ISBN");
		assertEquals(2, instance4.getIdentifiers().size());
	}
	
	@Test
	void testAddTitle() {
		Book instance5 = new Book("TEST", null, "");
		assertEquals(1, instance5.getListTitles().size());
		instance5.addTitle("TEST2");
		assertEquals(2, instance5.getListTitles().size());
	}

	@Test
	void testMerge() {
		Book instance6 = new Book("Title1", new Person("Author1"), "ISBN1");
		instance6.addUnitsToTotalSold(70);
		Book instance7 = new Book("Title2", null, null, new Person("Translator"), null, null, "ISBN2");
		instance7.addUnitsToTotalSold(30);
		instance6.merge(instance7);
		assertEquals(2, instance6.getListTitles().size());
		assertEquals(2, instance6.getIdentifiers().size());
		assertNotNull(instance6.getAuthor1());
		assertNull(instance6.getAuthor2());
		assertNotNull(instance6.getTranslator());
		assertNull(instance6.getPrefaceAuthor());
		assertNull(instance6.getAfterwordAuthor());
		assertEquals(100, instance6.getTotalUnitsSold());

	}
	
	@Test
	void testToString() {
		System.out.println(instance1.toString());
	}
	
}
