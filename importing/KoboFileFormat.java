package importing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;

import main.Book;
import main.Channel;
import main.Sale;
import main.SalesHistory;

/**Class that represents the format for raw monthly sales data files from Kobo channel and performs the import of the data found
 *  in such files, through method importData(). It is an implementation of the IFileFormat interface.
 * @author crhm
 *
 */
public class KoboFileFormat extends FileFormat {

	/**Imports the sales data found in the raw monthly sales data file from Kobo channel into the database.
	 * <br>Reads the file and then performs data processing for each sale.
	 * <br>Expects to find the first sale on the second line of the csv.
	 * <br>Expects sale lines to be longer than 9 characters.
	 * <br>Expects the dates to be of the format '23/01/2017'.
	 * <br>Always sets sale currency to USD because the conversion is done in the raw data already.
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
			//Considers that the first line of sales is the second line of csv.
			//Stops if counter reaches the total number of lines of csv, or if the line is shorter than 9 characters,
			//so that it doesn't try to parse the summary lines below the last sale line.
			int counter = 1;
			while (counter< allLines.length && allLines[counter].length() > 9) {
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
		
		//Checks if the Kobo channel already exists in database; if not, creates it.
		Channel channel = null;
		Boolean flag1 = true;
		for (Channel ch : SalesHistory.get().getListChannels().values()) {
			if (ch.getName().equals("Kobo")) {
				channel = ch;
				flag1 = false;
			}
		}
		if (flag1) {
			channel = new Channel("Kobo", new KoboFileFormat());
			SalesHistory.get().addChannel(channel);
		}
		
		//Assigns the value of the second cell (Country) to country
		String country = lineDivided[1];
		
		//Obtains the date from the first cell and formats it into the expected format.
		SimpleDateFormat oldFormat = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat newFormat = new SimpleDateFormat("MMM yyyy");
		Date date = null;
		try {
			date = oldFormat.parse(lineDivided[0]);
		} catch (ParseException e) {
			System.out.println("There was parsing the date in the second cell of the first line of the csv."
					+ " A date of the format '23/01/2017' was expected.");
			e.printStackTrace();
		}
		
		//Checks the database to see if a book of that title already exists in the list of books managed by PLP, and assigns it to 
		//the sale if there is, and adds the ID to the list of IDs of the book if it is not alrady in it. 
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
		
		//Creates the sale and adds its to the database
		Sale sale = new Sale(channel, country, newFormat.format(date), book, netUnitsSold, royaltyTypePLP, price, deliveryCost, revenuesPLP, currency);
		SalesHistory.get().addSale(sale);
	}
	
}
