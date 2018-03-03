package main;

import java.util.ArrayList;
import java.util.List;

/** Class representing books managed by PLP and sold via different channels.
 * <br> A book has a title, an author and an identifier which is unique but whose format
 * depends on the channel through which it was added (usually a variation on ISBN).
 * <br>It also keeps track of how many units of the book have been sold so far.
 * @author crhm
 *
 */
public class Book implements java.io.Serializable{

	private static final long serialVersionUID = 9050853882885242193L;
	private final String title;
	private String author;
	private final List<String> identifiers;
	private double totalUnitsSold;
	
	/**Book constructor.
	 * <br>Removes quote characters from title and author arguments, and then initialises Book variables to them.
	 * <br>Creates an ArrayList of Strings and places identifier in it.
	 * <br>Initialises totalUnitsSold to 0.
	 * @param title String title of book
	 * @param author String name of author
	 * @param identifier String unique ID of book, e.g. ISBN, ISBN-13, e-ISBN or ASIN
	 */
	public Book(String title, String author, String identifier) {
		this.title = title.replace("\"", "");
		this.author = author.replace("\"", "");
		this.identifiers = new ArrayList<String>();
		this.identifiers.add(identifier);
		this.totalUnitsSold = 0;
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

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuthor() {
		return author;
	}

	public List<String> getIdentifiers() {
		return identifiers;
	}

	public void addIdentifier(String identifier)	{
		this.identifiers.add(identifier);
	}

	@Override
	public String toString() {
		return "Book [title=" + title + ", author=" + author + ", identifier=" + identifiers + "totalUnitsSold=" + totalUnitsSold +"]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	/** Returns true if and only if the object is also a Book, with the same title and the same author.
	 * <br>Differences in the identifiers and units sold are disregarded.
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
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

}
