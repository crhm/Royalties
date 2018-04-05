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

import importing.sales.NookFileFormat;
import main.Sale;
import main.SalesHistory;

class NookFileFormatTest {

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
		NookFileFormat test = new NookFileFormat();
		test.importData("Data/UsuableFormats/Nook report for Oct 2017 -- payments_by_id_0000682440.csv");
		assertEquals(10, SalesHistory.get().getListPLPBooks().size());
		assertEquals(10, SalesHistory.get().getSalesHistory().size());
		assertNotNull(SalesHistory.get().getListChannels().get("Nook"));
		for (Sale s : SalesHistory.get().getSalesHistory()) {
			BigDecimal total = new BigDecimal((s.getPrice() - s.getDeliveryCost()) * s.getNetUnitsSold());
			BigDecimal royaltyPercentage = new BigDecimal(s.getRoyaltyTypePLP());
			BigDecimal royaltyAmountRecalculated = total.multiply(royaltyPercentage, new MathContext(4, RoundingMode.HALF_UP));
			double difference = royaltyAmountRecalculated.doubleValue() - s.getRevenuesPLP();
			assertTrue(difference > -0.1 && difference < 0.1);
		}
	}

}
