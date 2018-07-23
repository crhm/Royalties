package main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;

import importing.SalesFileFormat;
import main.royalties.IRoyaltyType;

/**Class designed to represent the different channels through which PLP sells books, e.g. Apple, Amazon, Nook, Kobo, Createspace...
 * <br>A Channel has a name, an import File Format it is associated with, a list of royalties, since it can vary per channel for 
 * the same book and the same royalty holder, and a list of historical FX rates, since they vary per channel.
 * <br>The list of royalties maps books to a list of persons associated with the type of royalty they hold for that book.
 * <br>The import file Format must be an implementation of the FileFormat interface, and can be changed at runtime.
 * <br>This class allows the user to add a royalty to the list of royalties through the addRoyalty method.
 * <br>The list of FX rates is per month, as a HashMap where the keys are a string representation of the month and year in the format of 
 * "Oct 2017", and the values are HashMaps mapping currency codes (e.g. "EUR") to exchange rates into US Dollars (as doubles).
 * <br>The method addHistoricalForex() allows one to add a list of FX rates for a certain month to the main list of historical rates.
 * <br>Its saleCurrencyIsAlwaysUSD variable is a boolean reflecting whether or not all monetary values in all sales through that channel are in USD. 
 * This is used to determine whether or not to use historical forexes to calculate royalties.
 * @author crhm
 *
 */
public class Channel implements java.io.Serializable {

	private static final long serialVersionUID = 6186357366182288547L;
	private final List<SalesFileFormat> listSalesFileFormats = new ArrayList<SalesFileFormat>();
	private final String name;
	private final List<String> listNames = new ArrayList<String>();
	private final HashMap<Book, HashMap<Person, IRoyaltyType>> listRoyalties = new HashMap<Book, HashMap<Person, IRoyaltyType>>();
	private final HashMap<String, HashMap<String, Double>> historicalForex = new HashMap<String, HashMap<String, Double>>();
	private final Boolean saleCurrencyIsAlwaysUSD;

	/**Channel constructor. Initialises channel name to the corresponding argument passed by user.
	 * <br>Allows one to set the value of saleCurrencyIsAlwaysUSD.
	 * @param name String name of channel
	 * @param saleCurrencyIsAlwaysUSD whether or not, for that channel, currency will always be USD
	 * @throws IllegalArgumentException if field takes an unpermitted value (name is null, empty, or already taken).
	 */
	public Channel(String name, Boolean saleCurrencyIsAlwaysUSD) {
		validateName(name);
		this.name = name;
		this.saleCurrencyIsAlwaysUSD = saleCurrencyIsAlwaysUSD;
		this.listNames.add(name);
		this.listNames.add(name.toLowerCase());
		this.listNames.add(name.toUpperCase());
	}

	/**Returns a boolean reflecting whether or not all monetary values in all sales through that channel are in USD.
	 * <br>Use to determine whether or not to use historical forexes to calculate royalties.
	 * @return true if all monetary values in all sales through that channel are in USD, false if some may not be
	 */
	public Boolean getSaleCurrencyIsAlwaysUSD() {
		return saleCurrencyIsAlwaysUSD;
	}

	/**
	 * @return the listSalesFileFormats
	 */
	public List<SalesFileFormat> getListSalesFileFormats() {
		return listSalesFileFormats;
	}
	
	/**
	 * @param salesFileFormat the SalesFileFormat to add to this channel's list of SalesFileFormats
	 */
	public void addSalesFileFormat(SalesFileFormat salesFileFormat) {
		this.listSalesFileFormats.add(salesFileFormat);
	}

	public String getName() {
		return name;
	}

	/**
	 * @return the listNames
	 */
	public List<String> getListNames() {
		return listNames;
	}

	
	public void addName(String name) {
		this.listNames.add(name);
		this.listNames.add(name.toLowerCase());
		this.listNames.add(name.toUpperCase());
	}
	
	/** Returns the list of royalties of that channel, as a HashMap mapping books to Hashmaps 
	 * which map Persons to the type of Royalty that they hold. Hence each book may have several 
	 * royalty holders with each a different type of royalty.
	 * @return the list of royalties of that channel, as a HashMap mapping books to Hashmaps 
	 * which map Persons to the type of Royalty that they hold.
	 */
	public HashMap<Book, HashMap<Person, IRoyaltyType>> getListRoyalties() {
		return listRoyalties;
	}
	
