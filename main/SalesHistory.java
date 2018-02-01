package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SalesHistory {
	
	private static final SalesHistory instance = new SalesHistory();
	
	private SalesHistory() {}
	
	public static SalesHistory get() {
		return instance;
	}
	
	private final HashMap<Book, Double> cumulativeSalesPerBook = new HashMap<Book, Double>();
	private final List<Sale> salesHistory = new ArrayList<Sale>();
	private final HashMap<String, Person> listRoyaltyHolders = new HashMap<String, Person>();
	private final HashMap<String, Book> listPLPBooks = new HashMap<String, Book>();
	private final HashMap<String, Channel> listChannels = new HashMap<String, Channel>();
	private final HashMap<String, HashMap<String, Double>> historicalForex = new HashMap<String, HashMap<String, Double>>();
	
	public HashMap<Book, Double> getCumulativeSalesPerBook() {
		return cumulativeSalesPerBook;
	}

	public void calculateAllRoyalies() {
		for (Sale s : salesHistory) {
			s.calculateRoyalties();
		}
	}

	public List<Sale> getSalesHistory() {
		return salesHistory;
	}
	
	public void addSale(Sale sale) {
		this.salesHistory.add(sale);	//TODO Fix, identifier won't work	
		if (!this.cumulativeSalesPerBook.keySet().contains(sale.getBook())) {
			this.cumulativeSalesPerBook.put(sale.getBook(), sale.getNetUnitsSold());
		}
		double unitsToAdd = sale.getNetUnitsSold();
		double oldTotal = cumulativeSalesPerBook.get(sale.getBook());
		this.cumulativeSalesPerBook.put(sale.getBook(), unitsToAdd + oldTotal);
	}

	public HashMap<String, Person> getListRoyaltyHolders() {
		return listRoyaltyHolders;
	}
	
	public void addRoyaltyHolder(Person royaltyHolder) {
		this.listRoyaltyHolders.put(royaltyHolder.getName(), royaltyHolder);
	}

	public HashMap<String, Book> getListPLPBooks() {
		return listPLPBooks;
	}
	
	public void addBook(Book book) {
		this.listPLPBooks.put(book.getTitle(), book);
	}

	public HashMap<String, Channel> getListChannels() {
		return listChannels;
	}
	
	public void addChannel(Channel channel) {
		this.listChannels.put(channel.getName(), channel);
	}

	public HashMap<String, HashMap<String, Double>> getHistoricalForex() {
		return historicalForex;
	}
	
	public void addHistoricalForex(String monthAndYear, HashMap<String, Double> listForex) {
		this.historicalForex.put(monthAndYear, listForex);
	}

}
