package main;

import java.util.Currency;

public class Sale {

	private final Channel channel;
	private final String country; //Make an Enum for countries?
	private final String date; //change to a date format? //May be empty
	private final Book book;
	private final double netUnitsSold;
	private final double royaltyTypePLP;
	private final double price; //including offer if there be
	private final double deliveryCost;
	private final double revenuesPLP;
	private final Currency currency;
	
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

	//TODO fix this; doesn't work because it gets called on each sale when it only needs to be called once per channel.
	//Problem being that it needs to use info from sale...
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
