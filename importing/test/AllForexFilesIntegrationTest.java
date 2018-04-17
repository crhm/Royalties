package importing.test;

import importing.forex.AmazonForexFileFormat;
import importing.forex.AppleForexFileFormat;
import main.SalesHistory;

public class AllForexFilesIntegrationTest {
	
	public static void main(String args[]) {
		
		AppleForexFileFormat test2 = new AppleForexFileFormat();
		test2.importData("Data/UsuableFormats/Apple payment data Oct 2017 -- financial_report.csv");
		
		AmazonForexFileFormat test = new AmazonForexFileFormat();
		test.importData("Data/UsuableFormats/Amazon payment data for Oct 2017 -- KDP Payments-1517170629138-0c18d9f0770c13d87289dfe806919f31.csv");

		System.out.println(SalesHistory.get().getChannel("Amazon").getHistoricalForex().get("Oct 2017"));
		System.out.println(SalesHistory.get().getChannel("Apple").getHistoricalForex().get("Oct 2017"));
	}

}
