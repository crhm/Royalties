package importing.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import importing.sales.AppleFileFormat;
import main.SalesHistory;

class AppleFileFormatTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
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
	void testImportData() {
		AppleFileFormat test = new AppleFileFormat();
		test.importData("Data/UsuableFormats/85711804_1017_US.txt");
		assertEquals(26, SalesHistory.get().getListPLPBooks().size());
		assertEquals(26, SalesHistory.get().getSalesHistory().size());
		assertNotNull(SalesHistory.get().getChannel("Apple"));
		System.out.println(SalesHistory.get().getListPLPBooks());
	}

}
