package main.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.Book;

class BookTest {
	static Book instance1;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		instance1 = new Book("Title", null, "ISBN");
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testBook() {
		assertNotNull(instance1);
	}

	@Test
	void testGetTotalUnitsSold() {
		assertEquals(0, instance1.getTotalUnitsSold());
	}

	@Test
	void testAddUnitsToTotalSold() {
		instance1.addUnitsToTotalSold(9);
		assertEquals(9, instance1.getTotalUnitsSold());
	}

	@Test
	void testAddIdentifier() {
		instance1.addIdentifier("ISBN-13");
		assertEquals(2, instance1.getIdentifiers().size());
	}

	@Test
	void testToString() {
		System.out.println(instance1.toString());
	}

}
