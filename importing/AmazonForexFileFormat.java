package importing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import main.Channel;

/**Class that represents the format for FX rates and payment data files from Amazon channel and performs the import of the data found
 *  in such files, through method importData(). It is an implementation of the IFileFormat interface.
 * <br>Expects to find a date of format '2017-01-23' in the first cell of the second line of the csv.
 * <br>Expects to find the first relevant line at the second line of the csv.
 * <br>Expects relevant lines to be more than 15 characters long.
 * @author crhm
 *
 */
public class AmazonForexFileFormat extends FileFormat {
	
	public AmazonForexFileFormat() {
		super();
		super.firstLineOfData = 1;
		super.minLengthOfLine = 15;
		super.oldDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	}

	private String monthAndYear = "";
	private HashMap<String, Double> listForex = new HashMap<String, Double>();
	
	/** Imports the FX data found in the Amazon raw FX data file designated by the argument passed.
	 * <br>Expects relevant lines to be only even numbered lines.
	 * <br>Will rewrite FX rates for existing currencies also present in this file, 
	 *  if any are present in the data for this month and year.
	 * <br>Does not import the FX rate provided by Amazon but the exact FX rate given the amount due in foreign currency and the
	 *  amount that was actually paid to PLP in dollars.
	 * @param path (from src folder) + name + extension of file to be read and imported
	 */
	@Override
	public void importData(String filePath) {
		String[] allLines = readFile(filePath);
		
		//Obtains the date to give to all sales from the cell in the second line and first column of the csv
		//And formats it into the expected format.
		String[] secondLine = allLines[1].split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
		this.monthAndYear = obtainDate(secondLine[0]);
		
		//Parses data for each currency and places it in class variable listForex by calling importForex() on each currency line of csv
		//Considers that the first line of currencies is the second line of csv.
		//Stops if counter reaches the total number of lines of csv, or if the line is shorter than 15 characters,
		//so that it doesn't try to parse the summary lines below the last currency line.
		//Skips every other line to disregard non-currency lines
		int counter = firstLineOfData;
		while (counter< allLines.length && allLines[counter].length() > minLengthOfLine) {
			importForex(allLines[counter]);
			counter = counter + 2;
		}
		
		Channel amazon = obtainChannel("Amazon", new AmazonFileFormat(), false);
		
		//Places the imported data in the app,
		//making sure not to replace the existing list of FX rates for this month and year if there is one.
		//It does however update the FX rate value for currencies that are already in the data for this month.
		if (amazon.getHistoricalForex().containsKey(monthAndYear)) {
			HashMap<String, Double> existingList = amazon.getHistoricalForex().get(monthAndYear);
			for (String s : existingList.keySet()) {
				listForex.put(s, existingList.get(s));
			}	
		}
		amazon.addHistoricalForex(monthAndYear, listForex);
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
		
		//Uses the value of the 8th cell to find currency
		String currency = lineDivided[7];
		
		//Sets the exchange rate as the division of amount paid to PLP in US dollars and the amount owed to PLP in the foreign currency,
		//thus obtaining a more exact measure than their listed FX rate.
		double exchangeRate = 0;
		BigDecimal amountPaid = new BigDecimal(lineDivided[15]);
		BigDecimal amountOwed = new BigDecimal(lineDivided[8]);
		BigDecimal rate = amountPaid.divide(amountOwed, 5, RoundingMode.HALF_UP);
		exchangeRate = rate.doubleValue();
		
		this.listForex.put(currency, exchangeRate);
	}
}
