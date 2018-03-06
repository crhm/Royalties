package importing.forex;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import importing.FileFormat;
import importing.sales.AppleFileFormat;
import main.Channel;

/**Class that represents the format for FX rates and payment data files from Apple channel and performs the import of the data found
 *  in such files, through method importData(). It is an implementation of the IFileFormat interface.
 * 
 * @author crhm
 *
 */
public class AppleForexFileFormat extends FileFormat implements java.io.Serializable {

	private static final long serialVersionUID = 407515394636942145L;
	private String monthAndYear = "";
	private HashMap<String, Double> listForex = new HashMap<String, Double>();

	/** Imports the FX data found in the Amazon raw FX data file designated by the argument passed.
	 * <br>Expects to find a date of format 'October, 2017' in the first cell of the first line of the csv.
	 * <br>Expects to find the first relevant line at the fifth line of the csv.
	 * <br>Expects relevant lines to be more than 15 characters long.
	 * <br>Will rewrite FX rates for existing currencies also present in this file, 
	 *  if any are present in the data for this month and year.
	 * <br>Does not import the FX rate provided by Apple but the exact FX rate given the amount due in foreign currency and the
	 *  amount that was actually paid to PLP in dollars.
	 * @param path (from src folder) + name + extension of file to be read and imported
	 */
	@Override
	public void importData(String filePath) {
		String[] allLines = readFile(filePath);

		//Obtains the date to give to all sales from the cell in the first line and first column of the csv
		//And formats it into the expected format.
		String[] firstLine = allLines[0].split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
		String[] title = firstLine[0].split("\\(");
		SimpleDateFormat oldFormat = new SimpleDateFormat("MMMMM, yyyy");
		SimpleDateFormat newFormat = new SimpleDateFormat("MMM yyyy");
		Date date = null;
		try {
			date = oldFormat.parse(title[1].replaceAll("[()]", ""));
		} catch (ParseException e) {
			System.out.println("There was parsing the date in the first cell of the first line of the csv.\n" 
					+ " A date of the format 'October, 2017' was expected.");
			e.printStackTrace();
		}
		this.monthAndYear = newFormat.format(date);

		//Parses data for each currency and places it in class variable listForex by calling importForex() on each currency line of csv
		//Considers that the first line of currencies is the fourth line of csv.
		//Stops if counter reaches the total number of lines of csv, or if the line is shorter than 15 characters,
		//so that it doesn't try to parse the summary lines below the last currency line.
		int counter = 3;
		while (counter< allLines.length && allLines[counter].length() > 15) {
			importForex(allLines[counter]);
			counter++;
		}

		Channel apple = obtainChannel("Apple", new AppleFileFormat(), false);

		//Places the imported data in the app,
		//making sure not to replace the existing list of FX rates for this month and year if there is one.
		//It does however update the FX rate value for currencies that are already in the data for this month.
		if (apple.getHistoricalForex().containsKey(monthAndYear)) {
			HashMap<String, Double> existingList = apple.getHistoricalForex().get(monthAndYear);
			for (String s : existingList.keySet()) {
				listForex.put(s, existingList.get(s));
			}	
		}
		apple.addHistoricalForex(monthAndYear, listForex);
	}

	/**Parses a currency line and places the currency and its associated FX rate into the class variable listForex.
	 * 
	 * @param line String csv line to be parsed for currency and FX rate information.
	 */
	private void importForex(String line) {
		//Divides line into its individual cells by splitting on commas that are not within quotes
		//And trims all leading and trailing whitespace from each value.
		String[] lineDivided = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
		int counter = 0;
		for (String s : lineDivided) {
			lineDivided[counter] = s.trim();
			counter++;
		}

		//Uses the value in parenthesis in the first cell as the currency
		String currency = "";
		String[] temp = lineDivided[0].split("\\(");
		currency = temp[1].replaceAll("[()]", "");

		//Sets the exchange rate as the division of amount paid to PLP in US dollars and the amount owed to PLP in the foreign currency,
		//thus obtaining a more exact measure than their listed FX rate.
		double exchangeRate = 0;
		BigDecimal amountPaid = new BigDecimal(lineDivided[9]);
		BigDecimal amountOwed = new BigDecimal(lineDivided[7]);
		BigDecimal rate = amountPaid.divide(amountOwed, 5, RoundingMode.HALF_UP);
		exchangeRate = rate.doubleValue();

		this.listForex.put(currency, exchangeRate);
	}
}
