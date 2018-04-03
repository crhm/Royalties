package main;

import java.util.HashSet;
import java.util.Set;

/** Class representing books managed by PLP and sold via different channels.
 * <br> A book has a title, an author and an identifier which is unique but whose format
 * depends on the channel through which it was added (usually a variation on ISBN).
 * <br>It also keeps track of how many units of the book have been sold so far.
 * @author crhm
 *
 */
public class Book implements java.io.Serializable{

	private static final long serialVersionUID = 9050853882885242193L;
	private String title;
	private Set<String> listTitles = new HashSet<String>();
	private Person author;
	private Set<Person> listAuthors = new HashSet<Person>();
	private Set<String> identifiers = new HashSet<String>();
	private double totalUnitsSold;
	private final long bookNumber;
	
	/**Book constructor.
	 * <br>Removes quote characters from title and author arguments, and then initialises Book variables to them.
	 * <br>Creates an ArrayList of Strings and places identifier in it.
	 * <br>Initialises totalUnitsSold to 0.
	 * @param title String title of book (Cannot be empty, cannot be null)
	 * @param author String name of author (Can be empty, cannot be null)
	 * @param identifier String unique ID of book, e.g. ISBN, ISBN-13, e-ISBN or ASIN (Can be empty, cannot be null)
	 * @throws IllegalArgumentException if title is empty or any of the arguments are null.
	 */
	public Book(String title, Person author, String identifier) {
		this.bookNumber = SalesHistory.get().getNextBookID();
		validateTitle(title);
//		if (author == null || identifier == null) {
//			throw new IllegalArgumentException("Error: arguments cannot be null.");
//		} //TODO fix
		this.title = title.replace("\"", "");
		this.listTitles.add(title);
		this.author = author;
		if (author != null) {
			this.listAuthors.add(author);
		}
		this.identifiers = new HashSet<String>();
		if (!identifier.isEmpty()) {
			this.identifiers.add(identifier);
		}
		this.totalUnitsSold = 0;
	}

	/**
	 * @return the listTitles
	 */
	public Set<String> getListTitles() {
		return listTitles;
	}
	
	public void addTitle(String s) {
		listTitles.add(s);
	}
	
	public void addAuthor(Person p) {
		if (author == null) {
			author = p;
		}
		listAuthors.add(p);
	}

	/**
	 * @param listTitles the listTitles to set
	 */
	public void setListTitles(Set<String> listTitles) {
		this.listTitles = listTitles;
	}

	/**
	 * @return the listAuthors
	 */
	public Set<Person> getListAuthors() {
		return listAuthors;
	}

	/**
	 * @param listAuthors the listAuthors to set
	 */
	public void setListAuthors(Set<Person> listAuthors) {
		this.listAuthors = listAuthors;
	}

	/**
	 * @return the bookNumber
	 */
	public long getBookNumber() {
		return bookNumber;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param identifiers the identifiers to set
	 */
	public void setIdentifiers(Set<String> identifiers) {
		this.identifiers = identifiers;
	}

	public double getTotalUnitsSold() {
		return totalUnitsSold;
	}
	
	/**Adds the argument passed to the total of units sold for this book
	 * @param unitsSold amount by which to increase the total of units sold
	 */
	public void addUnitsToTotalSold(double unitsSold) {
		this.totalUnitsSold = this.totalUnitsSold + unitsSold;
	}

	public String getTitle() {
		return title;
	}

	/**Sets the book's author as the person passed as argument (can be empty but cannot be null)
	 * @throws IllegalArgumentException if author is null 
	 */
	public void setAuthor(Person author) {
//		if (author == null) {
//			throw new IllegalArgumentException("Error: author cannot be null.");
//		} //TODO fix
		this.author = author;
	}

	public Person getAuthor() {
		return author;
	}

	public Set<String> getIdentifiers() {
		return identifiers;
	}

	/**Adds identifier to Book's list of identifiers
	 * @throws IllegalArgumentException if identifier is empty or null
	 */
	public void addIdentifier(String identifier)	{
		validateIdentifier(identifier);
		this.identifiers.add(identifier);
	}
	
	/**Checks title is not empty or null.
	 * @throws IllegalArgumentException if field takes an unpermitted value.
	 */
	private void validateTitle(String title) {
		if (title == null || title.isEmpty()) {
			throw new IllegalArgumentException("Error: title cannot be empty or null");
		}
	}
	
	/**Checks identifier is not empty or null.
	 * @param identifier
	 * @throws IllegalArgumentException if field takes an unpermitted value.
	 */
	private void validateIdentifier(String identifier) {
		if (identifier == null || identifier.isEmpty()) {
			throw new IllegalArgumentException("Error: identifier cannot be empty or null");
		}
	}
	
	/**To merge two books into one //TODO fix problem of left over book number?
	 * 
	 * @param b
	 */
	public void merge(Book b) {
		for (String s : b.getListTitles()) {
			this.addTitle(s);
		}
		for (String s : b.getIdentifiers()) {
			this.identifiers.add(s);
		}
		for (Person p : b.getListAuthors()) {
			this.listAuthors.add(p);
		}
		this.addUnitsToTotalSold(b.getTotalUnitsSold());
	}

	@Override
	public String toString() {
		return "Book [title=" + title + ", author=" + author + ", identifier=" + identifiers + ", totalUnitsSold=" + totalUnitsSold +"]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (bookNumber ^ (bookNumber >>> 32));
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		if (bookNumber != other.bookNumber)
			return false;
		return true;
	}

	

}
