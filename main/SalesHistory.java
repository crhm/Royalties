package main;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/** Main class of this app; a singleton, since it should never be more than one. method get() returns singleton instance.
 * <br><br>This class acts as a container of data which holds:
 * <br>-the complete list of sales, regardless of channel, as an ArrayList<Sale>, because there is no point in retrieving an 
 *  individual sale and some channels don't provide a unique identifier that could be used as a key.
 * <br>-the complete list of royalty holders, regardless of channel, as a HashMap where the keys are Person names and the values are Persons.
 * <br>-the list of books managed by PLP, as a HashMap where the keys are Book titles and the values are Books.
 * <br>-the list of channels that books are sold on, as a HashMap where the keys are Channel names and the values are Channels. 
 *  <br><br>This class also calculates all royalties (by doing so sale by sale, see Sale.calculateRoyalties()).
 *  <br>This class allows the user to add a sale, a royalty holder, a book, or a channel to the app.
 * @author crhm
 *
 */
public class SalesHistory implements java.io.Serializable { 

	private static final long serialVersionUID = 2532726647603526773L; //BUT careful: it's a singleton... 

	private static final SalesHistory instance = new SalesHistory();

	private SalesHistory() {}

	/** Method to obtain the SalesHistory singleton.
	 * @return SalesHistory singleton instance
	 */
	public static SalesHistory get() {
		return instance;
	}

	private List<Sale> salesHistory = new ArrayList<Sale>();
	private HashMap<String, Person> listRoyaltyHolders = new HashMap<String, Person>();
	private HashMap<String, Channel> listChannels = new HashMap<String, Channel>();
	private Set<Book> listPLPBooks = new HashSet<Book>();
	private Set<Person> listAuthors = new HashSet<Person>();
	private Set<Person> listPersons = new HashSet<Person>();
	private Set<String> listMonths = new HashSet<String>();
	//TODO think of getting rid of all Hashmaps for sets instead?
	
	private AtomicLong nextBookID = new AtomicLong(1);
	private AtomicLong nextPersonID = new AtomicLong(1);
		
	//GET IDS FOR OBJECT CREATION
	/** 
	 * @return the ID number to be assigned to the next book being created.
	 */
	public long getNextBookID() {
		return nextBookID.getAndIncrement();
	}
	
	/**
	 * @return the ID number to be assigned to the next person being created
	 */
	public long getNextPersonID() {
		return nextPersonID.getAndIncrement();
	}
		
	//GET A SPECIFIC OBJECT	
	/**Returns a person from listPersons
	 * @param personName the name of the person to be retrieved
	 * @return person with the name passed as argument, or null if there is no such person
	 */
	public Person getPerson(String personName) {
		Person personFound = null;
		for (Person p : listPersons) {
			if (p.getListNames().contains(personName)) {
				personFound = p;
			}
		}
		return personFound;
	}
	
	/**Returns a book from listPLPBooks
	 * @param title the title of the book to be retrieved
	 * @return the book with a title as the one passed as argument, or null if there is no such book
	 */
	public Book getBook(String title) {
		Book bookFound = null;
		for (Book b : listPLPBooks) {
			if (b.getListTitles().contains(title)) {
				bookFound = b;
			}
		}
		return bookFound;
	}
	
	//CALCULATE ROYALTIES
	/** Calculates all royalties by calling Sale.calculateRoyalties() on each sale
	 *  in the list of all sales.
	 */
	public void calculateAllRoyalies() {
		for (Sale s : salesHistory) {
			s.calculateRoyalties();
		}
	}
	
	//GET A LIST
	/**Returns a list of the months for which there are sales.
	 * This list is compiled upon request, and not updated gradually as new sales are added.
	 * @return A list of strings representing months and years
	 */
	public Set<String> getListMonths(){
		for (Sale s : salesHistory) {
			if (!listMonths.contains(s.getDate())){
				listMonths.add(s.getDate());
			}
		}
		return listMonths;
	}

	/**Returns a list of the unique authors found in the list of books.
	 * This list is compiled upon request, and not updated gradually as new books are added.
	 * Included are main authors, secondary authors, translators, preface authors and afterword authors.
	 * @return A list of string names of authors
	 */
	public Set<Person> getListAuthors(){
		for (Book b : listPLPBooks) {
			if (b.getAuthor1() != null && !listAuthors.contains(b.getAuthor1())) {
				listAuthors.add(b.getAuthor1());
			}
			if (b.getAuthor2() != null && !listAuthors.contains(b.getAuthor2())) {
				listAuthors.add(b.getAuthor2());
			}
			if (b.getTranslator() != null && !listAuthors.contains(b.getTranslator())) {
				listAuthors.add(b.getTranslator());
			}
			if (b.getPrefaceAuthor() != null && !listAuthors.contains(b.getPrefaceAuthor())) {
				listAuthors.add(b.getPrefaceAuthor());
			}
			if (b.getAfterwordAuthor() != null && !listAuthors.contains(b.getAfterwordAuthor())) {
				listAuthors.add(b.getAfterwordAuthor());
			}	
		}
		return listAuthors;
	}
	
	/**Returns the List of all sales that have been added to the app.
	 * @return the list of all sales that have been added to the app.
	 */
	public List<Sale> getSalesHistory() {
		return salesHistory;
	}
	
	/** Returns the complete list of royalty holders, regardless of channel, 
	 * as a HashMap mapping Person names to Persons.
	 * @return the complete list of royalty holders, regardless of channel.
	 */
	public HashMap<String, Person> getListRoyaltyHolders() {
		return listRoyaltyHolders;
	}
	
