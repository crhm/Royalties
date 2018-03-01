package importing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;

import main.Book;
import main.Channel;
import main.Sale;
import main.SalesHistory;

/**Class that represents the format for raw monthly sales data files from Nook channel and performs the import of the data found
 *  in such files, through method importData(). It is an implementation of the IFileFormat interface.
 * <br>Expects to find the first sale on the second line of the csv.
 * <br>Expects sale lines to be longer than 15 characters.
 * <br>Expects the dates to be of the format '01/23/17' and to be payment dates rather than sale dates, 
 * so it will obtain the actual date by substracting two months from the date it does find.
 * @author crhm
 *
 */
public class NookFileFormat extends FileFormat implements java.io.Serializable {

	private static final long serialVersionUID = 8989674085937131255L;

	public NookFileFormat() {
		super();
		super.firstLineOfData = 1;
		super.minLengthOfLine = 15;
		super.oldDateFormat = new SimpleDateFormat("MM/dd/yy");
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

		Channel channel = obtainChannel("Nook", new NookFileFormat(), true);

		//Set country to US since Nook only sells in US
		String country = "US";

		String date = obtainDate(lineDivided[2]);
		
		//Get book
		Book book = obtainBook(lineDivided[3], lineDivided[6], lineDivided[4]);

		//Assigns the value of the 12th cell (Net Units Sold) to netUnitsSold
		double netUnitsSold = Double.parseDouble(lineDivided[11]);
		
		//Assigns the value of the 11th cell (Royalty Percent) to royaltyTypePLP
		double royaltyTypePLP = Double.parseDouble(lineDivided[10]);

		//Sets deliveryCost to zero.
		double deliveryCost = 0;

		//Assigns the value of the 14th cell (Total Royalty Paid) to PLPrevenues
		double revenuesPLP = Double.parseDouble(lineDivided[13]);

		//Assigns the rounded value of revenuesPLP / royaltyTypePLP to price, so as to get the price in USD
		BigDecimal revenuesTemp = new BigDecimal(revenuesPLP);
		BigDecimal royaltyTypeTemp = new BigDecimal(royaltyTypePLP);
		double price = revenuesTemp.divide(royaltyTypeTemp, 2, RoundingMode.HALF_UP).doubleValue() / netUnitsSold;

		//Sets the Currency of the sale to US Dollars, since they do the conversion themselves in the file
		Currency currency = Currency.getInstance("USD");


		Sale sale = new Sale(channel, country, date, book, netUnitsSold, royaltyTypePLP, price, deliveryCost, revenuesPLP, currency);
		SalesHistory.get().addSale(sale);
	}

	/**Date is two month later than actual sale since it's payment date rather than sale date, so substract two months from it.
	 * @param oldDate String that is the date to substract two months from as well as to return in new format
	 * @return String the correct date in the correct format
	 */
	@Override
	protected String obtainDate(String oldDate) {
		Date date = null;
		Date actualDate = null;
		try {
			date = oldDateFormat.parse(oldDate);
			Calendar c = Calendar.getInstance(); 
			c.setTime(date); 
			c.add(Calendar.MONTH, -2);
			actualDate = c.getTime();
		} catch (ParseException e) {
			System.out.println("There was parsing the date in the second cell of the first line of the csv."
					+ " A date of the format " + oldDateFormat + " was expected.");
			e.printStackTrace();
		}
		return newDateFormat.format(actualDate);
	}

}
