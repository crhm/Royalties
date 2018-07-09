package main;

import java.util.Currency;

import importing.FileFormat;

public class ObjectFactory {
	
	/**Constructs a book and then adds it to SalesHistory.
	 * <br>All authors are initialised to null, no identifiers are added.
	 * <br><br>Book constructor removes quote characters from title.
	 * <br>Creates an ArrayList of Strings and places identifier in it.
	 * <br>Initialises totalUnitsSold to 0.
	 * @param title String title of book (Cannot be empty, cannot be null)
	 * @throws IllegalArgumentException if title is empty or null.
	 */
	public static Book createBook(String title) {
		Book book = new Book(title);
		SalesHistory.get().addBook(book);
		return book;
	}

	/**Constructs a person and adds them to SalesHistory.
	 * Person constructor initialises Person name to the String passed as argument by the user (removing quote characters), 
	 * and Person balance to 0.
	 * @param personName String name of Person. Cannot be empty or null.
	 * @throws IllegalArgumentException if person name is empty or null
	 */
	public static Person createPerson(String personName) {
		Person person = new Person(personName);
		SalesHistory.get().addPerson(person);
		return person;
	}

	/**Constructs a channel and then adds it to SalesHistory.
	 * Channel constructor initialises channel names and fileFormat to the corresponding arguments passed by user.
	 * <br>Initialises saleCurrencyIsAlwaysUSD to false by default. Use other constructor for channels that need it set to true.
	 * @param channelName String name of channel
	 * @param fileFormat FileFormat implementation to be associated with the channel
	 * @throws IllegalArgumentException if field takes an unpermitted value (name is null, empty, or already taken).
	 */
	public static Channel createChannel(String channelName, FileFormat channelFormat) {
		Channel channel = new Channel(channelName, channelFormat);
		SalesHistory.get().addChannel(channel);
		return channel;
	}

	/**Constructs a channel and then adds it to SalesHistory.
	 * Channel constructor initialises channel names and fileFormat to the corresponding arguments passed by user.
	 * <br>Initialises saleCurrencyIsAlwaysUSD to false by default. Use other constructor for channels that need it set to true.
	 * @param channelName String name of channel
	 * @param channelFormat FileFormat implementation to be associated with the channel
	 * @param isCurrencyAlwaysUSD determines whether there'll be need to look for exchange rates or not
	 * @throws IllegalArgumentException if field takes an unpermitted value (name is null, empty, or already taken).
	 */
	public static Channel createChannel(String channelName, FileFormat channelFormat, Boolean isCurrencyAlwaysUSD) {
		Channel channel = new Channel(channelName, channelFormat, isCurrencyAlwaysUSD);
		SalesHistory.get().addChannel(channel);
		return channel;
	}

	/**Constructs a sale and adds it to SalesHistory.
	 * Sale constructor initialises all variables as the arguments passed by user.
	 * @param channel Channel through which the sale was made. Must be one recognised by SalesHistory.
	 * @param country Format is not fixed for this at this stage.
	 * @param date String representing month and year following the format "Oct 2017"
	 * @param book Item sold. Must be a book managed by PLP.
	 * @param netUnitsSold May be negative to represent returns / refunds.
	 * @param royaltyTypePLP Percentage of channel revenue that PLP gets per sale. (Will be rounded Half Up to two decimal places).
	 * @param price May be 0. Always without tax. Use offer price if there is one rather than full price. 
	 * (Will be rounded Half Up to two decimal places).
	 * @param deliveryCost May be 0. (Will be rounded Half Up to two decimal places).
	 * @param revenuesPLP Should be (Price - deliverCost) * netUnitsSold * royaltyPLP. (Will be rounded Half Up to two decimal places).
	 * @param currency Because of need to calculate balances in USD if the sale was in a foreign currency
	 * @throws IllegalArgumentException if arguments are null, or empty strings, or violate above stated rules.
	 */
	public static Sale createSale(Channel channel, String country, String date, Book book, double netUnitsSold, 
			double royaltyTypePLP, double price, double deliveryCost, double revenuesPLP, Currency currency) {
		Sale sale = new Sale(channel, country, date, book, netUnitsSold, royaltyTypePLP, price, deliveryCost, revenuesPLP, currency);
		SalesHistory.get().addSale(sale);
		return sale;
	}

}
