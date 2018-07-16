package main;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;

import main.royalties.IRoyaltyType;

/**Class which represents a sale of a book managed by PLP through one of its channels. 
 * <br>A Sale has a channel of sale, a country of sale, a date of sale, a book sold, 
 * a net number of units sold, a royalty percentage to be applied to the price to obtain PLP revenues on sale,
 *  an item price, a delivery cost to be deducted from the price before calculating PLP revenue, 
 *  a PLP revenue for the sale, and a currency which price, delivery cost and revenue are in.
 *  <br>This class is where the details of royalties calculation happen (see the caculateRoyalties() method).
 * @author crhm
 *
 */
public class Sale implements java.io.Serializable {

	private static final long serialVersionUID = -3585377913984089914L;
	private final Channel channel;
	private final String country;
	private final String date;
	private Book book;
	private final double netUnitsSold;
	private final double royaltyTypePLP;
	private final double price;
	private final double deliveryCost;
	private final double revenuesPLP;
	private final Currency currency;
	private Boolean royaltyHasBeenCalculated = false;

	/**Sale constructor. Initialises all variables as the arguments passed by user.
	 * @param channel Channel through which the sale was made. Must be one recognised by SalesHistory.
	 * @param country Format is not fixed for this at this stage. Can be null or empty.
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
	public Sale(Channel channel, String country, String date, Book book, double netUnitsSold, 
			double royaltyTypePLP, double price, double deliveryCost, double revenuesPLP, Currency currency) {
		validateChannel(channel);
		validateDate(date);
		validateBook(book);
		validateCurrency(currency);
		book.addUnitsToTotalSold(netUnitsSold);
		this.channel = channel;
		this.country = country;
		this.date = date;
		this.book = book;
		this.netUnitsSold = netUnitsSold;
		BigDecimal tempRoyaltyType = new BigDecimal(royaltyTypePLP).setScale(2, RoundingMode.HALF_UP);
		this.royaltyTypePLP = tempRoyaltyType.doubleValue();
		BigDecimal tempPrice = new BigDecimal(price).setScale(2, RoundingMode.HALF_UP);
		this.price = tempPrice.doubleValue();
		BigDecimal tempDeliveryCost = new BigDecimal(deliveryCost).setScale(2, RoundingMode.HALF_UP);
		this.deliveryCost = tempDeliveryCost.doubleValue();
		BigDecimal tempRevenuesPLP = new BigDecimal(revenuesPLP).setScale(2, RoundingMode.HALF_UP);
		this.revenuesPLP = tempRevenuesPLP.doubleValue();
		this.currency = currency;
	}
	
	/**Sale constructor. Initialises corresponding variables as the arguments passed by user, 
	 *  and royaltyTypePLP, price and deliveryCost to -1.
	 * @param channel Channel through which the sale was made. Must be one recognised by SalesHistory.
	 * @param date String representing month and year following the format "Oct 2017"
	 * @param book Item sold. Must be a book managed by PLP.
	 * @param netUnitsSold May be negative to represent returns / refunds.
	 * @param revenuesPLP Should be (Price - deliverCost) * netUnitsSold * royaltyPLP. (Will be rounded Half Up to two decimal places).
	 * @param currency Because of need to calculate balances in USD if the sale was in a foreign currency
	 * @throws IllegalArgumentException if arguments are null, or empty strings, or violate above stated rules.
	 */
	public Sale(Channel channel, String country, String date, Book book, double netUnitsSold, double revenuesPLP, Currency currency) {
		validateChannel(channel);
		validateDate(date);
		validateBook(book);
		validateCurrency(currency);
		book.addUnitsToTotalSold(netUnitsSold);
		this.channel = channel;
		this.country = "";
		this.date = date;
		this.book = book;
		this.netUnitsSold = netUnitsSold;
		this.royaltyTypePLP = -1;
		this.price = -1;
		this.deliveryCost = -1;
		BigDecimal tempRevenuesPLP = new BigDecimal(revenuesPLP).setScale(2, RoundingMode.HALF_UP);
		this.revenuesPLP = tempRevenuesPLP.doubleValue();
		this.currency = currency;
	}
	
