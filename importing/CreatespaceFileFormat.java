package importing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.junit.platform.commons.util.StringUtils;

import main.Book;
import main.Channel;
import main.Sale;
import main.SalesHistory;

public class CreatespaceFileFormat implements IFileFormat {
		
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
			int counter = 4;
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
		String[] lineDivided = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
		
	
		Channel channel = null;
		Boolean flag1 = true;
		for (Channel ch : SalesHistory.get().getListChannels().values()) {
			if (ch.getName().equals("Createspace")) {
				channel = ch;
				flag1 = false;
			}
		}
		if (flag1) {
			channel = new Channel("Createspace", new CreatespaceFileFormat());
			SalesHistory.get().addChannel(channel);
		}
		String country = "";
		SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd");
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
			if (b.getTitle().equals(lineDivided[1])) {
				book = b;
				flag2 = false;
			}
		}
		if (flag2) {
			book = new Book(lineDivided[1], "", lineDivided[5]);
			SalesHistory.get().addBook(book);			
		}
		double netUnitsSold = Integer.parseInt(lineDivided[12]);
		double price = Double.parseDouble(lineDivided[10].substring(1, 4));
		double deliveryCost = Double.parseDouble(lineDivided[11].substring(1, 4));
		double revenuesPLP = Double.parseDouble(lineDivided[13].substring(1, 4));
		double royaltyTypePLP = revenuesPLP / (netUnitsSold * (price - deliveryCost));
		Currency currency = getCurrency(lineDivided[10]);
		
		
		Sale sale = new Sale(channel, country, newFormat.format(date), book, netUnitsSold, royaltyTypePLP, price, deliveryCost, revenuesPLP, currency);
		SalesHistory.get().addSale(sale);
	}
	
	private Currency getCurrency(String cellWithSymbol) {
		Currency currency = null;
	    String[] arr = cellWithSymbol.split("\\d+", 2);
	    String symbol = arr[0].trim();
	    currency = Currency.getInstance(getAllCurrencies().get(symbol));
		return currency;
		
	}
	
	private static Map<String, String> getAllCurrencies() {
	    Map<String, String> currencies = new TreeMap<String, String>();
	    for (Locale locale : Locale.getAvailableLocales()) {
	        if (StringUtils.isNotBlank(locale.getCountry())) {
	            Currency currency = Currency.getInstance(locale);
	            currencies.put(currency.getSymbol(locale), currency.getCurrencyCode());
	        }
	    }
	    return currencies;
	}
	
}
