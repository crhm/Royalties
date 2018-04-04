package importing.sales;

import java.text.SimpleDateFormat;
import java.util.Currency;

import importing.FileFormat;
import main.Book;
import main.Channel;
import main.ObjectFactory;

/**Class that represents the format for raw monthly sales data files from Kobo channel and performs the import of the data found
 *  in such files, through method importData(). It is an implementation of the IFileFormat interface.
 *<br>Expects to find the first sale on the second line of the csv.
 *<br>Expects sale lines to be longer than 9 characters.
 *<br>Expects the dates to be of the format '23/01/2017'.
 * @author crhm
 *
 */
public class KoboFileFormat extends FileFormat implements java.io.Serializable {

	private static final long serialVersionUID = 126581817248928432L;

	public KoboFileFormat() {
		super();
		super.firstLineOfData = 1;
		super.minLengthOfLine = 9;
		super.oldDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	}

	/**Imports the sales data found in the raw monthly sales data file from Kobo channel into the app.
	 * <br>Reads the file and then performs data processing for each sale.
	 * <br>Always sets sale currency to USD because the conversion is done in the raw data already.
	 * @param filePath path (from src folder) + name + extension of file to be read and imported.
	 */
	@Override
	public void importData(String filePath) {
		String[] allLines = readFile(filePath);

		//Parses data for each sale and imports it by calling importSale on each sales line of csv
		//Considers that the first line of sales is the second line of csv.
		//Stops if counter reaches the total number of lines of csv, or if the line is shorter than 9 characters,
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

		Channel channel = obtainChannel("Kobo", new KoboFileFormat(), true);

		//Assigns the value of the second cell (Country) to country
		String country = lineDivided[1];

		String date = obtainDate(lineDivided[0]);

		//Get book
		Book book = obtainBook(lineDivided[12], lineDivided[11], lineDivided[10]);
		
		//Assigns the value found in the sixth cell (Total Qty) to netUnitsSold
		double netUnitsSold = Double.parseDouble(lineDivided[5]);
		
		//Assigns the value found in the 16th cell (COGS %) to royaltyTypePLP
		double royaltyTypePLP = Double.parseDouble(lineDivided[15]);

		//Assigns the value found in the 15th cell (COGS Based LP Excluding Tax) to price
		double price = Double.parseDouble(lineDivided[21]);

		//Sets deliveryCost to 0.
		double deliveryCost = 0;

		//Assigns the value found in the 25th cell (Net due (Payable Currency)) to revenuesPLP
		double revenuesPLP = Double.parseDouble(lineDivided[24]);

		//Sets the Currency of the sale to US Dollars, since they do the conversion themselves in the file
		Currency currency = Currency.getInstance("USD");

		//Creates the sale and adds its to the app
		ObjectFactory.createSale(channel, country, date, book, netUnitsSold, royaltyTypePLP, price, deliveryCost, revenuesPLP, currency);
	}

}
