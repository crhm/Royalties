package importing.sales;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Currency;

import importing.FileFormat;
import main.Book;
import main.Channel;
import main.Sale;
import main.SalesHistory;

/**Class that represents the format for raw monthly sales data files from Apple channel and performs the import of the data found
 *  in such files, through method importData(). It is an implementation of the IFileFormat interface.
 * <br>Expects to find the first sale on the second line of the txt file.
 * <br>Expects that the currency of all monetary amounts in a sale are the same.
 * <br>Expects sale lines to be longer than 25 characters.
 * <br>Expects dates to be of the format '10/23/2017'.
 * @author crhm
 *
 */
public class AppleFileFormat extends FileFormat implements java.io.Serializable {

	private static final long serialVersionUID = -7516082584099681089L;

	public AppleFileFormat() {
		super();
		super.firstLineOfData = 1;
		super.minLengthOfLine = 25;
		super.oldDateFormat = new SimpleDateFormat("MM/dd/yyyy");
	}

	/**Imports the sales data found in the raw monthly sales data files from Apple channel into the app.
	 * <br>Reads the file and then performs data processing for each sale.
	 * <br>Expects column values to be separated by a single tabulation.
	 * @param filePath path (from src folder) + name + extension of file to be read and imported.
	 */
	@Override
	public void importData(String filePath) {
		String[] allLines = readFile(filePath);

		//Parses data for each sale and imports it by calling importSale on each sales line of txt file
		//Considers that the first line of sales is the second line of txt file.
		//Stops if counter reaches the total number of lines of txt file, or if the line is shorter than 25 characters,
		//so that it doesn't try to parse the summary lines below the last sale line.
		try {
			int counter = firstLineOfData;
			while (counter< allLines.length && allLines[counter].length() > minLengthOfLine) {
				importSale(allLines[counter]);
				counter++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void importSale(String line) throws IOException {
		//Divides sales line into its individual cells by splitting on tabulations
		//And trims all leading and trailing whitespace from each value.
		String[] lineDivided = line.split("\t", -1);
		int counter = 0;
		for (String s : lineDivided) {
			lineDivided[counter] = s.trim();
			counter++;
		}

		Channel channel = obtainChannel("Apple", new AppleFileFormat(), false);

		//Initialises the country as the value of the 18th cell (Country of Sale)
		String country = lineDivided[17];

		String date = obtainDate(lineDivided[0]);

		//Gets book
		Book book = obtainBook(lineDivided[12], lineDivided[11], lineDivided[10]);

		//Checks wether this sale is a sale or a return by checking the value of the 10th cell (Sale or Return),
		//and correspondingly assigns a positive or a negative version of the value found in the 6th cell (Quantity)
		//to the variable netUnitsSold.
		double netUnitsSold = 0;
		if (lineDivided[9].equals("S")) {
			netUnitsSold = Double.parseDouble(lineDivided[5]);
		} else {
			netUnitsSold = - Double.parseDouble(lineDivided[5]);
		}

		//Divides value of seventh cell (Extended Partner Share, aka amount PLP got from this sale) by the value
		//of the 21st cell (Customer Price, since there is no delivery cost to factor in) to obtain the percentage
		//of the sale that PLP gets as royalty.
		double royaltyTypePLP = Double.parseDouble(lineDivided[6]) / Double.parseDouble(lineDivided[20]);

		//Assigns the value of the 21st cell (Customer Price) as the price of the item.
		double price = Double.parseDouble(lineDivided[20]);

		//Sets deliveryCost to 0.
		double deliveryCost = 0;

		//Assigns the value of the 8th cell (Extended Partner Share) as the revenuesPLP
		double revenuesPLP = Double.parseDouble(lineDivided[7]);

		//Initialises the Currency of the sale, using the value of the 22d cell (Customer Price Currency)  
		// as the currency code needed to obtain the correct Currency instance.
		// Assumes that value of the 9th cell (Extended Partner Share Currency) is the same.
		Currency currency = Currency.getInstance(lineDivided[21]);
		if (!lineDivided[21].equals(lineDivided[8])) {
			throw new IOException("Assumption about file format is violated; the Extended Partner Share Currency "
					+ "(currency of PLP revenues for this sale)"
					+ " was not the same as the Customer Price Currency (currency that the customer paid in.");
		}

		//Creates the sale and adds its to the app
		Sale sale = new Sale(channel, country, date, book, netUnitsSold, royaltyTypePLP, price, deliveryCost, revenuesPLP, currency);
		SalesHistory.get().addSale(sale);
	}

}
