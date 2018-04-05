package main.royalties.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.royalties.RoyaltyPercentage;

class RoyaltyPercentageTest {
	RoyaltyPercentage instance1;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		instance1 = new RoyaltyPercentage(0.05);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testRoyaltyPercentage() {
		assertNotNull(instance1);
		Boolean thrown = false;
		try {
			new RoyaltyPercentage(1.05);
		} catch (IllegalArgumentException e) {
			thrown = true;
		}
		assertTrue(thrown);
	}

	@Test
	void testGetAmountDue() {
		assertEquals(5, instance1.getAmountDue(100, 0));
	}

	@Test
	void testToString() {
		System.out.println(instance1);
	}

	@Test
	void testGetPercentage() {
		assertEquals(0.05, instance1.getPercentage());
	}

}
