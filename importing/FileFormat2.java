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
import main.ObjectFactory;
import main.Person;
import main.SalesHistory;

public class FileFormat2 {

	private int firstLineOfData;
	private int minLengthOfLine;
	private SimpleDateFormat oldDateFormat;
	private final SimpleDateFormat newDateFormat = new SimpleDateFormat("MMM yyyy");
	private int dateColumnIndex;
	private int dateRowIndex;
	private Channel channel;

	private ObjectToImport bookTitleSettings = null;
	private ObjectToImport bookAuthorSettings = null;
	private ObjectToImport bookIDSettings = null;
	private ObjectToImport netUnitsSoldSettings = null;
	private ObjectToImport revenuesPLPSettings = null;
	private ObjectToImport priceSettings = null;
	private ObjectToImport royaltyTypePLPSettings = null;
	private ObjectToImport deliveryCostSettings = null;
	private ObjectToImport dateSettings = null;
	private ObjectToImport currencySettings = null;
	private ObjectToImport countrySettings = null;

	/**
	 * @param firstLineOfData
	 * @param minLengthOfLine
	 * @param oldDateFormat
	 * @param channel
	 * @param bookTitleSettings
	 * @param bookAuthorSettings
	 * @param bookIDSettings
	 * @param netUnitsSoldSettings
	 * @param revenuesPLPSettings
	 * @param priceSettings
	 * @param royaltyTypePLPSettings
	 * @param deliveryCostSettings
	 * @param dateSettings
	 * @param currencySettings
	 * @param countrySettings
	 */
	public FileFormat2(int firstLineOfData, int minLengthOfLine, SimpleDateFormat oldDateFormat, Channel channel,
			ObjectToImport bookTitleSettings, ObjectToImport bookAuthorSettings, ObjectToImport bookIDSettings, ObjectToImport netUnitsSoldSettings,
			ObjectToImport revenuesPLPSettings, ObjectToImport priceSettings, ObjectToImport royaltyTypePLPSettings,
			ObjectToImport deliveryCostSettings, ObjectToImport dateSettings, ObjectToImport currencySettings,
			ObjectToImport countrySettings) {
		this.firstLineOfData = firstLineOfData;
		this.minLengthOfLine = minLengthOfLine;
		this.oldDateFormat = oldDateFormat;
		this.channel = channel;
		this.bookTitleSettings = bookTitleSettings;
		this.bookAuthorSettings = bookAuthorSettings;
		this.bookIDSettings = bookIDSettings;
		this.netUnitsSoldSettings = netUnitsSoldSettings;
		this.revenuesPLPSettings = revenuesPLPSettings;
		this.priceSettings = priceSettings;
		this.royaltyTypePLPSettings = royaltyTypePLPSettings;
		this.deliveryCostSettings = deliveryCostSettings;
		this.dateSettings = dateSettings;
		this.currencySettings = currencySettings;
		this.countrySettings = countrySettings;
	}

	/**Imports the data found in the file, whose path (from src folder) + name + extension is the parameter filePath, into the 
	 * app. Performs that action differently depending on the IFileFormat implementation it is called on.
	 * @param filePath
	 */
	public void importData(String filePath) {
		String[] allLines = readFile(filePath);

		//Parses data for each sale and imports it by calling importSale on each sales line of csv
		//Stops if counter reaches the total number of lines of csv, or if the line is shorter than minLengthOfLine,
		//so that it doesn't try to parse the summary lines below the last sale line.
		int counter = firstLineOfData;
		while (counter< allLines.length && allLines[counter].length() > minLengthOfLine) {
			importSale(allLines[counter]);
			counter++;
		}
	}

	private void importSale(String line) {
		//TODO - this is very simplified version, make it as full as possible
		
		//Divides sales line into its individual cells by splitting on commas that are not within quotes
		//And trims all leading and trailing whitespace from each value.
		String[] lineDivided = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
		int counter = 0;
		for (String s : lineDivided) {
			lineDivided[counter] = s.trim();
			counter++;
		}

		String date = obtainDate(dateSettings.getFormattedString(lineDivided[dateSettings.getColumnIndex()]));

		String bookTitle = bookTitleSettings.getFormattedString(lineDivided[bookTitleSettings.getColumnIndex()]);
		String bookAuthor = bookAuthorSettings.getFormattedString(lineDivided[bookAuthorSettings.getColumnIndex()]);
		String bookID = bookIDSettings.getFormattedString(lineDivided[bookIDSettings.getColumnIndex()]);

		Book book = obtainBook(bookTitle, bookAuthor, bookID);

		double netUnitsSold = Double.parseDouble(netUnitsSoldSettings.getFormattedString(lineDivided[netUnitsSoldSettings.getColumnIndex()]));

		double price = Double.parseDouble(priceSettings.getFormattedString(lineDivided[priceSettings.getColumnIndex()]));

		double deliveryCost = Double.parseDouble(deliveryCostSettings.getFormattedString(lineDivided[deliveryCostSettings.getColumnIndex()]));

		double revenuesPLP = Double.parseDouble(revenuesPLPSettings.getFormattedString(lineDivided[revenuesPLPSettings.getColumnIndex()]));

		double royaltyTypePLP = Double.parseDouble(royaltyTypePLPSettings.getFormattedString(lineDivided[royaltyTypePLPSettings.getColumnIndex()]));

		Currency currency = Currency.getInstance(currencySettings.getFormattedString(lineDivided[currencySettings.getColumnIndex()]));

		String country = countrySettings.getFormattedString(lineDivided[countrySettings.getColumnIndex()]);

		//Creates the sale and adds its to the app
		ObjectFactory.createSale(channel, country, date, book, netUnitsSold, royaltyTypePLP, price, deliveryCost, revenuesPLP, currency);
	}

