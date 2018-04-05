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

import importing.sales.CreatespaceFileFormat;
import main.Sale;
import main.SalesHistory;

class CreatespaceFileFormatTest {

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
	void test() {
		CreatespaceFileFormat test = new CreatespaceFileFormat();
		test.importData("Data/UsuableFormats/Createspace Oct 2017 Euros -- royalty_details_1952179_2018-01-28_narab-0.csv");
		assertEquals(1, SalesHistory.get().getListPLPBooks().size());
		assertEquals(1, SalesHistory.get().getSalesHistory().size());
		assertNotNull(SalesHistory.get().getListChannels().get("Createspace"));
		for (Sale s : SalesHistory.get().getSalesHistory()) {
			BigDecimal total = new BigDecimal((s.getPrice() - s.getDeliveryCost()) * s.getNetUnitsSold());
			BigDecimal royaltyPercentage = new BigDecimal(s.getRoyaltyTypePLP());
			BigDecimal royaltyAmountRecalculated = total.multiply(royaltyPercentage, new MathContext(3, RoundingMode.HALF_UP));
			double difference = royaltyAmountRecalculated.doubleValue() - s.getRevenuesPLP();
			assertTrue(difference > -0.1 && difference < 0.1);
		}
	}

}
