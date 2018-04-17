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
		assertEquals("Name", instance1.getName());
		assertEquals(1, instance1.getListNames().size());
		assertEquals(0, instance1.getBalance());
		assertNotNull(instance1.getPersonNumber());
	}

	@Test
	void testAddToBalance() {
		Person instance2 = new Person("Test");
		instance2.addToBalance(4);
		assertEquals(4, instance2.getBalance());
		instance2.addToBalance(-5);
		assertEquals(-1, instance2.getBalance());
	}

	@Test
	void testToString() {
		System.out.println(instance1);
	}
	
	@Test
	void testAddName() {
		Person instance3 = new Person("Test");
		instance3.addName("Test2");
		assertEquals(2, instance3.getListNames().size());
	}
	
	@Test
	void testMerge() {
		Person instance4 = new Person("Test");
		Person instance5 = new Person("Test2");
		instance4.addToBalance(50);
		instance5.addToBalance(-5);
		instance5.addName("Test3");
		instance4.merge(instance5);
		assertEquals(45, instance4.getBalance());
		assertEquals(3, instance4.getListNames().size());
	}

}
