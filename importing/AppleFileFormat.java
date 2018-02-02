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

/**Class that represents the format for raw monthly sales data files from Apple channel and performs the import of the data found
 *  in such files, through method importData(). It is an implementation of the IFileFormat interface.
 * @author crhm
 *
 */
public class AppleFileFormat implements IFileFormat {

	/**Imports the sales data found in the raw monthly sales data files from Apple channel into the database.
	 * <br>Reads the file and then performs data processing for each sale.
	 * <br>Expects to find the first sale on the second line of the txt file.
	 * <br>Expects that the currency of all monetary amounts in a sale are the same.
	 * <br>Expects sale lines to be longer than 25 characters.
	 * <br>Expects dates to be of the format '10/23/2017'.
	 * <br>Expects column values to be separated by a single tabulation.
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
			
			//Parses data for each sale and imports it by calling importSale on each sales line of txt file
			//Considers that the first line of sales is the second line of txt file.
			//Stops if counter reaches the total number of lines of txt file, or if the line is shorter than 25 characters,
			//so that it doesn't try to parse the summary lines below the last sale line.
			int counter = 1;
			while (counter< allLines.length && allLines[counter].length() > 25) {
				importSale(allLines[counter]);
				counter++;
			}
		} catch (IOException e) {
			System.out.println("There was an error reading this file.");
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
		
		//Checks if the Apple channel already exists in database; if not, creates it.
		Channel channel = null;
		Boolean flag1 = true;
		for (Channel ch : SalesHistory.get().getListChannels().values()) {
			if (ch.getName().equals("Apple")) {
				channel = ch;
				flag1 = false;
			}
		}
		if (flag1) {
			channel = new Channel("Apple", new AppleFileFormat());
			SalesHistory.get().addChannel(channel);
		}
		
		//Initialises the country as the value of the 18th cell (Country of Sale)
		String country = lineDivided[17];
		
		//Obtains the date from the first cell and formats it into the expected format.
		SimpleDateFormat oldFormat = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat newFormat = new SimpleDateFormat("MMM yyyy");
		Date date = null;
		try {
			date = oldFormat.parse(lineDivided[0]);
		} catch (ParseException e) {
			System.out.println("There was parsing the date in the second cell of the first line of the csv."
					+ " A date of the format '10/23/2017' was expected.");
			e.printStackTrace();
		}
		
		//Checks the database to see if a book of that title already exists in the list of books managed by PLP, and assigns it to 
		//the sale if there is. If there is not, it creates one with the title provided in the 13th column, the author provided in
		//the 12th column, and the ID provided in the fourth (ISRC/ISBN), and adds it to the database, as well as assigning it to the sale.
		Book book = null;
		Boolean flag2 = true;
		for (Book b : SalesHistory.get().getListPLPBooks().values()) {
			if (b.getTitle().equals(lineDivided[12])) {
				book = b;
				flag2 = false;
			}
		}
		if (flag2) {
			book = new Book(lineDivided[12], lineDivided[11], lineDivided[3]);
			SalesHistory.get().addBook(book);			
		}
		
		//Checks wether this sale is a sale or a return by checking the value of the 10th cell (Sale or Return),
		//and correspondingly assigns a positive or a negative version of the value found in the 6th cell (Quantity)
		//to the variable netUnitsSold.
		int netUnitsSold = 0;
		if (lineDivided[9].equals("S")) {
			netUnitsSold = Integer.parseInt(lineDivided[5]);
		} else {
			netUnitsSold = - Integer.parseInt(lineDivided[5]);
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
		
		//Creates the sale and adds its to the database
		Sale sale = new Sale(channel, country, newFormat.format(date), book, netUnitsSold, royaltyTypePLP, price, deliveryCost, revenuesPLP, currency);
		SalesHistory.get().addSale(sale);
	}
	
}
