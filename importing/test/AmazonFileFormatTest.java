package importing.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import importing.sales.AmazonFileFormat;
import main.SalesHistory;

class AmazonFileFormatTest {

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
		AmazonFileFormat test = new AmazonFileFormat();
		test.importData("Data/UsuableFormats/Amazon sales & royalty data Oct 2017 -- KDP Prior Month Royalties-2017-10-1517170571744-77e60e4db2d4ae84832f7bea14d2b940.csv");
		assertEquals(89, SalesHistory.get().getListPLPBooks().size());
		assertEquals(196, SalesHistory.get().getSalesHistory().size());
		assertNotNull(SalesHistory.get().getChannel("Amazon"));
	}

}
