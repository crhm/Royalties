package main.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.Person;

class PersonTest {
	Person instance1;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		instance1 = new Person("Name");
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testPerson() {
		assertNotNull(instance1);
	}

	@Test
	void testAddToBalance() {
		instance1.addToBalance(4);
		assertEquals(4, instance1.getBalance());
		instance1.addToBalance(-4);
		assertEquals(0, instance1.getBalance());
	}

	@Test
	void testToString() {
		System.out.println(instance1);
	}

}
