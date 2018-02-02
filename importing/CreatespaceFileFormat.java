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

/**Class that represents the format for raw monthly sales data files from Createspace channel and performs the import of the data found
 *  in such files, through method importData(). It is an implementation of the IFileFormat interface.
 * @author crhm
 *
 */
public class CreatespaceFileFormat implements IFileFormat {
	
	/**Imports the sales data found in the raw monthly sales data file from Createspace channel into the database.
	 * <br>Reads the file and then performs data processing for each sale.
	 * <br>Expects to find the first sale on the fifth line of the csv.
	 * <br>Expects sale lines to be longer than 25 characters.
	 * <br>Expects that the currency of all monetary amounts in a sale are the same.
	 * <br>Expects the dates to be of the format '2017-01-23'.
	 * <br>Expects currency symbols to never be more than 1 character.
	 * @param filePath path (from src folder) + name + extension of file to be read and imported.
	 */
	@Override
	public void importData(String filePath) {
		try {
			//Reads file
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			StringBuilder lines = new StringBuilder();
			String line = "";
			while (line!= null) {
				line = br.readLine();
				lines.append(line + "\n");
			}
			br.close();
			
			// Places each line as an element in an array of Strings
			String temp = lines.toString();
			String[] allLines = temp.split("\n");
			
			//Parses data for each sale and imports it by calling importSale on each sales line of csv
			//Considers that the first line of sales is the fifth line of csv.
			//Stops if counter reaches the total number of lines of csv, or if the line is shorter than 25 characters,
			//so that it doesn't try to parse the summary lines below the last sale line.
			int counter = 4;
			while (counter< allLines.length && allLines[counter].length() > 25) {
				importSale(allLines[counter]);
				counter++;
			}
		} catch (IOException e) {
			System.out.println("There was an error reading this file.");
			e.printStackTrace();
		}
	}
	
	private void importSale(String line) {
		//Divides sales line into its individual cells by splitting on commas that are not within quotes
		//And trims all leading and trailing whitespace from each value.
		String[] lineDivided = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
		int counter = 0;
		for (String s : lineDivided) {
			lineDivided[counter] = s.trim();
			counter++;
		}
		
		//Checks if the Createspace channel already exists in database; if not, creates it.
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
		
		//Sets country to an empty String
		String country = "";
		
		//Obtains the date from the first cell and formats it into the expected format.
		SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat newFormat = new SimpleDateFormat("MMM yyyy");
		Date date = null;
		try {
			date = oldFormat.parse(lineDivided[0]);
		} catch (ParseException e) {
			System.out.println("There was parsing the date in the second cell of the first line of the csv."
					+ " A date of the format '2017-01-23' was expected.");
			e.printStackTrace();
		}
		
		//Checks the database to see if a book of that title already exists in the list of books managed by PLP, and assigns it to 
		//the sale if there is. If there is not, it creates one with the title provided in the second column, an empty string for author, 
		// and the ID provided in the sixth (UPC/ISBN), and adds it to the database, as well as assigning it to the sale.
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
		
		//Assigns the value of the 13th cell (Quantity) to net units sold
		double netUnitsSold = Double.parseDouble(lineDivided[12]);
		
		//Assigns the value of the 11th cell (List Price), minus its first character which is the currency symbol, to price
		double price = Double.parseDouble(lineDivided[10].substring(1, lineDivided[10].length()));
		
		//Assigns the value of the 12th cell (Unit Fees), minus its first character which is the currency symbol, to deliveryCost
		double deliveryCost = Double.parseDouble(lineDivided[11].substring(1, lineDivided[11].length()));
		
		//Assigns the value of the 14th cell (Royalty), minus its first character which is the currency symbol, to revenuesPLP
		double revenuesPLP = Double.parseDouble(lineDivided[13].substring(1, lineDivided[13].length()));
		
		//Divides revenues PLP by netUnitsSold times (price - deliverCost) and assigns it to royaltyTypePLP
		double royaltyTypePLP = revenuesPLP / (netUnitsSold * (price - deliveryCost));
		
		//Initialises the Currency of the sale, from the currency symbol found in the 11th cell (List Price)
		//Assumes that the currency of all monetary amounts in a sale are the same.
		Currency currency = getCurrency(lineDivided[10]);
		
		//Creates the sale and adds its to the database
		Sale sale = new Sale(channel, country, newFormat.format(date), book, netUnitsSold, royaltyTypePLP, price, deliveryCost, revenuesPLP, currency);
		SalesHistory.get().addSale(sale);
	}
	
	/** Returns a currency based on the symbol found as the first character of the string passed as argument
	 * 
	 * @param cellWithSymbol String whose first character is a currency symbol
	 * @return the corresponding currency
	 */
	private Currency getCurrency(String cellWithSymbol) {
		Currency currency = null;
		//Gets first character, expecting it to be the currency symbol
	    String symbol = cellWithSymbol.substring(0, 2);
	    //Retrieves the currency based on its symbol from a HashMap mapping symbols to currency codes
	    currency = Currency.getInstance(getAllCurrencies().get(symbol));
		return currency;
		
	}
	
	/** Provides a HashMap mapping currency symbols to currency codes
	 * 
	 * @return a HashMap mapping currency symbols to currency codes
	 */
	private static Map<String, String> getAllCurrencies() {
	    Map<String, String> currencies = new TreeMap<String, String>();
	    for (Locale locale : Locale.getAvailableLocales()) { //For each locale (aka country) in this list
	        if (StringUtils.isNotBlank(locale.getCountry())) {
	            Currency currency = Currency.getInstance(locale); //get the associated currency
	            currencies.put(currency.getSymbol(locale), currency.getCurrencyCode()); //and map that currency's symbol to its code
	        }
	    }
	    return currencies;
	}
	
}
