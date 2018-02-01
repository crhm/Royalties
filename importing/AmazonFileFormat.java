package importing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import main.Book;
import main.Channel;
import main.Sale;
import main.SalesHistory;

public class AmazonFileFormat implements IFileFormat{

	private String date = "";
	
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
			br.close();
			String temp = lines.toString();
			
			// Places each line as an element in an array of Strings
			String[] allLines = temp.split("\n");
			SimpleDateFormat oldFormat = new SimpleDateFormat("MMMMM yyyy");
			SimpleDateFormat newFormat = new SimpleDateFormat("MMM yyyy");
			Date date = null;
			String[] firstLine = allLines[0].split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
			String oldDate = firstLine[1];
			try {
				date = oldFormat.parse(oldDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.date = newFormat.format(date);
			
			int counter = 2;
			while (counter< allLines.length && allLines[counter].length() > 15) {
				importSale(allLines[counter]);
				counter++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void importSale(String line) {
		String[] lineDivided = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
		
	
		Channel channel = null;
		Boolean flag1 = true;
		for (Channel ch : SalesHistory.get().getListChannels().values()) {
			if (ch.getName().equals("Amazon")) {
				channel = ch;
				flag1 = false;
			}
		}
		if (flag1) {
			channel = new Channel("Amazon", new AmazonFileFormat());
			SalesHistory.get().addChannel(channel);
		}
		String country = "";
		if (lineDivided[3].endsWith(".com")) {
			country = "US";
		} else {
			int length = lineDivided[3].length();
			country = lineDivided[3].substring(length - 2, length).toUpperCase();
		}
		Book book = null;
		Boolean flag2 = true;
		for (Book b : SalesHistory.get().getListPLPBooks().values()) {
			if (b.getTitle().equals(lineDivided[0])) {
				book = b;
				flag2 = false;
			}
		}
		if (flag2) {
			book = new Book(lineDivided[0], lineDivided[1], lineDivided[2]);
			SalesHistory.get().addBook(book);			
		}
		int netUnitsSold = Integer.parseInt(lineDivided[6]);
		double royaltyTypePLP = Double.parseDouble(lineDivided[7].replace("%", "")) / 100;
		NumberFormat format = NumberFormat.getInstance(Locale.US);
		Number number1 = null;
		try {
			number1 = format.parse(lineDivided[11].replace("\"", ""));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double price = number1.doubleValue();
		double deliveryCost = 0;
		if (!lineDivided[13].equals("N/A")) {
			deliveryCost = Double.parseDouble(lineDivided[13]);
		}
	    Number number2 = null;
		try {
			number2 = format.parse(lineDivided[14].replace("\"", ""));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double revenuesPLP = number2.doubleValue();
		Currency currency = Currency.getInstance(lineDivided[9].trim());
		
		Sale sale = new Sale(channel, country, this.date, book, netUnitsSold, royaltyTypePLP, price, deliveryCost, revenuesPLP, currency);
		SalesHistory.get().addSale(sale);
	}

}
