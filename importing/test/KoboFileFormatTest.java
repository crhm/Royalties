package importing.test;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import importing.sales.KoboFileFormat;
import main.Sale;
import main.SalesHistory;

class KoboFileFormatTest {

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
		KoboFileFormat test = new KoboFileFormat();
		test.importData("Data/UsuableFormats/Kobo Oct 2017 -- _EEAB5A75-C291-43F0-B5FC-8461EBBDBF7C_Oct 2017.csv");
		assertEquals(9, SalesHistory.get().getListPLPBooks().size());
		assertEquals(12, SalesHistory.get().getSalesHistory().size());
		assertNotNull(SalesHistory.get().getChannel("Kobo"));
		for (Sale s : SalesHistory.get().getSalesHistory()) {
			BigDecimal total = new BigDecimal((s.getPrice() - s.getDeliveryCost()) * s.getNetUnitsSold());
			BigDecimal royaltyPercentage = new BigDecimal(s.getRoyaltyTypePLP());
			BigDecimal royaltyAmountRecalculated = total.multiply(royaltyPercentage, new MathContext(3, RoundingMode.HALF_UP));
			double difference = royaltyAmountRecalculated.doubleValue() - s.getRevenuesPLP();
			assertTrue(difference > -0.1 && difference < 0.1);
		}
	}

}
