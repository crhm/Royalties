package importing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;

import main.Book;
import main.Channel;
import main.Sale;
import main.SalesHistory;

public class AppleFileFormat implements IFileFormat {

	@Override
	public void importData(String filePath) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			StringBuilder lines = new StringBuilder();
			String line = "";
			while (line!= null) {
				line = br.readLine();
				lines.append(line + "\n");
			}
			String temp = lines.toString();
			
			// Places each line as an element in an array of Strings
			String[] allLines = temp.split("\n");
			br.close();
			int counter = 1;
			while (counter< allLines.length && allLines[counter].length() > 25) {
				importSale(allLines[counter]);
				counter++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void importSale(String line) {
		String[] lineDivided = line.split("\t", -1);
		
	
		Channel channel = null;
		Boolean flag1 = true;
		for (Channel ch : SalesHistory.get().getListChannels().values()) {
			if (ch.getName().equals("Apple")) {
				channel = ch;
				flag1 = false;
			}
		}
		if (flag1) {
			channel = new Channel("Apple", new AppleFileFormat());
			SalesHistory.get().addChannel(channel);
		}
		String country = lineDivided[17];
		SimpleDateFormat oldFormat = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat newFormat = new SimpleDateFormat("MMM yyyy");
		Date date = null;
		try {
			date = oldFormat.parse(lineDivided[0]);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Book book = null;
		Boolean flag2 = true;
		for (Book b : SalesHistory.get().getListPLPBooks().values()) {
			if (b.getTitle().equals(lineDivided[12])) {
				book = b;
				flag2 = false;
			}
		}
		if (flag2) {
			book = new Book(lineDivided[12], lineDivided[11], lineDivided[3]);
			SalesHistory.get().addBook(book);			
		}
		int netUnitsSold = 0;
		if (lineDivided[9].equals("S")) {
			netUnitsSold = Integer.parseInt(lineDivided[5]);
		} else {
			netUnitsSold = - Integer.parseInt(lineDivided[5]);
		}
		double royaltyTypePLP = Double.parseDouble(lineDivided[6]) / Double.parseDouble(lineDivided[20]); //Because they are the same currency
		double price = Double.parseDouble(lineDivided[20]);
		double deliveryCost = 0;
		double revenuesPLP = Double.parseDouble(lineDivided[7]);
		Currency currency = Currency.getInstance(lineDivided[21].trim());
		
		Sale sale = new Sale(channel, country, newFormat.format(date), book, netUnitsSold, royaltyTypePLP, price, deliveryCost, revenuesPLP, currency);
		SalesHistory.get().addSale(sale);
	}
	
}
