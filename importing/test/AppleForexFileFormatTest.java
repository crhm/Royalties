package importing.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import importing.forex.AppleForexFileFormat;
import main.SalesHistory;

class AppleForexFileFormatTest {

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
		AppleForexFileFormat test = new AppleForexFileFormat();
		test.importData("Data/UsuableFormats/Apple payment data Oct 2017 -- financial_report.csv");
		assertEquals(Double.valueOf(1), SalesHistory.get().getChannel("Apple").getHistoricalForex().get("Oct 2017").get("USD"));
	}

}
