package importing.sales;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Locale;

import importing.FileFormat;
import main.Book;
import main.Channel;
import main.Sale;
import main.SalesHistory;

/**Class that represents the new (?) format for raw monthly sales data files from Amazon channel and performs the import of the data found
 *  in such files, through method importData(). It is an implementation of the IFileFormat interface.
 * <br>Expects to find the first sale on the third line of the csv.
 * <br>Expects sale lines to be longer than 15 characters.
 * <br>Obtains the date from the cell in the first line and second column of the csv.
 * <br>Expects the date in the file name to be of the format 'October 2017'.
 * @author crhm
 *
 */
public class AmazonFileFormat extends FileFormat implements java.io.Serializable {

	private static final long serialVersionUID = -5610610928541188327L;
	private String date = "";

	public AmazonFileFormat() {
		super();
		super.firstLineOfData = 2;
		super.minLengthOfLine = 15;
		super.oldDateFormat = new SimpleDateFormat("MMMMM yyyy");
	}

	/**Imports the sales data found in the raw monthly sales data file from Amazon channel into the app.
	 * <br>Reads the file and then performs data processing for each sale.
	 * <br>Expects prices to be in american number format (commas for thousands, full stop for decimals)
	 * <br>Expects values for 'Royalty Type' to be of the format '70%' rather than '0.7'
	 * @param filePath path (from src folder) + name + extension of file to be read and imported.
	 */
	@Override
	public void importData(String filePath) {
		String[] allLines = readFile(filePath);

		//Obtains the date to give to all sales from the cell in the first line and second column of the csv
		//And formats it into the expected format.
		String[] firstLine = allLines[0].split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
		this.date = obtainDate(firstLine[1]);

		//Parses data for each sale and imports it by calling importSale on each sales line of csv
		//Considers that the first line of sales is the third line of csv.
		//Stops if counter reaches the total number of lines of csv, or if the line is shorter than 15 characters,
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

		Channel channel = obtainChannel("Amazon", new AmazonFileFormat(), false);

		//Checks the column called 'Marketplace' for the last two characters, which it converts to upper case and assigns to
		//country, unless the value is '.com', in which case the country is "US".
		String country = "";
		if (lineDivided[3].endsWith(".com")) {
			country = "US";
		} else {
			int length = lineDivided[3].length();
			country = lineDivided[3].substring(length - 2, length).toUpperCase();
		}
		
		//Gets book
		Book book = obtainBook(lineDivided[0], lineDivided[1], lineDivided[2]);

		//Assigns value of seventh column ('Net Units Sold') as the netUnitsSold
		double netUnitsSold = Double.parseDouble(lineDivided[6]);
		
		//Assigns the value of the eigth column ('Royalty Type'), minus the '%' sign and converted into a value between 0 and 1,
		//as the royalty percentage that PLP gets for this sale.
		double royaltyTypePLP = Double.parseDouble(lineDivided[7].replace("%", "")) / 100;

		//Parses the value in 12th column (Average Offer Price Without Tax) as a number in american format 
		//(comma for thousands, full stop for decimals), and assigns it to the price of the item sold.
		//Because of the commma the value is surrounded with quotes, which need to be removed.
		NumberFormat format = NumberFormat.getInstance(Locale.US);
		Number number1 = null;
		try {
			number1 = format.parse(lineDivided[11].replace("\"", ""));
		} catch (ParseException e) {
			System.out.println("There was an error parsing the data in column 'Average Offer Price Without Tax'; "
					+ "a number in american format was expected.");
			e.printStackTrace();
		}
		double price = number1.doubleValue();

		//Parses the value in 14th column (Average Delivery Cost) as a number in american format 
		//(comma for thousands, full stop for decimals), and assigns it to the delivery Cost of the item sold.
		//If the value is "N/A", delivery cost is assigned 0.
		Number number2 = null;
		try {
			if (lineDivided[13].equals("N/A")) {
				number2 = format.parse("0");
			} else {				
				number2 = format.parse(lineDivided[13].replace("\"", ""));
			}
		} catch (ParseException e) {
			System.out.println("There was an error parsing the data in column 'Average Delivery Cost'; "
					+ "either 'N/A' or a number in american format was expected.");
			e.printStackTrace();
		}
		double deliveryCost = 0;
		//In the raw data sales are given a negative delivery cost if there was an odd number of returns, this fixes that
		if (number2.doubleValue() < 0) {
			deliveryCost = number2.doubleValue() * -1;
		} else {
			deliveryCost = number2.doubleValue();
		}

		//Parses the value in 15th column (Royalty) as a number in american format 
		//(comma for thousands, full stop for decimals), and assigns it to amount PLP made off this sale.
		//Because of the commma the value is surrounded with quotes, which need to be removed.
		Number number3 = null;
		try {
			number3 = format.parse(lineDivided[14].replace("\"", ""));
		} catch (ParseException e) {
			System.out.println("There was an error parsing the data in column 'Royalty'; a number in american format was expected.");
			e.printStackTrace();
		}
		double revenuesPLP = number3.doubleValue();

		//Initialises the Currency of the sale, using the value of the 10th cell (Currency) as the currency code 
		// needed to obtain the correct Currency instance.
		Currency currency = Currency.getInstance(lineDivided[9]);

		//Creates the sale and adds its to the app
		Sale sale = new Sale(channel, country, this.date, book, netUnitsSold, royaltyTypePLP, price, deliveryCost, revenuesPLP, currency);
		SalesHistory.get().addSale(sale);
	}

}
