package importing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
 * @author crhm
 *
 */
public class NookFileFormat implements IFileFormat{

	/**Imports the sales data found in the raw monthly sales data file from Kobo channel into the database.
	 * <br>Reads the file and then performs data processing for each sale.
	 * <br>Expects to find the first sale on the second line of the csv.
	 * <br>Expects sale lines to be longer than 15 characters.
	 * <br>Expects the dates to be of the format '01/23/17' and to be payment dates rather than sale dates, 
	 * so it will obtain the actual date by substracting two months from the date it does find.
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
			//Stops if counter reaches the total number of lines of csv, or if the line is shorter than 15 characters,
			//so that it doesn't try to parse the summary lines below the last sale line.
			int counter = 1;
			while (counter< allLines.length && allLines[counter].length() > 15) {
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
	
		//Checks if the Nook channel already exists in database; if not, creates it.
		Channel channel = null;
		Boolean flag1 = true;
		for (Channel ch : SalesHistory.get().getListChannels().values()) {
			if (ch.getName().equals("Nook")) {
				channel = ch;
				flag1 = false;
			}
		}
		if (flag1) {
			channel = new Channel("Nook", new NookFileFormat());
			SalesHistory.get().addChannel(channel);
		}
		
		//Set country to US since Nook only sells in US
		String country = "US";
		
		//Obtains the date from the third cell and formats it into the expected format.
		//Date is two month later than actual sale since it's payment info, so substract two months from it.
		SimpleDateFormat oldFormat = new SimpleDateFormat("MM/dd/yy");
		SimpleDateFormat newFormat = new SimpleDateFormat("MMM yyyy");
		Date date = null;
		Date actualDate = null;
		try {
			date = oldFormat.parse(lineDivided[2]);
			Calendar c = Calendar.getInstance(); 
			c.setTime(date); 
			c.add(Calendar.MONTH, -2);
			actualDate = c.getTime();
		} catch (ParseException e) {
			System.out.println("There was parsing the date in the second cell of the first line of the csv."
					+ " A date of the format '01/23/2017' was expected.");
			e.printStackTrace();
		}
		
		//Checks the database to see if a book of that title already exists in the list of books managed by PLP, and assigns it to 
		//the sale if there is, and adds the ID to the list of IDs of the book if it is not alrady in it. 
		//If there is not, it creates one with the title provided in the 4th column, the string provided
		// in the 7th cell for author, and the ID provided in the 5th (ebook ISBN), 
		//and adds it to the database, as well as assigning it to the sale.
		Book book = null;
		Boolean flag2 = true;
		for (Book b : SalesHistory.get().getListPLPBooks().values()) {
			if (b.getTitle().equals(lineDivided[3])) {
				book = b;
				flag2 = false;
				if (!b.getIdentifier().contains(lineDivided[4])) {
					b.addIdentifier(lineDivided[4]);
				}
				if (b.getAuthor().equals("")) {
					b.setAuthor(lineDivided[6]);
				}
			}
		}
		if (flag2) {
			book = new Book(lineDivided[3], lineDivided[6], lineDivided[4]);
			SalesHistory.get().addBook(book);			
		}
		
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
		
		
		Sale sale = new Sale(channel, country, newFormat.format(actualDate), book, netUnitsSold, royaltyTypePLP, price, deliveryCost, revenuesPLP, currency);
		SalesHistory.get().addSale(sale);
	}
	
}