	/**Sale constructor. Initialises all variables as the arguments passed by user, and revenuesPLP 
	 * to be (Price - deliveryCost) * netUnitsSold * royaltyPLP. (Will be rounded Half Up to two decimal places).
	 * @param channel Channel through which the sale was made. Must be one recognised by SalesHistory.
	 * @param country Format is not fixed for this at this stage. (can be empty string or null)
	 * @param date String representing month and year following the format "Oct 2017"
	 * @param book Item sold. Must be a book managed by PLP.
	 * @param netUnitsSold May be negative to represent returns / refunds.
	 * @param royaltyTypePLP Percentage of channel revenue that PLP gets per sale. (Will be rounded Half Up to two decimal places).
	 * @param price May be 0. Always without tax. Use offer price if there is one rather than full price. 
	 * (Will be rounded Half Up to two decimal places).
	 * @param deliveryCost May be 0. (Will be rounded Half Up to two decimal places).
	 * @param currency Because of need to calculate balances in USD if the sale was in a foreign currency
	 * @throws IllegalArgumentException if arguments are null, or empty strings, or violate above stated rules.
	 */
	public Sale(Channel channel, String country, String date, Book book, double netUnitsSold, 
			double royaltyTypePLP, double price, double deliveryCost, Currency currency) {
		validateChannel(channel);
		validateDate(date);
		validateBook(book);
		validateCurrency(currency);
		book.addUnitsToTotalSold(netUnitsSold);
		this.channel = channel;
		this.country = country;
		this.date = date;
		this.book = book;
		this.netUnitsSold = netUnitsSold;
		BigDecimal tempRoyaltyType = new BigDecimal(royaltyTypePLP).setScale(2, RoundingMode.HALF_UP);
		this.royaltyTypePLP = tempRoyaltyType.doubleValue();
		BigDecimal tempPrice = new BigDecimal(price).setScale(2, RoundingMode.HALF_UP);
		this.price = tempPrice.doubleValue();
		BigDecimal tempDeliveryCost = new BigDecimal(deliveryCost).setScale(2, RoundingMode.HALF_UP);
		this.deliveryCost = tempDeliveryCost.doubleValue();
		double tempRevenuesPLP = (this.price - this.deliveryCost) * this.netUnitsSold * this.royaltyTypePLP;
		BigDecimal roundedRevenuesPLP = new BigDecimal(tempRevenuesPLP).setScale(2, RoundingMode.HALF_UP);
		this.revenuesPLP = roundedRevenuesPLP.doubleValue();
		this.currency = currency;
	}

	public Channel getChannel() {
		return channel;
	}

	public String getCountry() {
		return country;
	}

	public String getDate() {
		return date;
	}

	public Book getBook() {
		return book;
	}

	/**Should only be called when there is a book merge...
	 * @param book
	 */
	public void setBook(Book book) {
		this.book = book;
	}

	public double getNetUnitsSold() {
		return netUnitsSold;
	}

	public double getRoyaltyTypePLP() {
		return royaltyTypePLP;
	}

	public double getPrice() {
		return price;
	}

	public double getDeliveryCost() {
		return deliveryCost;
	}

	public double getRevenuesPLP() {
		return revenuesPLP;
	}

	public Currency getCurrency() {
		return currency;
	}

	/**Indicates whether the calculateRoyalties() method has been called on this sale before or not.
	 * 
	 * @return true if royalties have been calculated for this sale, false if not.
	 */
	public Boolean getRoyaltyHasBeenCalculated() {
		return royaltyHasBeenCalculated;
	}

