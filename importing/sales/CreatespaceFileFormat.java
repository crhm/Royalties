package importing.sales;

import java.text.SimpleDateFormat;
import java.util.Currency;

import importing.FileFormat;
import main.Book;
import main.Channel;
import main.Sale;
import main.SalesHistory;

/**Class that represents the format for raw monthly sales data files from Createspace channel and performs the import of the data found
 *  in such files, through method importData(). It is an implementation of the IFileFormat interface.
 *<br>Expects to find the first sale on the fifth line of the csv.
 *<br>Expects sale lines to be longer than 25 characters.
 *<br>Expects the dates to be of the format '2017-01-23'.
 * @author crhm
 *
 */
public class CreatespaceFileFormat extends FileFormat implements java.io.Serializable {

	private static final long serialVersionUID = -1324180921623940642L;

	public CreatespaceFileFormat() {
		super();
		super.firstLineOfData = 4;
		super.minLengthOfLine = 25;
		super.oldDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	}

	/**Imports the sales data found in the raw monthly sales data file from Createspace channel into the app.
	 * <br>Reads the file and then performs data processing for each sale.
	 * <br>Expects that the currency of all monetary amounts in a sale are the same.
	 * @param filePath path (from src folder) + name + extension of file to be read and imported.
	 */
	@Override
	public void importData(String filePath) {

		String[] allLines = readFile(filePath);

		//Parses data for each sale and imports it by calling importSale on each sales line of csv
		//Considers that the first line of sales is the fifth line of csv.
		//Stops if counter reaches the total number of lines of csv, or if the line is shorter than 25 characters,
		//so that it doesn't try to parse the summary lines below the last sale line.
		int counter = firstLineOfData;
		while (counter< allLines.length && allLines[counter].length() > minLengthOfLine) {
			importSale(allLines[counter]);
			counter++;
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

		Channel channel = obtainChannel("Createspace", new CreatespaceFileFormat(), true);

		String date = obtainDate(lineDivided[0]);
		
		//Get Book
		Book book = obtainBook(lineDivided[1], "", lineDivided[5]);

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

		//Initialises the Currency of the sale, from the currency code found in the 5th cell (Sales Channel), and assumes
		// that if there isn't one (because it can't split on "-", or parse what it expects to be a currency code) then it is USD.
		//Assumes that the currency of all monetary amounts in a sale are the same.
		Currency currency = getCurrency(lineDivided[4]);

		//Sadly no way to automate country obtention for Createspace
		String country = "";
		switch (currency.getCurrencyCode()) {
		case "EUR": country = "Eurozone";
		break;
		case "CAD": country = "CA";
		break;
		case "USD" : country = "US";
		break;
		case "GBP" : country = "UK";
		break;
		}

		//Creates the sale and adds its to the app
		Sale sale = new Sale(channel, country, date, book, netUnitsSold, royaltyTypePLP, price, deliveryCost, revenuesPLP, currency);
		SalesHistory.get().addSale(sale);
	}

	/** Returns a currency based on the currency code found after the "-" in the argument passed, 
	 * or if either it cannot find a "-" in the string or what is after it is not a currency code,
	 * it returns the US currency (USD).
	 * @param cellWithCode String to be analysed
	 * @return the corresponding currency
	 */
	private Currency getCurrency(String cellWithCode) {
		Currency currency = null;

		if(cellWithCode.contains("-")) {
			String[] nameAndSymbol = cellWithCode.split("-", -1);
			currency = Currency.getInstance(nameAndSymbol[1].trim());
		} else {
			currency = Currency.getInstance("USD");
		}
		return currency;

	}

}
