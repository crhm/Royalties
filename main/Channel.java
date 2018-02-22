package main;

import java.util.HashMap;

import importing.FileFormat;

/**Class designed to represent the different channels through which PLP sells books, e.g. Apple, Amazon, Nook, Kobo, Createspace...
 * <br>A Channel has a name, an import File Format it is associated with, a list of royalties, since it can vary per channel for 
 * the same book and the same royalty holder, and a list of historical FX rates, since they vary per channel.
 * <br>The list of royalties maps books to a list of persons associated with the type of royalty they hold for that book.
 * <br>The import file Format must be an implementation of the FileFormat interface, and can be changed at runtime.
 * <br>This class allows the user to add a royalty to the list of royalties through the addRoyalty method.
 * <br>The list of FX rates is per month, as a HashMap where the keys are a string representation of the month and year in the format of 
 * "Oct 2017", and the values are HashMaps mapping currency codes (e.g. "EUR") to exchange rates into US Dollars (as doubles).
 * <br>The method addHistoricalForex() allows one to add a list of FX rates for a certain month to the main list of historical rates.
 * @author crhm
 *
 */
public class Channel {
	
	private FileFormat fileFormat;
	private final String name;
	private final HashMap<Book, HashMap<Person, IRoyaltyType>> listRoyalties = new HashMap<Book, HashMap<Person, IRoyaltyType>>();
	private final HashMap<String, HashMap<String, Double>> historicalForex = new HashMap<String, HashMap<String, Double>>();
	private final Boolean saleCurrencyIsAlwaysUSD;

	//TODO print list royalties?
	
	/**Channel constructor. Initialises channel names and fileFormat to the corresponding arguments passed by user.
	 * <br>Initialises saleCurrencyIsAlwaysUSD to false by default. Use other constructor for channels that need it set to true.
	 * @param name String name of channel
	 * @param fileFormat FileFormat implementation to be associated with the channel
	 */
	public Channel(String name, FileFormat fileFormat) {
		this.name = name;
		this.fileFormat = fileFormat;
		this.saleCurrencyIsAlwaysUSD = false;
	}
	
	/**Channel constructor. Initialises channel names and fileFormat to the corresponding arguments passed by user.
	 * <br>Allows one to set the value of saleCurrencyIsAlwaysUSD to true, which simplifies royalties calculations.
	 * @param name String name of channel
	 * @param fileFormat FileFormat implementation to be associated with the channel
	 * @param saleCurrencyIsAlwaysUSD
	 */
	public Channel(String name, FileFormat fileFormat, Boolean saleCurrencyIsAlwaysUSD) {
		this.name = name;
		this.fileFormat = fileFormat;
		this.saleCurrencyIsAlwaysUSD = saleCurrencyIsAlwaysUSD;
	}

	/**Returns a boolean reflecting whether or not all monetary values in all sales through that channel are in USD.
	 * <br>Use to determine whether or not to use historical forexes to calculate royalties.
	 * @return true if all monetary values in all sales through that channel are in USD, false if some may not be
	 */
	public Boolean getSaleCurrencyIsAlwaysUSD() {
		return saleCurrencyIsAlwaysUSD;
	}

	public FileFormat getfileFormat() {
		return fileFormat;
	}

	public void setfileFormat(FileFormat fileFormat) {
		this.fileFormat = fileFormat;
	}

	public String getName() {
		return name;
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
	 * @param b Book for which the royalty is held
	 * @param royaltyHolder Person which holds the royalty
	 * @param royalty royalty type that is held by the person for this book
	 */
	public void addRoyalty(Book b, String royaltyHolderName, IRoyaltyType royalty) {
		HashMap<Person, IRoyaltyType> listHolder = null;
		if (listRoyalties.containsKey(b)) {
			listHolder = listRoyalties.get(b);
		} else {
			listHolder = new HashMap<Person, IRoyaltyType>();
		}
		Person royaltyHolder2 = null;
		Boolean flag = true;
		for (Person p : SalesHistory.get().getListRoyaltyHolders().values()) {
			if (p.getName().equals(royaltyHolderName)) {
				royaltyHolder2 = p;
				flag = false;
			}
		}
		if (flag) {
			royaltyHolder2 = new Person(royaltyHolderName);
			SalesHistory.get().addRoyaltyHolder(royaltyHolder2);
		}
		listHolder.put(royaltyHolder2, royalty);
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
	 * @param monthAndYear String representing the month and year (following the format "Oct 2017")
	 * @param listForex HashMap mapping currency codes (a String following the format "EUR") with the exchange rate into dollars (a double).
	 */
	public void addHistoricalForex(String monthAndYear, HashMap<String, Double> listForex) {
		this.historicalForex.put(monthAndYear, listForex);
	}
	
	@Override
	public String toString() {
		return "Channel [name=" + name + "]";
	}
	
	

}
