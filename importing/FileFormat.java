package importing;

import main.Book;
import main.SalesHistory;

//TODO Read https://stackoverflow.com/questions/21817816/java-reading-a-file-different-methods

/**This interface is meant to be implemented by all classes that import raw data from a file, sales or FX. 
 * <br>It enforces an importData(String filePath) method which should perform the import from the file to the database.
 * @author crhm
 *
 */
public abstract class FileFormat {
	
	/**Imports the data found in the file, whose path (from src folder) + name + extension is the parameter filePath, into the 
	 * database. Performs that action differently depending on the IFileFormat implementation it is called on.
	 * @param filePath
	 */
	public abstract void importData(String filePath);
	
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

}
