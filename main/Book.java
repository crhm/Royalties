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
	private Person author1;
	private Person author2;
	private Person translator;
	private Person prefaceAuthor;
	private Person afterwordAuthor;
	private Set<String> identifiers = new HashSet<String>();
	private double totalUnitsSold;
	private final long bookNumber;

	/**Book constructor.
	 * <br>Removes quote characters from title, and makes it the main title as well as adding it to the list of titles.
	 * <br>Author becomes the book's author1 (it may be null).
	 * <br>Author2, prefaceAuthor, afterwordAuthor and translator are initialised as null (see other constructor for options).
	 * <br>Creates an ArrayList of Strings and places identifier in it.
	 * <br>Initialises totalUnitsSold to 0.
	 * @param title String title of book (Cannot be empty, cannot be null)
	 * @param author Person to be the author1 (can be null)
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
		this.author1 = author;
		this.author2 = null;
		this.afterwordAuthor = null;
		this.prefaceAuthor = null;
		this.translator = null;
		if (!identifier.isEmpty()) {
			this.identifiers.add(identifier);
		}
		this.totalUnitsSold = 0;
	}
	
	/**Book constructor for detailed input of various authors.
	* <br>Removes quote characters from title, and makes it the main title as well as adding it to the list of titles.
	 * <br>Author1, author2, prefaceAuthor, afterwordAuthor and translator may be null.
	 * <br>Creates an ArrayList of Strings and places identifier in it.
	 * <br>Initialises totalUnitsSold to 0.
	 * @param title String title of book (Cannot be empty, cannot be null)
	 * @param author1 Main author (Person or null)
	 * @param author2 Secondary author  (Person or null)
	 * @param translator  (Person or null)
	 * @param prefaceAuthor  (Person or null)
	 * @param afterwordAuthor  (Person or null)
	 * @param identifier String unique ID of book, e.g. ISBN, ISBN-13, e-ISBN or ASIN (Can be empty, cannot be null)
	 * @throws IllegalArgumentException if title is empty or null or if identifier is null.
	 */
	public Book(String title, Person author1, Person author2, Person translator, Person prefaceAuthor, Person afterwordAuthor, String identifier) {
		this.bookNumber = SalesHistory.get().getNextBookID();
		validateTitle(title);
		if (identifier == null) {
			throw new IllegalArgumentException("Error: identifier may be empty but may not be null.");
		}
		this.title = title.replace("\"", "");
		this.listTitles.add(title);
		this.author1 = author1;
		this.author2 = author2;
		this.afterwordAuthor = afterwordAuthor;
		this.prefaceAuthor = prefaceAuthor;
		this.translator = translator;

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
	 * @return the author1
	 */
	public Person getAuthor1() {
		return author1;
	}
	
	/**
	 * @return the author2
	 */
	public Person getAuthor2() {
		return author2;
	}
	
	/**
	 * @return the translator
	 */
	public Person getTranslator() {
		return translator;
	}

	/**
	 * @return the prefaceAuthor
	 */
	public Person getPrefaceAuthor() {
		return prefaceAuthor;
	}

	/**
	 * @return the afterwordAuthor
	 */
	public Person getAfterwordAuthor() {
		return afterwordAuthor;
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
	 * @param author1
	 */
	public void setAuthor1(Person author) {
		this.author1 = author;
	}
	
	/**
	 * @param author2 the author2 to set
	 */
	public void setAuthor2(Person author2) {
		this.author2 = author2;
	}

	/**
	 * @param prefaceAuthor the prefaceAuthor to set
	 */
	public void setPrefaceAuthor(Person prefaceAuthor) {
		this.prefaceAuthor = prefaceAuthor;
	}
	
	/**
	 * @param afterwordAuthor the afterwordAuthor to set
	 */
	public void setAfterwordAuthor(Person afterwordAuthor) {
		this.afterwordAuthor = afterwordAuthor;
	}
	
	/**
	 * @param translator the translator to set
	 */
	public void setTranslator(Person translator) {
		this.translator = translator;
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
	 * <br>Adds all of b's titles and identifiers to this book.
	 * <br>For authors, authors from book b are added to this book if this book doesn't have them.
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

		if (this.author1 == null) {
			this.author1 = b.getAuthor1();
		}
		if (this.author2 == null) {
			this.author2 = b.getAuthor2();
		}
		if (this.translator == null) {
			this.translator = b.getTranslator();
		}
		if (this.prefaceAuthor == null) {
			this.prefaceAuthor = b.getPrefaceAuthor();
		}
		if (this.afterwordAuthor == null) {
			this.afterwordAuthor = b.getAfterwordAuthor();
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
		return "Book [title=" + title + ", author=" + author1 + ", identifier=" + identifiers + ", totalUnitsSold=" + totalUnitsSold +"]";
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
