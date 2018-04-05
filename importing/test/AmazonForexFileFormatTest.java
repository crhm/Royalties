package importing.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import importing.forex.AmazonForexFileFormat;
import main.SalesHistory;

class AmazonForexFileFormatTest {

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
		AmazonForexFileFormat test = new AmazonForexFileFormat();
		test.importData("Data/UsuableFormats/Amazon payment data for Oct 2017 -- KDP Payments-1517170629138-0c18d9f0770c13d87289dfe806919f31.csv");
		assertEquals(Double.valueOf(1), SalesHistory.get().getListChannels().get("Amazon").getHistoricalForex().get("Oct 2017").get("USD"));
	}

}