	/**Calculates royalties for this sale and adds the amounts due to each royalty holder's balance.
	 * <br>As all balances are in USD but some sales from Amazon and Apple are not, FX rates are applied 
	 * where appropriate.
	 * <br>Will print an error message if it cannot find an exchange rate or a list of royalties for a sale.
	 * <br>Only calculates royalties for each sale once; if a sale's getRoyaltyHasBeenCalculated() returns true, 
	 * it will print an error message and do nothing else.
	 */
	public void calculateRoyalties() {
		if (!royaltyHasBeenCalculated) {
			//Get exchange rate
			double exchangeRate = 0;
			try {
				if (channel.getSaleCurrencyIsAlwaysUSD()) {
					exchangeRate = 1;
				} else {
					exchangeRate = channel.getHistoricalForex().get(date).get(currency.getCurrencyCode());
				}
			} catch (NullPointerException e) {
				System.out.println("There was a problem getting the exchange rate of this sale: " + this);
			}

			//Calculate Royalty
			try {
				if (SalesHistory.get().getUniformRoyalties().get(book) != null) {
					for (Person p : SalesHistory.get().getUniformRoyalties().get(book).keySet()) {
						IRoyaltyType royalty = SalesHistory.get().getUniformRoyalties().get(book).get(p);
						double amount = royalty.getAmountDue(revenuesPLP * exchangeRate, book.getTotalUnitsSold());
						p.addToBalance(amount);
						this.royaltyHasBeenCalculated = true;
					}
				} else {
					for (Person p : channel.getListRoyalties().get(book).keySet()) {
						IRoyaltyType royalty = channel.getListRoyalties().get(book).get(p);
						double amount = royalty.getAmountDue(revenuesPLP * exchangeRate, book.getTotalUnitsSold());
						p.addToBalance(amount);
						this.royaltyHasBeenCalculated = true;
					}
				}
			} catch (NullPointerException e) {
				System.out.println("There was a problem getting the royalty list for this sale: " + this);
			}
		}
	}

	@Override
	public String toString() {
		return "Sale [channel=" + channel + ", country=" + country + ", date=" + date + ", book=" + book
				+ ", netUnitsSold=" + netUnitsSold + ", royaltyTypePLP=" + royaltyTypePLP
				+ ", price=" + price + ", deliveryCost=" + deliveryCost + ", revenuesPLP=" + revenuesPLP + ", currency=" + currency 
				+ ", Royalties have been calculated=" + royaltyHasBeenCalculated + "]";
	}

	/**Checks that channel is not null and belongs to SalesHistory's list of channels
	 * @throws IllegalArgumentException if field takes unauthorised value
	 */
	private void validateChannel(Channel channel) {
		if (channel == null) {
			throw new IllegalArgumentException("Error: channel cannot be null");
		}
		if (!SalesHistory.get().getListChannels().contains(channel)) {
			throw new IllegalArgumentException("Error: channel should belong to the list of channels in SalesHistory");
		}
	}

	/**Checks that book is not null, and that it is on the list of books managed by PLP in SalesHistory
	 * @throws IllegalArgumentException if field takes unauthorised value
	 */
	private void validateBook(Book book) {
		if (book == null) {
			throw new IllegalArgumentException("Error: book cannot be null");
		}
		if (!SalesHistory.get().getListPLPBooks().contains(book)) {
			throw new IllegalArgumentException("Error: book is not on the list of books managed by PLP in SalesHistory");
		}
	}

	/**Checks that currency is not null
	 * @throws IllegalArgumentException if field takes unauthorised value
	 */
	private void validateCurrency(Currency currency) {
		if (currency == null) {
			throw new IllegalArgumentException("Error: currency cannot be null");
		}
	}

	/**Checks date is not null or empty, and that it follows the "Oct 2018" format.
	 * @throws IllegalArgumentException if field takes unauthorised value
	 */
	private void validateDate(String date) {
		if (date == null || date.isEmpty()){
			throw new IllegalArgumentException("Error: date cannot be empty or null");
		}

		SimpleDateFormat format = new SimpleDateFormat("MMM yyyy");
		try {
			format.parse(date);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Error: date must be of format Oct 2018");
		}
	}
}
