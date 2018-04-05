package importing.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

import importing.ChannelRoyaltiesFileFormat;
import main.SalesHistory;

public class ImportEverything implements Runnable{

	public static void importAndCalculateRoyalties() {
		HashMap<String, Double> actualBalances = new HashMap<String, Double>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("Data/Balances Oct 2017.csv"));
			StringBuilder lines = new StringBuilder();
			String line = "";
			while (line!= null) {
				line = br.readLine();
				lines.append(line + "\n");
			}
			br.close();
			String temp = lines.toString();

			String[] allLines = temp.split("\n");
			int counter = 0;
			for (String s : allLines) {
				if (s.length() > 5 && counter > 0) { 
					String[] values = s.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
					BigDecimal roundedValue = new BigDecimal(values[0]).setScale(2, RoundingMode.HALF_UP);
					actualBalances.put(values[1], roundedValue.doubleValue());
				}
				counter ++;
			} 
		} catch (IOException e) {
			e.printStackTrace();
		}
		new ImportSeveralFiles("Data/File names.txt");
		ChannelRoyaltiesFileFormat test = new ChannelRoyaltiesFileFormat();
		test.importData("Data/Amazon Royalties.csv");
		test.importData("Data/Nook Royalties.csv");
		test.importData("Data/Createspace Royalties.csv");
		test.importData("Data/Kobo Royalties.csv");
		test.importData("Data/Apple Royalties.csv");

		SalesHistory.get().calculateAllRoyalies();
	}

	@Override
	public void run() {
		importAndCalculateRoyalties();
	}
}
