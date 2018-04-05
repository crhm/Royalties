package importing.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import importing.ChannelRoyaltiesFileFormat;
import main.SalesHistory;

class ChannelRoyaltiesFileFormatTest {

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
		new ImportSeveralFiles("Data/File names.txt");
		ChannelRoyaltiesFileFormat test = new ChannelRoyaltiesFileFormat();
		test.importData("Data/Amazon Royalties.csv");
		assertEquals(155, SalesHistory.get().getListChannels().get("Amazon").getListRoyalties().keySet().size());
	}

}
