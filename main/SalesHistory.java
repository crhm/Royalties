package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** Main class of this app; a singleton, since it should never be more than one. method get() returns singleton instance.
 * <br><br>This class acts as a container of data which holds:
 * <br>-the complete list of sales, regardless of channel, as an ArrayList<Sale>, because there is no point in retrieving an 
 *  individual sale and some channels don't provide a unique identifier that could be used as a key.
 * <br>-the complete list of royalty holders, regardless of channel, as a HashMap where the keys are Person names and the values are Persons.
 * <br>-the list of books managed by PLP, as a HashMap where the keys are Book titles and the values are Books.
 * <br>-the total number of units sold per book, as a HashMap where the keys are the Books and the values are the total units sold 
 *  (a double to allow for negative units aka returns).  
 * <br>-the list of channels that books are sold on, as a HashMap where the keys are Channel names and the values are Channels. 
 *  <br><br>This class also calculates all royalties (by doing so sale by sale, see Sale.calculateRoyalties()).
 *  <br>This class allows the user to add a sale, a royalty holder, a book, or a channel to the app.
 * @author crhm
 *
 */
public class SalesHistory { 
	
	private static final SalesHistory instance = new SalesHistory();
	
	private SalesHistory() {}
	
	/** Method to obtain the SalesHistory singleton.
	 * @return SalesHistory singleton instance
	 */
	public static SalesHistory get() {
		return instance;
	}
	
	private final HashMap<Book, Double> cumulativeSalesPerBook = new HashMap<Book, Double>();
	private final List<Sale> salesHistory = new ArrayList<Sale>();
	private final HashMap<String, Person> listRoyaltyHolders = new HashMap<String, Person>();
	private final HashMap<String, Book> listPLPBooks = new HashMap<String, Book>();
	private final HashMap<String, Channel> listChannels = new HashMap<String, Channel>();
	
	public HashMap<Book, Double> getCumulativeSalesPerBook() {
		return cumulativeSalesPerBook;
	}

	/** Calculates all royalties by calling Sale.calculateRoyalties() on each sale
	 *  in the list of all sales.
	 */
	public void calculateAllRoyalies() {
		for (Sale s : salesHistory) {
			s.calculateRoyalties();
		}
	}

	/**Returns the List of all sales that have been added to the app.
	 * @return the list of all sales that have been added to the app.
	 */
	public List<Sale> getSalesHistory() {
		return salesHistory;
	}
	
	/** Adds a sale to the app.
	 * <br>First places it in the list of all sales, then updates the list holding the total number of units sold 
	 * for each book by the appropriate number.
	 * @param sale Sale to add to the app.
	 */
	public void addSale(Sale sale) {
		this.salesHistory.add(sale);	
		if (!this.cumulativeSalesPerBook.keySet().contains(sale.getBook())) {
			this.cumulativeSalesPerBook.put(sale.getBook(), sale.getNetUnitsSold());
		}
		double unitsToAdd = sale.getNetUnitsSold();
		double oldTotal = cumulativeSalesPerBook.get(sale.getBook());
		this.cumulativeSalesPerBook.put(sale.getBook(), unitsToAdd + oldTotal);
	}
	
	/** Returns the complete list of royalty holders, regardless of channel, 
	 * as a HashMap mapping Person names to Persons.
	 * @return the complete list of royalty holders, regardless of channel.
	 */
	public HashMap<String, Person> getListRoyaltyHolders() {
		return listRoyaltyHolders;
	}
	
	/** Adds a Person to the complete list of Royalty holders
	 * @param royaltyHolder Person to add to the complete list of Royalty holders
	 */
	public void addRoyaltyHolder(Person royaltyHolder) {
		this.listRoyaltyHolders.put(royaltyHolder.getName(), royaltyHolder);
	}
	
	/** Returns the complete list of books managed by PLP, 
	 * as a HashMap mapping Book titles to Books.
	 * @return the complete list of books managed by PLP.
	 */
	public HashMap<String, Book> getListPLPBooks() {
		return listPLPBooks;
	}
	
	/** Adds a book to the list of Books managed by PLP
	 * @param book the Book to add to the list of Books managed by PLP.
	 */
	public void addBook(Book book) {
		this.listPLPBooks.put(book.getTitle(), book);
	}

	/** Returns the list of channels through which PLP sells books,
	 *  as a HashMap mapping Channel names to Channels.
	 * @return the list of channels through which PLP sells books
	 */
	public HashMap<String, Channel> getListChannels() {
		return listChannels;
	}
	
	/** Adds a Channel to the list of channels through which PLP sells books
	 * @param channel
	 */
	public void addChannel(Channel channel) {
		this.listChannels.put(channel.getName(), channel);
	}

}