	/**Adds a royalty to the list of royalties of this channel.
	 * <br>If book is not in SalesHistory's list of books, it is added to it.
	 * <br>If SalesHistory's list of royalty holders has no one by royaltyHolderName, it creates and adds one.
	 * <br>Warning: If a royalty already exists for that royalty holder for that book, it will be replaced by the new one.
	 * @param b Book for which the royalty is held
	 * @param royaltyHolder Person which holds the royalty
	 * @param royalty royalty type that is held by the person for this book
	 * @throws IllegalArgumentException if book b is null or has an empty title, or royaltyHolderName is empty
	 */
	public void addRoyalty(Book b, Person royaltyHolder, IRoyaltyType royalty) {
		//Argument validation
		validateBook(b);
		if (royaltyHolder == null) {
			throw new IllegalArgumentException("Error: royaltyHolder cannot be null.");
		}

		//Obtains the list of royalties for this book if one exists, or creates an empty one if not
		HashMap<Person, IRoyaltyType> listHolder = null;
		if (listRoyalties.containsKey(b)) {
			listHolder = listRoyalties.get(b);
		} else {
			listHolder = new HashMap<Person, IRoyaltyType>();
		}		
		
		//Adds the royalty holder + royalty combination to the list of royalties, and links the book to this list of royalties
		listHolder.put(royaltyHolder, royalty);
		this.listRoyalties.put(b, listHolder);
	}

	/** Returns the list of historical Foreign Exchange rates for different currencies into US Dollars,
	 *  as a HashMap mapping a String representing a Month and Year (following the format "Oct 2017") to 
	 *  the list of FX rates for that month, which is itself a HashMap mapping currency codes (following the format 
	 *  "EUR") with the exchange rate into dollars, as a double.
	 * @return the list of historical Foreign Exchange rates for different currencies into US Dollars
	 */
	public HashMap<String, HashMap<String, Double>> getHistoricalForex() {
		return historicalForex;
	}

	/** Adds a list of Foreign Exchange rates for different currencies into US Dollars, associated with the month and year 
	 *  that it corresponds to, to the app's list of historical FX rates.
	 *  Warning: if one already exists, the old value will be replaced.
	 * @param monthAndYear String representing the month and year (following the format "Oct 2017")
	 * @param listForex HashMap mapping currency codes (a String following the format "EUR") with the exchange rate into dollars (a double).
	 * @throws IllegalArgumentException if monthAndYear is empty, null, or incorrect format, or listFX is empty, null or incorrect keys.
	 */
	public void addHistoricalForex(String monthAndYear, HashMap<String, Double> listForex) {
		validateDate(monthAndYear);
		validateListForex(listForex);
		this.historicalForex.put(monthAndYear, listForex);
	}

	/**Replaces one royalty holder by another (keeping the same royalties)
	 * @param oldPerson
	 * @param newPerson
	 */
	public void replaceRoyaltyHolder(Person oldPerson, Person newPerson) {
		/*For each book in listRoyalties, if it has oldPerson has a royaltyHolder, then create a new HashMap (newMappings) with the same mappings
		 * as the old one except for oldPerson, which is now a mapping of newPerson to whatever royalty oldPerson was mapped to.
		 * Hold this new HashMap in a HashMap called booksToUpdate which can be traversed outside of this for loop later on 
		 * (to avoid issues related to modifying something we are traversing).
		 */
		HashMap<Book, HashMap<Person, IRoyaltyType>> booksToUpdate = new HashMap<Book, HashMap<Person, IRoyaltyType>>();
		for (Book b : listRoyalties.keySet()) {
			if (listRoyalties.get(b).keySet().contains(oldPerson)) {
				HashMap<Person, IRoyaltyType> newMappings = new HashMap<Person, IRoyaltyType>();
				for (Person person : listRoyalties.get(b).keySet()) {
					if (person == oldPerson) {
						newMappings.put(newPerson, listRoyalties.get(b).get(person));
					} else {
						newMappings.put(person, listRoyalties.get(b).get(person));
					}
				}
				booksToUpdate.put(b, newMappings);
			}
		}
		for (Book b : booksToUpdate.keySet()) {
			listRoyalties.remove(b);
			listRoyalties.put(b, booksToUpdate.get(b));
		}
	}

