package main.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import importing.ChannelRoyaltiesFileFormat;
import importing.test.ImportSeveralFiles;
import main.DataVerification;

class DataVerificationTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		new ImportSeveralFiles("Data/File names.txt");
//		SalesHistory.get().addChannel(new Channel("Test", new AmazonFileFormat(), false)); //To test whether they fail correctly
		ChannelRoyaltiesFileFormat test = new ChannelRoyaltiesFileFormat();
		test.importData("Data/Amazon Royalties.csv");
		test.importData("Data/Nook Royalties.csv");
		test.importData("Data/Createspace Royalties.csv");
		test.importData("Data/Kobo Royalties.csv");
		test.importData("Data/Apple Royalties.csv");
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
	void testCheckSalesDataForAllChannels() {
		assertEquals("", DataVerification.checkSalesDataForAllChannels());
	}
	
	@Test
	void testCheckRoyaltiesDataForAllChannels() {
		assertEquals("", DataVerification.checkRoyaltiesDataForAllChannels());
	}
	
	@Test
	void testCheckForexDataForRelevantChannels() {
		assertEquals("", DataVerification.checkForexDataForRelevantChannels());
	}
	
	@Test
	void testGetListChannelsWithSalesForEachMonth() {
		Set<String> channels = new HashSet<String>();
		String[] channelNames = {"Amazon", "Kobo", "Nook", "Createspace", "Apple"};
		for (String s : channelNames) {
			channels.add(s);
		}
		assertEquals(channels, DataVerification.getListChannelsWithSalesForEachMonth().get("Oct 2017"));
	}

}