	/** Reads file whose name and path is passed as argument, and takes each line and puts it in an array of strings which it returns.
	 * 
	 * @param filePath name and path of file to be read 
	 * @return array of string where each element is a line of the file
	 */
	protected String[] readFile(String filePath) {
		String[] allLines = null;
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
			allLines = temp.split("\n");
		} catch (IOException e) {
			System.out.println("There was an error reading this file.");
			e.printStackTrace();
		}
		return allLines;
	}

	/**Checks the app to see if the list of books managed by PLP contains a book whose title contains the bookTitle string (or vice versa) 
	 * (ignoring quotes and capitalisation), or which has this identifier. If it does, this is the book it returns.
	 * <br>If that book has no author info, it sets it the author passed as argument. 
	 * <br>If that book doesn't have the identifier passed as argument, it sets it the identifier passed as argument.
	 * <br>If there is not such a book in the list of existing PLP books, it creates one with the arguments provided and adds it to the app, 
	 * and returns this new book.
	 * @param bookTitle book title found in raw data for this sale
	 * @param author author found in raw data for this sale
	 * @param identifier identifier found in raw data for this sale
	 * @return the book to add to the sale to be imported
	 */
	protected Book obtainBook(String bookTitle, String authorName, String identifier) {
		Book book = null;
		Boolean needToCreateNewBook = true;
		for (Book b : SalesHistory.get().getListPLPBooks()) {
			String existingBookTitle = b.getTitle().toLowerCase();
			String bookTitleFound = bookTitle.replace("\"", "").toLowerCase();
			if (existingBookTitle.equals(bookTitleFound) || b.getIdentifiers().contains(identifier)) {
				book = b;
				needToCreateNewBook = false;
				if (!identifier.isEmpty()) {
					b.addIdentifier(identifier);
				}
				if (book.getAuthor1() == null && !authorName.isEmpty()) {
					Person author = null;
					if (SalesHistory.get().getPerson(authorName) != null) {
						author = SalesHistory.get().getPerson(authorName);
					} else {
						author = ObjectFactory.createPerson(authorName);
					}
					book.setAuthor1(author);
				}
			}
		}
		if (needToCreateNewBook) {
			book = ObjectFactory.createBook(bookTitle);
			Person author = null;
			if (SalesHistory.get().getPerson(authorName) != null) {
				author = SalesHistory.get().getPerson(authorName);
			} else if (!authorName.isEmpty()){
				author = ObjectFactory.createPerson(authorName);
			}
			book.setAuthor1(author);
			book.addIdentifier(identifier);
		}
		return book;
	}

	/** Passed a string which it expects to be in oldDateFormat, it returns the correct date in newDateFormat.
	 * Throws a ParseException if the string passed as argument is not of the oldDateFormat
	 * @param oldDate String that contains the date to be parsed and modified
	 * @return String that is the date in the correct format
	 */
	protected String obtainDate(String oldDate){
		Date date = null;
		try {
			date = oldDateFormat.parse(oldDate);
		} catch (ParseException e) {
			System.out.println("There was parsing the date. A date of the format " + oldDateFormat.toPattern() + " was expected.");
			e.printStackTrace();
		}
		return newDateFormat.format(date);
	}

	/**Checks if the app contains a channel of the name provided; returns it if it does.
	 * If it does not, it creates one with that name and the fileFormat provided and adds it to the app.
	 * 
	 * @param channelName String that is the channel name
	 * @param fileFormat format to be associated with the channel if it is to be created
	 * @param isCurrencyAlwaysUSD boolean representing whether or not all monetary amounts in all sales for that channel are USD
	 * @return the right channel for the fileformat
	 */
	protected Channel obtainChannel(String channelName, FileFormat fileFormat, boolean isCurrencyAlwaysUSD) {
		//Checks if the Createspace channel already exists in app; if not, creates it.
		Channel channel = null;
		Boolean needToCreateNewChannel = true;
		for (Channel ch : SalesHistory.get().getListChannels()) {
			if (ch.getName().equals(channelName)) {
				channel = ch;
				needToCreateNewChannel = false;
			}
		}
		if (needToCreateNewChannel) {
			channel = ObjectFactory.createChannel(channelName, fileFormat, isCurrencyAlwaysUSD);
		}		
		return channel;
	}

}
