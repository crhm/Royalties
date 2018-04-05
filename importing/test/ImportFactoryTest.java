package importing.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import importing.ImportFactory;
import main.SalesHistory;

class ImportFactoryTest {

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
		ImportFactory.ImportData("Data/UsuableFormats/Createspace Oct 2017 Euros -- royalty_details_1952179_2018-01-28_narab-0.csv");
		assertEquals(1, SalesHistory.get().getListPLPBooks().size());
		assertEquals(1, SalesHistory.get().getSalesHistory().size());
		assertNotNull(SalesHistory.get().getListChannels().get("Createspace"));
		ImportFactory.ImportData("Data/UsuableFormats/Kobo Oct 2017 -- _EEAB5A75-C291-43F0-B5FC-8461EBBDBF7C_Oct 2017.csv");
		System.out.println(SalesHistory.get().getListPLPBooks());
		assertNotNull(SalesHistory.get().getListChannels().get("Kobo"));
	}

}
