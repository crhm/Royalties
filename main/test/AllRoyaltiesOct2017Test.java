package main.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

import importing.ChannelRoyaltiesFileFormat;
import importing.test.ImportSeveralFiles;
import main.SalesHistory;
import main.royalties.IRoyaltyType;

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

//		System.out.println("\n\nRoyalty holders where calculations match: ");
//		for (String name : SalesHistory.get().getListRoyaltyHolders().keySet()) {
//			if (actualBalances.containsKey(name)) {
//				BigDecimal actualBalance = new BigDecimal(actualBalances.get(name)).setScale(2, RoundingMode.HALF_UP);
//				double difference = SalesHistory.get().getListRoyaltyHolders().get(name).getBalance() - actualBalance.doubleValue();
//				if (difference == 0) {
//					System.out.println(name + ": Actual Balance - " + actualBalance.doubleValue() + " vs. Balance Calculated - " 
//							+ SalesHistory.get().getListRoyaltyHolders().get(name).getBalance());
//				}				
//			}
//		}
//		System.out.println("\nRoyalty holders where calculations almost match: ");
//		for (String name : SalesHistory.get().getListRoyaltyHolders().keySet()) {
//			if (actualBalances.containsKey(name)) {
//				BigDecimal actualBalance = new BigDecimal(actualBalances.get(name)).setScale(2, RoundingMode.HALF_UP);
//				double difference = SalesHistory.get().getListRoyaltyHolders().get(name).getBalance() - actualBalance.doubleValue();
//				BigDecimal diffBD = new BigDecimal(difference);
//				BigDecimal percentageDiff = diffBD.divide(actualBalance, 3, RoundingMode.HALF_UP);
//				if (difference > -0.5 && difference < 0.5 && difference != 0) {
//					System.out.println(name + ": Actual Balance - " + actualBalance.doubleValue() + " vs. Balance Calculated - " 
//							+ SalesHistory.get().getListRoyaltyHolders().get(name).getBalance() 
//							+ " Difference: " + percentageDiff.doubleValue()*100 + "%");
//				}				
//			}
//		}
//		System.out.println("\nRoyalty holders where calculations don't match: ");
//		for (String name : SalesHistory.get().getListRoyaltyHolders().keySet()) {
//			if (actualBalances.containsKey(name)) {
//				BigDecimal actualBalance = new BigDecimal(actualBalances.get(name)).setScale(2, RoundingMode.HALF_UP);
//				double difference = SalesHistory.get().getListRoyaltyHolders().get(name).getBalance() - actualBalance.doubleValue();
//				BigDecimal diffBD = new BigDecimal(difference);
//				BigDecimal percentageDiff = diffBD.divide(actualBalance, 3, RoundingMode.HALF_UP);
//				if (difference < -0.5 || difference > 0.5) {
//					System.out.println(name + ": Actual Balance - " + actualBalance.doubleValue() + " vs. Balance Calculated - " 
//							+ SalesHistory.get().getListRoyaltyHolders().get(name).getBalance() 
//							+ " Difference: " + percentageDiff.doubleValue()*100 + "%");
//				}				
//			}
//		}
//		for (Book b : SalesHistory.get().getListPLPBooks().values()) {
//			System.out.println(royaltiesUniformAcrossChannelsCheck(b));
//		}


//		System.out.println(SalesHistory.get().seeNumberOfBooks());
//		System.out.println(SalesHistory.get().getBook(168));
//		System.out.println(SalesHistory.get().seeNumberOfPersons());
	}

//	private static Boolean royaltiesUniformAcrossChannelsCheck(Book book) {
//		Boolean uniformity = true;
//		HashMap<Person, IRoyaltyType> royaltyRules = SalesHistory.get().getListChannels().get("Amazon").getListRoyalties().get(book);
//		System.out.println(book);
//		for (Channel ch : SalesHistory.get().getListChannels().values()) {
//			if (!ch.getName().equals("Createspace")) {
//				for (Person p : ch.getListRoyalties().get(book).keySet()) {
//					if (!royaltyRules.keySet().contains(p)) {
//						uniformity = false;
//					} else if (ch.getListRoyalties().get(book).get(p) != royaltyRules.get(p)) {
//						uniformity = false;
//					}
//				}
//			}
//		}
//		return uniformity;
//	}

}
