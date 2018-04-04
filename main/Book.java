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

	//Get methods
	public String getTitle() {
		return title;
	}

	public Person getAuthor() {
		return author;
	}

	public Set<String> getIdentifiers() {
		return identifiers;
	}

	/**
	 * @return the bookNumber unique to this book
	 */
	public long getBookNumber() {
		return bookNumber;
	}

	/**
	 * @return the Set of all acceptable titles for this book
	 */
	public Set<String> getListTitles() {
		return listTitles;
	}

	public double getTotalUnitsSold() {
		return totalUnitsSold;
	}

	/**
	 * @return the set of all authors for this book
	 */
	public Set<Person> getListAuthors() {
		return listAuthors;
	}

	//Add methods
	/**Adds identifier to Book's list of identifiers
	 * @throws IllegalArgumentException if identifier is empty or null
	 */
	public void addIdentifier(String identifier)	{
		validateIdentifier(identifier);
		this.identifiers.add(identifier);
	}

	/**Adds the argument passed to the total of units sold for this book
	 * @param unitsSold amount by which to increase the total of units sold
	 */
	public void addUnitsToTotalSold(double unitsSold) {
		this.totalUnitsSold = this.totalUnitsSold + unitsSold;
	}

	public void addTitle(String s) {
		validateTitle(s);
		//If somehow, there is no main title yet
		if (title.isEmpty()) {
			title = s;
		}
		listTitles.add(s);
	}

	/**Will throw an exception is person p is null.
	 * Will make p the main author if there is none.
	 * @param p the author to add to the list of authors
	 */
	public void addAuthor(Person p) {
		//If somehow, there is no main author yet
		if (author == null) {
			author = p;
		}
		listAuthors.add(p);
	}

	//Set methods (needed for editing)
	/**
	 * @param listTitles the listTitles to set
	 */
	public void setListTitles(Set<String> listTitles) {
		this.listTitles = listTitles;
		//TODO should i make one of those the main title here?
	}

	/**
	 * @param listAuthors the listAuthors to set
	 */
	public void setListAuthors(Set<Person> listAuthors) {
		this.listAuthors = listAuthors;
		//TODO should i make one of those the main author here?
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
		this.listTitles.add(title);
	}

	/**Sets the book's author as the person passed as argument
	 * @param author
	 */
	public void setAuthor(Person author) {
		this.author = author;
		this.listAuthors.add(author);
	}

	/**
	 * @param identifiers the identifiers to set
	 */
	public void setIdentifiers(Set<String> identifiers) {
		this.identifiers = identifiers;
	}

	//Merge method
	/**To merge two books into one //TODO fix problem of left over book number?
	 * <br>Adds all of b's titles, authors and identifiers to this book.
	 * @param b Book which will merged into this one.
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

	//Validate methods
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

	//Generated methods
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
