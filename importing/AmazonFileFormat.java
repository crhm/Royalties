package importing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import main.Book;
import main.Channel;
import main.Sale;
import main.SalesHistory;

/**Class that represents the new (?) format for raw monthly sales data files from Amazon channel and performs the import of the data found
 *  in such files, through method importData(). It is an implementation of the IFileFormat interface.
 * @author crhm
 *
 */
public class AmazonFileFormat implements IFileFormat{

	private String date = "";
	
	/**Imports the sales data found in the raw monthly sales data file from Amazon channel into the database.
	 * <br>Reads the file and then performs data processing for each sale.
	 * <br>Expects to find the first sale on the third line of the csv.
	 * <br>Expects sale lines to be longer than 15 characters.
	 * <br>Obtains the date from the cell in the first line and second column of the csv.
	 * <br>Expects the date in the file name to be of the format 'October 2017'.
	 * <br>Expects prices to be in american number format (commas for thousands, full stop for decimals)
	 * <br>Expects values for 'Royalty Type' to be of the format '70%' rather than '0.7'
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
			
			//Obtains the date to give to all sales from the cell in the first line and second column of the csv
			//And formats it into the expected format.
			SimpleDateFormat oldFormat = new SimpleDateFormat("MMMMM yyyy");
			SimpleDateFormat newFormat = new SimpleDateFormat("MMM yyyy");
			Date date = null;
			String[] firstLine = allLines[0].split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
			String oldDate = firstLine[1];
			try {
				date = oldFormat.parse(oldDate);
			} catch (ParseException e) {
				System.out.println("There was parsing the date in the second cell of the first line of the csv."
						+ " A date of the format 'October 2017' was expected.");
				e.printStackTrace();
			}
			this.date = newFormat.format(date);
			
			//Parses data for each sale and imports it by calling importSale on each sales line of csv
			//Considers that the first line of sales is the third line of csv.
			//Stops if counter reaches the total number of lines of csv, or if the line is shorter than 15 characters,
			//so that it doesn't try to parse the summary lines below the last sale line.
			int counter = 2;
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
		
		//Checks if the Amazon channel already exists in database; if not, creates it.
		Channel channel = null;
		Boolean flag1 = true;
		for (Channel ch : SalesHistory.get().getListChannels().values()) {
			if (ch.getName().equals("Amazon")) {
				channel = ch;
				flag1 = false;
			}
		}
		if (flag1) {
			channel = new Channel("Amazon", new AmazonFileFormat());
			SalesHistory.get().addChannel(channel);
		}
		
		//Checks the column called 'Marketplace' for the last two characters, which it converts to upper case and assigns to
		//country, unless the value is '.com', in which case the country is "US".
		String country = "";
		if (lineDivided[3].endsWith(".com")) {
			country = "US";
		} else {
			int length = lineDivided[3].length();
			country = lineDivided[3].substring(length - 2, length).toUpperCase();
		}
		
		//Checks the database to see if a book of that title already exists in the list of books managed by PLP, and assigns it to 
		//the sale if there is. If there is not, it creates one with the title provided in the first column, the author provided in
		//the second column, and the ID provided in the third (ASIN), and adds it to the database, as well as assigning it to the sale.
		Book book = null;
		Boolean flag2 = true;
		for (Book b : SalesHistory.get().getListPLPBooks().values()) {
			if (b.getTitle().equals(lineDivided[0])) {
				book = b;
				flag2 = false;
			}
		}
		if (flag2) {
			book = new Book(lineDivided[0], lineDivided[1], lineDivided[2]);
			SalesHistory.get().addBook(book);			
		}
		
		//Assigns value of seventh column ('Net Units Sold') as the netUnitsSold
		int netUnitsSold = Integer.parseInt(lineDivided[6]);
		
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
		double deliveryCost = number2.doubleValue();
	   
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
		
		//Creates the sale and adds its to the database
		Sale sale = new Sale(channel, country, this.date, book, netUnitsSold, royaltyTypePLP, price, deliveryCost, revenuesPLP, currency);
		SalesHistory.get().addSale(sale);
	}

}
