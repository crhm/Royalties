package main.royalties.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.royalties.RoyaltyDependentOnUnitsSold;

public class RoyaltyDependentOnUnitsSoldTest {
	static RoyaltyDependentOnUnitsSold instance = null;
	static HashMap<Integer[], Double> testHashMap = null;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		Integer[] testKey1 = new Integer[] {0, 1000};
		Integer[] testKey2 = new Integer[] {1001, 2000};
		HashMap<Integer[], Double> testParameter = new HashMap<Integer[], Double>();
		testParameter.put(testKey1, 0.7);
		testParameter.put(testKey2, 0.5);
		testHashMap = testParameter;
		instance = new RoyaltyDependentOnUnitsSold(testParameter, 0.6);
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
	void testRoyaltyDependentOnUnitsSold() {
		assertNotNull(instance); 
		Boolean thrown1 = false;
		try {
			new RoyaltyDependentOnUnitsSold(testHashMap, 3);
		} catch (IllegalArgumentException e) {
			thrown1 = true;
		}
		assertTrue(thrown1);
	}

	@Test
	void testGetAmountDue() {
		assertEquals(7, instance.getAmountDue(10, 200));
		assertEquals(100, instance.getAmountDue(200, 1001));
		assertEquals(6000, instance.getAmountDue(10000, 2001));
	}
	
	@Test
	void testToString() {
		System.out.println(instance);
	}
	
	@Test
	void testGetPercentageForUnitsSold() {
		assertEquals(0.5, instance.getPercentageForUnitsSold(1001));
		assertEquals(0.6, instance.getPercentageForUnitsSold(3000));
	}
	
	@Test
	void testGetDefaultPercentage() {
		assertEquals(0.6, instance.getDefaultPercentage());
	}
	
	@Test
	void testGetPercentagesForUnitsSold() {
		Integer[] testKey1 = new Integer[] {0, 1000};
		Integer[] testKey2 = new Integer[] {1001, 2000};
		HashMap<Integer[], Double> testParameter = new HashMap<Integer[], Double>();
		testParameter.put(testKey1, 0.7);
		testParameter.put(testKey2, 0.5);
		assertEquals(testHashMap, instance.getPercentagesForUnitsSold());
	}
	
}
