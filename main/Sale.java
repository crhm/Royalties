package main;

import java.util.Currency;

/**Class which represents a sale of a book managed by PLP through one of its channels. 
 * <br>A Sale has a channel of sale, a country of sale, a date of sale, a book sold, 
 * a net number of units sold, a royalty percentage to be applied to the price to obtain PLP revenues on sale,
 *  an item price, a delivery cost to be deducted from the price before calculating PLP revenue, 
 *  a PLP revenue for the sale, and a currency which price, delivery cost and revenue are in.
 *  <br>This class is where the details of royalties calculation happen (see the caculateRoyalties() method).
 * @author crhm
 *
 */
public class Sale {

	private final Channel channel;
	private final String country;
	private final String date;
	private final Book book;
	private final double netUnitsSold;
	private final double royaltyTypePLP;
	private final double price;
	private final double deliveryCost;
	private final double revenuesPLP;
	private final Currency currency;
	
	//TODO enforce these assumptions!
	/**Sale constructor. Initialises all variables as the arguments passed by user.
	 * @param channel Channel through which the sale was made.
	 * @param country Format is not fixed for this at this stage.
	 * @param date String representing month and year following the format "Oct 2017"
	 * @param book Item sold. Must be a book managed by PLP.
	 * @param netUnitsSold May be negative to represent returns / refunds.
	 * @param royaltyTypePLP Percentage of channel revenue that PLP gets per sale.
	 * @param price May be 0. Always without tax. Use offer price if there is one rather than full price.
	 * @param deliveryCost May be 0.
	 * @param revenuesPLP Should be (Price - deliverCost) * netUnitsSold * royaltyPLP
	 * @param currency Need to calculate balances in USD if the sale was in a foreign currency
	 */
	public Sale(Channel channel, String country, String date, Book book, double netUnitsSold, 
			double royaltyTypePLP, double price, double deliveryCost, double revenuesPLP, Currency currency) {
		this.channel = channel;
		this.country = country;
		this.date = date;
		this.book = book;
		this.netUnitsSold = netUnitsSold;
		this.royaltyTypePLP = royaltyTypePLP;
		this.price = price;
		this.deliveryCost = deliveryCost;
		this.revenuesPLP = revenuesPLP;
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

	/**Calculates royalties for this sale and adds the amounts due to each royalty holder's balance.
	 * <br>As all balances are in USD but all sales are not, FX rates are applied using SalesHistory's 
	 * list of historical FX sales to find the correct one.
	 */
	public void calculateRoyalties() {
		double exchangeRate = SalesHistory.get().getHistoricalForex().get(date).get(currency.getCurrencyCode());
		for (Person p : channel.getListRoyalties().get(book).keySet()) {
			IRoyaltyType royalty = channel.getListRoyalties().get(book).get(p);
			double amount = royalty.getAmountDue(revenuesPLP * exchangeRate, SalesHistory.get().getCumulativeSalesPerBook().get(book));
			p.addToBalance(amount);
		}
	}

	@Override
	public String toString() {
		return "Sale [channel=" + channel + ", country=" + country + ", date=" + date + ", book=" + book
				+ ", netUnitsSold=" + netUnitsSold + ", royaltyTypePLP=" + royaltyTypePLP
				+ ", price=" + price + ", deliveryCost=" + deliveryCost + ", revenuesPLP=" + revenuesPLP + ", currency=" + currency + "]";
	}
	
}
