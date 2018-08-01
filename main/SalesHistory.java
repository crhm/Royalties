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

import main.royalties.IRoyaltyType;

/** Main class of this app; a singleton, since it should never be more than one. method get() returns singleton instance.
 * <br><br>This class acts as a container of data which holds:
 * <br>-the complete list of sales, regardless of channel, as an ArrayList<Sale>, because there is no point in retrieving an 
 *  individual sale and some channels don't provide a unique identifier that could be used as a key.
 * <br>-the complete list of royalty holders, regardless of channel, as a HashMap where the keys are Person names and the values are Persons.
 * <br>-the list of books managed by PLP, as a set of books.
 * <br>-the list of channels that books are sold on, as a set of channels. 
 * <br>-the list of authors, as a set of persons.
 * <br>-the list of months for which there have been sales, as a set of strings (representing the date in format MMM yyyy).
 * <br>-the list of royalties for each book that has the same royalties across all channels.
 * <br>-the next bookNumber and personNumber to be assigned to the next created book or person for the purposes of identification.
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
	private Set<Channel> listChannels = new HashSet<Channel>();
	private Set<Book> listPLPBooks = new HashSet<Book>();
	private Set<Person> listAuthors = new HashSet<Person>();
	private Set<Person> listPersons = new HashSet<Person>();
	private Set<String> listMonths = new HashSet<String>();

	private AtomicLong nextBookID = new AtomicLong(1);
	private AtomicLong nextPersonID = new AtomicLong(1);
	
	private List<String> importedFiles = new ArrayList<String>();


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

	/**Returns the person from list of persons with the personNumber passed as argument, or null if there is no such person
	 * @param personNumber the personNumber of the person to be retrieved
	 * @return the person with the personNumber passed as argument, or null if there is no such person
	 */
	public Person getPersonWithNumber(Long personNumber) {
		Person person = null;
		for (Person p : listPersons) {
			if (p.getPersonNumber() == personNumber) {
				person = p;
			}
		}
		return person;
	}

	/**Returns a book from listPLPBooks
	 * <br>Note: if there are more than one book with such title in the list of Books, it will return the first one it encounters.
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

	/**Returns the book from list of Books with the bookNumber passed as argument, or null if there is no such book
	 * @param number the bookNumber of the book to be retrieved
	 * @return the book with the bookNumber passed as argument, or null if there is no such book
	 */
	public Book getBookWithNumber(Long number) {
		Book book = null;
		for (Book b : listPLPBooks) {
			if (b.getBookNumber() == number) {
				book = b;
			}
		}
		return book;
	}

	/**Returns the channel with that name
	 * @param channelName String that is the name of the channel to be retrieved
	 * @return the channel with that name, or null if there is none.
	 */
	public Channel getChannel(String channelName) {
		Channel channel = null;
		for (Channel ch : listChannels) {
			if (ch.getName().equals(channelName)) {
				channel = ch;
			}
		}
		return channel;
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
		Set<Person> listAuthors = new HashSet<Person>();
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
		this.listAuthors = listAuthors;
		return listAuthors;
	}

	/** Returns the list of persons (authors or royalty holders) in SalesHistory.
	 * @return the listPersons
	 */
	public Set<Person> getListPersons() {
		return listPersons;
	}

	/**Returns the List of all sales that have been added to the app.
	 * @return the list of all sales that have been added to the app.
	 */
	public List<Sale> getSalesHistory() {
		return salesHistory;
	}

	/** Compiles and returns the complete list of royalty holders, regardless of channel, 
	 * as a Set of persons.
	 * @return the complete list of royalty holders, regardless of channel.
	 */
	public Set<Person> getListRoyaltyHolders() {
		Set<Person> listRoyaltyHolders = new HashSet<Person>();
		for (Channel ch : listChannels) {
			for (Book b : ch.getListRoyalties().keySet()) {
				for (Person p : ch.getListRoyalties().get(b).keySet()) {
					listRoyaltyHolders.add(p);
				}
			}
		}
		return listRoyaltyHolders;
	}

	/** Returns the complete list of books managed by PLP, 
	 * as a Set of Books.
	 * @return the complete list of books managed by PLP.
	 */
	public Set<Book> getListPLPBooks() {
		return listPLPBooks;
	}

	/**Calls UniformRoyalties.check(Book b) on all books, to see if they have royalties that are the same across channels, and returns
	 *  the mapping of those books to their royalties (and sets uniformRoyalties to it).
	 * @return the uniformRoyalties
	 */
	public HashMap<Book, HashMap<Person, IRoyaltyType>> getUniformRoyalties(){
		HashMap<Book, HashMap<Person, IRoyaltyType>> uniformRoyalties = new HashMap<Book, HashMap<Person, IRoyaltyType>>();
		for (Book b : listPLPBooks) {
			if (UniformRoyalties.check(b)) {
				uniformRoyalties.put(b, SalesHistory.get().getChannel("Amazon").getListRoyalties().get(b));
			}
		}
		return uniformRoyalties;
	}

	/** Returns the list of channels through which PLP sells books,
	 *  as a set of Channels.
	 * @return the list of channels through which PLP sells books
	 */
	public Set<Channel> getListChannels() {
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

	/** Adds a sale to the list of sales
	 * @param sale Sale to add to the app.
	 */
	public void addSale(Sale sale) {
		this.salesHistory.add(sale);	
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
		this.listChannels.add(channel);
	}

	//Remove methods
	/**Removes a book from the list of books managed by PLP, and from any channel's royalty list.
	 * @param book the Book to remove from the app
	 */
	public void removeBook(Book book) {
		for (Channel c : listChannels) {
			c.replaceBook(book, null);
		}
		this.listPLPBooks.remove(book);
	}

	/**Removes a person from the list of persons, from being listed as an author anywhere, from having any royalties, 
	 * and from being listed as a royalty holder
	 * @param person the Person to remove from the app
	 */
	public void removePerson(Person person) {
		for (Book b : listPLPBooks) {
			if (b.getAuthor1() == person) {
				b.setAuthor1(null);
			}
			if (b.getAuthor2() == person) {
				b.setAuthor2(null);
			}
			if (b.getTranslator() == person) {
				b.setTranslator(null);
			}
			if (b.getPrefaceAuthor() == person) {
				b.setPrefaceAuthor(null);
			}
			if (b.getAfterwordAuthor() == person) {
				b.setPrefaceAuthor(null);
			}
		}
		for (Channel c : listChannels) {
			for (Book b : c.getListRoyalties().keySet()) {
				if (c.getListRoyalties().get(b).containsKey(person)) {
					c.deleteRoyalty(b, person);
				}
			}
		}

		this.listPersons.remove(person);
	}

	//Replace methods
	/**This method replaces all references to oldPerson (as an author and as a royalty holder) so that they become references to newPerson.
	 * Useful when merging two persons (which results in the deletion of one person as all its attributes are passed onto the other).
	 * @param oldPerson
	 * @param newPerson
	 */
	public void replacePerson(Person oldPerson, Person newPerson) {
		//Replacing amongst authors
		for (Book b : SalesHistory.get().getListPLPBooks()) {
			if (b.getAuthor1() == oldPerson) {
				b.setAuthor1(newPerson);
			}
			if (b.getAuthor2() == oldPerson) {
				b.setAuthor2(newPerson);
			}
			if (b.getTranslator() == oldPerson) {
				b.setTranslator(newPerson);
			}
			if (b.getPrefaceAuthor() == oldPerson) {
				b.setPrefaceAuthor(newPerson);
			}
			if (b.getAfterwordAuthor() == oldPerson) {
				b.setAfterwordAuthor(newPerson);
			}
		}

		//Replacing amongst channel royalty lists.
		for (Channel ch : listChannels) {
			ch.replaceRoyaltyHolder(oldPerson, newPerson);
		}
	}

	/**Replaces a book by another in all the places it may occur (sales, and royalty lists, both general and channel specific).
	 * Royalties of old book are added to that of newBook unless the royaltyholder already exists in newBook.
	 * @param oldBook
	 * @param newBook
	 */
	public void replaceBook(Book oldBook, Book newBook) {
		for (Sale s : salesHistory) {
			if (s.getBook() == oldBook) {
				s.setBook(newBook);
			}
		}

		for (Channel ch : listChannels) {
			ch.replaceBook(oldBook, newBook);
		}
	}

	/**
	 * @return the importedFiles
	 */
	public List<String> getImportedFiles() {
		return importedFiles;
	}
	
	public void addImportedFile(String fileName) {
		this.importedFiles.add(fileName);
	}

	//	SERIALISATION METHODS
	/**Writes SalesHistory state to the ObjectOutputStream.
	 * <br>Writes by serialising the following SalesHistory variables (in this order, same as read by readObject()):
	 * listChannels, listPLPBooks, salesHistory, listAuthors, listPersons, listMonths, nextBookID, nextPersonID, importedFiles
	 * @param out ObjectOutputStream to write to.
	 * @throws IOException
	 */
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		try {
			out.writeObject(listChannels);
			out.writeObject(listPLPBooks);
			out.writeObject(salesHistory);
			out.writeObject(listAuthors);
			out.writeObject(listPersons);
			out.writeObject(listMonths);
			out.writeObject(nextBookID);
			out.writeObject(nextPersonID);
			out.writeObject(importedFiles);
			out.close();
		} catch (IOException i) {
			i.printStackTrace();
		}

	}

	/**Reads SalesHistory state from the ObjectInputStream.
	 * <br>Expects to find (in same order as written by writeObject()):
	 * listChannels, listPLPBooks, salesHistory, listAuthors, listPersons, listMonths, nextBookID, nextPersonID, importedFiles.
	 * <br><br>It then assigns these to the corresponding class variables of the singleton instance of SalesHistory.
	 * @param in ObjectInputStream to read from.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		try {
			Set<Channel> listChannels = (Set<Channel>) in.readObject();
			Set<Book> listPLPBooks = (Set<Book>) in.readObject();
			List<Sale> salesHistory = (List<Sale>) in.readObject();
			Set<Person> listAuthors = (Set<Person>) in.readObject();
			Set<Person> listPersons = (Set<Person>) in.readObject();
			Set<String> listMonths = (Set<String>) in.readObject();
			AtomicLong nextBookID = (AtomicLong) in.readObject();
			AtomicLong nextPersonID = (AtomicLong) in.readObject();
			List<String> importedFiles = (List<String>) in.readObject();
			in.close();
			this.listChannels = listChannels;
			this.listPLPBooks = listPLPBooks;
			this.salesHistory = salesHistory;
			this.listAuthors = listAuthors;
			this.listPersons = listPersons;
			this.listMonths = listMonths;
			this.nextBookID = nextBookID;
			this.nextPersonID = nextPersonID;
			this.importedFiles = importedFiles;
		} catch (IOException i) {
			i.printStackTrace();
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
		}
	}

	/**Serialises SalesHistory by calling its custom writeObject() method, and outputs it to a file called "/tmp/data.ser"
	 */
	public void serialise() { 
		try {
			FileOutputStream fileOut = new FileOutputStream("/tmp/data40.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			SalesHistory.get().writeObject(out);
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**Deserialises SalesHistory from a file called "/tmp/data.ser" by calling its custom readObject() method.
	 */
	public void deSerialise() {
		try {
			FileInputStream fileIn = new FileInputStream("/tmp/data40.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			SalesHistory.get().readObject(in);
			fileIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