	/** Returns the complete list of books managed by PLP, 
	 * as a Set of Books.
	 * @return the complete list of books managed by PLP.
	 */
	public Set<Book> getListPLPBooks() {
		return listPLPBooks;
	}
	
	/** Returns the list of channels through which PLP sells books,
	 *  as a HashMap mapping Channel names to Channels.
	 * @return the list of channels through which PLP sells books
	 */
	public HashMap<String, Channel> getListChannels() {
		return listChannels;
	}
	
	//ADD AN OBJECT TO A LIST. SHOULD BE CALLED ONLY FROM OBJECTFACTORY (EXCEPT FOR ROYALTYHOLDER)
	/**Adds someone to the list of persons created. 
	 * 
	 * @param person
	 */
	public void addPerson(Person person) {
		this.listPersons.add(person);
	}
	
	/** Adds a sale to the app.
	 * <br>First places it in the list of all sales, then updates the book's total number of units sold appropriately.
	 * @param sale Sale to add to the app.
	 */
	public void addSale(Sale sale) {
		this.salesHistory.add(sale);	
	}

	/** Adds a Person to the complete list of Royalty holders
	 * @param royaltyHolder Person to add to the complete list of Royalty holders
	 */
	public void addRoyaltyHolder(Person royaltyHolder) {
		this.listRoyaltyHolders.put(royaltyHolder.getName(), royaltyHolder);
	}

	/** Adds a book to the list of Books managed by PLP
	 * @param book the Book to add to the list of Books managed by PLP.
	 */
	public void addBook(Book book) {
		this.listPLPBooks.add(book);
	}

	/** Adds a Channel to the list of channels through which PLP sells books
	 * @param channel
	 */
	public void addChannel(Channel channel) {
		this.listChannels.put(channel.getName(), channel);
	}
	
	//Remove methods
	/**Removes a book from the list of books managed by PLP
	 * @param book the Book to remove from the list of Books managed by PLP
	 */
	public void removeBook(Book book) {
		this.listPLPBooks.remove(book);
	}
	
	/**Removes a person from the list of persons
	 * and adds its personNumber to the list of deleted persons
	 * @param person the Person to remove from the list of persons
	 */
	public void removePerson(Person person) {
		this.listPersons.remove(person);
	}

	//SERIALISATION METHODS //TODO fix so that it is updated for current state of things
//	/**Writes SalesHistory state to the ObjectOutputStream.
//	 * <br>Writes by serialising the following SalesHistory variables (in this order, same as read by readObject()):
//	 * <br>HashMap<Book, Double> cumulativeSalesPerBook
//	 * <br>HashMap<String, Channel> listChannels
//	 * <br>HashMap<String, Book> listPLPBooks
//	 * <br>HashMap<String, Person> listRoyaltyHolders
//	 * <br>List of Sales salesHistory
//	 * @param out ObjectOutputStream to write to.
//	 * @throws IOException
//	 */
//	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
//		try {
//			out.writeObject(listChannels);
//			out.writeObject(listPLPBooks);
//			out.writeObject(listRoyaltyHolders);
//			out.writeObject(salesHistory);
//			out.close();
//			System.out.println("Serialized data is saved.");
//		} catch (IOException i) {
//			i.printStackTrace();
//		}
//
//	}
//	
//	/**Reads SalesHistory state from the ObjectInputStream.
//	 * <br>Expects to find (in same order as written by writeObject()):
//	 * <br>HashMap<String, Channel> listChannels
//	 * <br>HashMap<String, Book> listPLPBooks
//	 * <br>HashMap<String, Person> listRoyaltyHolders
//	 * <br>List of Sales salesHistory
//	 * <br><br>It then assigns these to the corresponding class variables of the singleton instance of SalesHistory.
//	 * @param in ObjectInputStream to read from.
//	 * @throws IOException
//	 * @throws ClassNotFoundException
//	 */
//	@SuppressWarnings("unchecked")
//	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
//		try {
//			HashMap<String, Channel> listChannels = (HashMap<String, Channel>) in.readObject();
//			HashMap<String, Book> listPLPBooks = (HashMap<String, Book>) in.readObject();
//			HashMap<String, Person> listRoyaltyHolders = (HashMap<String, Person>) in.readObject();
//			List<Sale> salesHistory = (List<Sale>) in.readObject();
//			in.close();
//			this.listChannels = listChannels;
//			this.listPLPBooks = listPLPBooks;
//			this.listRoyaltyHolders = listRoyaltyHolders;
//			this.salesHistory = salesHistory;
//		} catch (IOException i) {
//			i.printStackTrace();
//		} catch (ClassNotFoundException c) {
//			c.printStackTrace();
//		}
//	}
	
//	/**Serialises SalesHistory by calling its custom writeObject() method, and outputs it to a file called "/tmp/data.ser"
//	 */
//	public void serialise() { //TODO make serialisation output be a filename with date and time? and then in deserialise choose filename with most recent date?
//		try {
//			FileOutputStream fileOut = new FileOutputStream("/tmp/data10.ser");
//			ObjectOutputStream out = new ObjectOutputStream(fileOut);
//			SalesHistory.get().writeObject(out);
//			fileOut.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**Deserialises SalesHistory from a file called "/tmp/data.ser" by calling its custom readObject() method.
//	 */
//	public void deSerialise() {
//		try {
//			FileInputStream fileIn = new FileInputStream("/tmp/data10.ser");
//			ObjectInputStream in = new ObjectInputStream(fileIn);
//			SalesHistory.get().readObject(in);
//			fileIn.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//	}

}