	/**Removes the mapping of royalties for oldBook, and adds its royalties to the list mapped by newBook, unless the royaltyHolder preexists there, 
	 * in which case it does not add it (thus keeping the royalty as it was for newBook originally).
	 * If newBook is null, oldDook will be removed from the channel's list of royalties altogether.
	 * @param oldBook
	 * @param newBook
	 */
	public void replaceBook(Book oldBook, Book newBook) {
		HashMap<Person, IRoyaltyType> oldRoyalties = listRoyalties.get(oldBook);
		HashMap<Person, IRoyaltyType> newRoyalties = listRoyalties.get(newBook);

		if (oldRoyalties != null) {
			if (newRoyalties == null) {
				newRoyalties = new HashMap<Person, IRoyaltyType>();
			}
			//adding oldbook's royalties to that of newbook (unless a royalty holder already has a royalty in newBook)
			for (Person p : oldRoyalties.keySet()) {
				newRoyalties.putIfAbsent(p, oldRoyalties.get(p));
			}
		}
		listRoyalties.remove(oldBook);
		if (newRoyalties != null) {
			listRoyalties.put(newBook, newRoyalties);
		}
	}

	/**Removes the royalty mapping for the person passed as argument for the book passed as argument.
	 * If the book subsequently has no royalties at all for that channel, then the book is removed from the list of Royalties
	 * @param book
	 * @param person
	 */
	public void deleteRoyalty(Book book, Person person) {
		HashMap<Person, IRoyaltyType> newRoyaltiesMapping = listRoyalties.get(book);
		newRoyaltiesMapping.remove(person);
		if (newRoyaltiesMapping.isEmpty()) {
			listRoyalties.remove(book);
		} else {
			listRoyalties.replace(book, newRoyaltiesMapping);
		}
	}

	@Override
	public String toString() {
		return "Channel [name=" + name + "]";
	}


	/**Channel name must be non empty and non null, and another channel with the same name must not already exist in SalesHistory.
	 * @throws IllegalArgumentException if field takes an unpermitted value.
	 * @param name
	 */
	private void validateName(String name) {
		if (SalesHistory.get().getChannel(name) != null) {
			throw new IllegalArgumentException("Error: another channel with that name already exists.");
		}

		boolean nameHasContent = (name != null) && (!name.equals(""));
		if (!nameHasContent){
			throw new IllegalArgumentException("Names must be non-null and non-empty.");
		}
	}

	/**Checks that monthAndYear is a string representing a valid date of the format 'Oct 2018', and is 
	 * neither null nor empty.
	 * @param monthAndYear
	 * @throws IllegalArgumentException if field takes an unpermitted value.
	 */
	private void validateDate(String monthAndYear) {
		boolean dateHasContent = (monthAndYear != null) && (!monthAndYear.equals(""));
		if (!dateHasContent){
			throw new IllegalArgumentException("Dates (monthAndYear) must be non-null and non-empty.");
		}

		SimpleDateFormat format = new SimpleDateFormat("MMM yyyy");
		try {
			format.parse(monthAndYear);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Error: Date (monthAndYear) must be of the following format: Oct 2018, Jan 2009, ...");
		}
	}

	/**Checks that the HashMap is not empty or null, that keys are valid currency codes, and that no exchange rate is 0.
	 * 
	 * @param listForex
	 * @throws IllegalArgumentException if field takes an unpermitted value.
	 */
	private void validateListForex(HashMap<String, Double> listForex) {
		if (listForex == null || listForex.isEmpty()) {
			throw new IllegalArgumentException("Error: listForex cannot be empty or null.");
		}
		for (String s : listForex.keySet()) {
			if (s.isEmpty() || s == null) {
				throw new IllegalArgumentException("Error: Currency codes must not be empty or null.");
			}
			try {
				Currency.getInstance(s);
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("Error: Currency codes must be a valid ISO 4217 three letter code such as USD or EUR");
			}
		}
		for (Double d : listForex.values()) {
			if (d == 0) {
				throw new IllegalArgumentException("Error: no exchange rate can be 0");
			}
		}
	}

	/**Checks that book is not null nor has an empty title
	 * 
	 * @param b
	 * @throws IllegalArgumentException if field takes an unpermitted value.
	 */
	private void validateBook(Book b) {
		if (b ==null || b.getTitle().isEmpty()) {
			throw new IllegalArgumentException("Error: book cannot be null, or have an empty title");
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((saleCurrencyIsAlwaysUSD == null) ? 0 : saleCurrencyIsAlwaysUSD.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Channel other = (Channel) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (saleCurrencyIsAlwaysUSD == null) {
			if (other.saleCurrencyIsAlwaysUSD != null)
				return false;
		} else if (!saleCurrencyIsAlwaysUSD.equals(other.saleCurrencyIsAlwaysUSD))
			return false;
		return true;
	}



}
