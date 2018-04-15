package main.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

import importing.ChannelRoyaltiesFileFormat;
import importing.test.ImportSeveralFiles;
import main.Person;
import main.SalesHistory;

public class AllRoyaltiesOct2017Test {

	public static void main(String args[]) {
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

		System.out.println("\n\nRoyalty holders where calculations match: ");
		for (Person p : SalesHistory.get().getListRoyaltyHolders()) {
			if (actualBalances.containsKey(p.getName())) {
				BigDecimal actualBalance = new BigDecimal(actualBalances.get(p.getName())).setScale(2, RoundingMode.HALF_UP);
				double difference = p.getBalance() - actualBalance.doubleValue();
				if (difference == 0) {
					System.out.println(p.getName() + ": Actual Balance - " + actualBalance.doubleValue() + " vs. Balance Calculated - " 
							+ p.getBalance());
				}				
			}
		}
		System.out.println("\nRoyalty holders where calculations almost match: ");
		for (Person p : SalesHistory.get().getListRoyaltyHolders()) {
			if (actualBalances.containsKey(p.getName())) {
				BigDecimal actualBalance = new BigDecimal(actualBalances.get(p.getName())).setScale(2, RoundingMode.HALF_UP);
				double difference = p.getBalance() - actualBalance.doubleValue();
				BigDecimal diffBD = new BigDecimal(difference);
				BigDecimal percentageDiff = diffBD.divide(actualBalance, 3, RoundingMode.HALF_UP);
				if (difference > -0.5 && difference < 0.5 && difference != 0) {
					System.out.println(p.getName() + ": Actual Balance - " + actualBalance.doubleValue() + " vs. Balance Calculated - " 
							+ p.getBalance() 
							+ " Difference: " + percentageDiff.doubleValue()*100 + "%");
				}				
			}
		}
		System.out.println("\nRoyalty holders where calculations don't match: ");
		for (Person p : SalesHistory.get().getListRoyaltyHolders()) {
			if (actualBalances.containsKey(p.getName())) {
				BigDecimal actualBalance = new BigDecimal(actualBalances.get(p.getName())).setScale(2, RoundingMode.HALF_UP);
				double difference = p.getBalance() - actualBalance.doubleValue();
				BigDecimal diffBD = new BigDecimal(difference);
				BigDecimal percentageDiff = diffBD.divide(actualBalance, 3, RoundingMode.HALF_UP);
				if (difference < -0.5 || difference > 0.5) {
					System.out.println(p.getName() + ": Actual Balance - " + actualBalance.doubleValue() + " vs. Balance Calculated - " 
							+ p.getBalance() + " Difference: " + percentageDiff.doubleValue()*100 + "%");
				}				
			}
		}		
	}
}
