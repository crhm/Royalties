package main.royalties.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.royalties.RoyaltyFixedAmount;

class RoyaltyFixedAmountTest {
	static RoyaltyFixedAmount instance = null;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		instance = new RoyaltyFixedAmount(17);
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
	void testRoyaltyFixedAmount() {
		assertNotNull(instance);
	}

	@Test
	void testGetAmountDue() {
		assertEquals(17, instance.getAmountDue(0, 0));		
		assertEquals(17, instance.getAmountDue(100, 200));
	}

	@Test
	void testGetFixedAmount() {
		assertEquals(17, instance.getFixedAmount());		
	}

	@Test
	void testToString() {
		System.out.println(instance.toString());
	}

}
