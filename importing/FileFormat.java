package importing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import main.Book;
import main.Channel;
import main.SalesHistory;

//TODO Read https://stackoverflow.com/questions/21817816/java-reading-a-file-different-methods

/**This abstract class is meant to be extended by all classes that import raw data from a file, sales or FX. 
 * <br>It enforces an importData(String filePath) method which should perform the import from the file to the database.
 * @author crhm
 *
 */
public abstract class FileFormat {
	
	int firstLineOfData;
	int minLengthOfLine;
	SimpleDateFormat oldDateFormat;
	
	final SimpleDateFormat newDateFormat = new SimpleDateFormat("MMM yyyy");
	
	/**Imports the data found in the file, whose path (from src folder) + name + extension is the parameter filePath, into the 
	 * database. Performs that action differently depending on the IFileFormat implementation it is called on.
	 * @param filePath
	 */
	public abstract void importData(String filePath);
	
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
	
	/**Checks the database to see if the list of books managed by PLP contains a book whose title contains the bookTitle string (or vice versa) 
	 * (ignoring quotes and capitalisation), or which has this identifier. If it does, this is the book it returns.
	 * <br>If that book has no author info, it sets it the author passed as argument. 
	 * <br>If that book doesn't have the identifier passed as argument, it sets it the identifier passed as argument.
	 * <br>If there is not such a book in the list of existing PLP books, it creates one with the arguments provided and adds it to the database, 
	 * and returns this new book.
	 * @param bookTitle book title found in raw data for this sale
	 * @param author author found in raw data for this sale
	 * @param identifier identifier found in raw data for this sale
	 * @return the book to add to the sale to be imported
	 */
	protected Book obtainBook(String bookTitle, String author, String identifier) {
		Book book = null;
		Boolean flag2 = true;
		for (Book b : SalesHistory.get().getListPLPBooks().values()) {
			String existingBookTitle = b.getTitle().replace("\"", "").toLowerCase();
			String bookTitleFound = bookTitle.replace("\"", "").toLowerCase();
			if (existingBookTitle.contains(bookTitleFound) || bookTitleFound.contains(existingBookTitle) || b.getIdentifiers().contains(identifier)) {
				book = b;
				flag2 = false;
				if (!b.getIdentifiers().contains(identifier)) {
					b.addIdentifier(identifier);
				}
				if (b.getAuthor().equals("")) {
					b.setAuthor(author);
				}
			}
		}
		if (flag2) {
			book = new Book(bookTitle, author, identifier);
			SalesHistory.get().addBook(book);			
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
	
	/**Checks if the database contains a channel of the name provided; returns it if it does.
	 * If it does not, it creates one with that name and the fileFormat provided and adds it to the database.
	 * 
	 * @param channelName String that is the channel name
	 * @param fileFormat format to be associated with the channel if it is to be created
	 * @return the right channel for the fileformat
	 */
	protected Channel obtainChannel(String channelName, FileFormat fileFormat) {
		//Checks if the Createspace channel already exists in database; if not, creates it.
		Channel channel = null;
		Boolean flag1 = true;
		for (Channel ch : SalesHistory.get().getListChannels().values()) {
			if (ch.getName().equals(channelName)) {
				channel = ch;
				flag1 = false;
			}
		}
		if (flag1) {
			channel = new Channel(channelName, fileFormat);
			SalesHistory.get().addChannel(channel);
		}		
		return channel;
	}

}