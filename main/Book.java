package main;

import java.util.HashSet;
import java.util.Set;

/** Class representing books managed by PLP and sold via different channels.
 * <br> A book has a main title, but also a list of titles (can be varied spelling, varied length...).
 *  The main title should be contained in the list of titles, and therefore neither can ever be null or empty.
 * <br>A book has an author (a Person) and a list of authors (in case of additional authors such as cover designer or translator for example), 
 * and the main author should be in the listAuthors, but both can be null.
 * <br>A book has a list of identifiers which are unique to this book, but whose format
 * depends on the channel through which it was added (usually a variation on ISBN). It may be empty but may not contain empty strings.
 * <br>It also keeps track of how many units of the book have been sold so far.
 * <br>A book is identified by its unique bookNumber, assigned at creation by SalesHistory.
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
	 * <br>Removes quote characters from title, and makes it the main title as well as adding it to the list of titles.
	 * <br>Author becomes the book's author (it may be null) and is added to the list of authors (if not null).
	 * <br>Creates an ArrayList of Strings and places identifier in it.
	 * <br>Initialises totalUnitsSold to 0.
	 * @param title String title of book (Cannot be empty, cannot be null)
	 * @param author Person to be the author (can be null)
	 * @param identifier String unique ID of book, e.g. ISBN, ISBN-13, e-ISBN or ASIN (Can be empty, cannot be null)
	 * @throws IllegalArgumentException if title is empty or null or if identifier is null.
	 */
	public Book(String title, Person author, String identifier) {
		this.bookNumber = SalesHistory.get().getNextBookID();
		validateTitle(title);
		if (identifier == null) {
			throw new IllegalArgumentException("Error: identifier may be empty but may not be null.");
		}
		this.title = title.replace("\"", "");
		this.listTitles.add(title);
		this.author = author;
		if (author != null) {
			this.listAuthors.add(author);
		}
		if (!identifier.isEmpty()) {
			this.identifiers.add(identifier);
		}
		this.totalUnitsSold = 0;
	}

	//GET METHODS
	/**
	 * @return main title only
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return main author only
	 */
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

	//ADD METHODS
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

	/**
	 * @param s title to add
	 * @throws IllegalArgumentException if title is empty or null
	 */
	public void addTitle(String title) {
		validateTitle(title);
		listTitles.add(title);
	}

	/**Will throw an exception is person p is null.
	 * Will make p the main author if there is none yet.
	 * @param p the author to add to the list of authors
	 * @throws IllegalArgumentException if p is null
	 */
	public void addAuthor(Person p) {
		if (p == null) {
			throw new IllegalArgumentException("Error: you cannot add null as an author.");
		}
		//If somehow, there is no main author yet
		if (author == null) {
			author = p;
		}
		listAuthors.add(p);
	}

	//SET METHODS (NEEDED FOR EDITING)
	/**
	 * @param listTitles the listTitles to set
	 * @throws IllegalArgumentException if any strings in listTitles are empty or null
	 */
	public void setListTitles(Set<String> listTitles) {
		for (String s : listTitles) {
			validateTitle(s);
		}
		this.listTitles = listTitles;
		//TODO should i make one of those the main title here?
		//No, enforce having the main title box filled in the editing window before it accepts the entries
		//and then add the main title entry to the set before passing the set as argument
	}

	/**
	 * @param listAuthors the listAuthors to set
	 */
	public void setListAuthors(Set<Person> listAuthors) {
		this.listAuthors = listAuthors;
		//TODO should i make one of those the main author here?
		//No, enforce having the main author box filled in the editing window before it accepts the entries
		//and then add the main author entry to the set before passing the set as argument
	}

	/**
	 * @param title the title to set
	 * @throws IllegalArgumentException if title is empty or null
	 */
	public void setTitle(String title) {
		validateTitle(title);
		this.title = title;
		this.listTitles.add(title);
	}

	/**Sets the book's author as the person passed as argument
	 * @param author
	 */
	public void setAuthor(Person author) {
		this.author = author;
		if (author != null) {
			this.listAuthors.add(author);
		}
	}

	/** Will remove empty strings from the set before setting it.
	 * @param identifiers the identifiers to set
	 */
	public void setIdentifiers(Set<String> identifiers) {
		for (String s : identifiers) {
			if (s.isEmpty()) {
				identifiers.remove(s);
			}
		}
		this.identifiers = identifiers;
	}

	//MERGE METHOD
	/**To merge two books into one
	 * <br>Adds all of b's titles, authors and identifiers to this book.
	 * <br>Removes b from list of PLP books
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
			this.addAuthor(p);
		}
		this.addUnitsToTotalSold(b.getTotalUnitsSold());
		SalesHistory.get().removeBook(b);
	}

	//VALIDATE METHODS
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

	//GENERATED METHODS
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
