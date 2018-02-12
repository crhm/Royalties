package main;

import java.util.ArrayList;
import java.util.List;

/** Class representing books managed by PLP and sold via different channels.
 * <br> A book has a title, an author and an identifier which is unique but whose format
 * depends on the channel through which it was added (usually a variation on ISBN).
 * @author crhm
 *
 */
public class Book {
	
	private final String title;
	private String author;
	private final List<String> identifier;
	
	/** Book constructor. Initialises title, author and identifier to the corresponding arguments passed by the user.
	 * @param title String title of book
	 * @param author String name of author
	 * @param identifier String unique ID of book, e.g. ISBN, ISBN-13, e-ISBN or ASIN
	 */
	public Book(String title, String author, String identifier) {
		this.title = title;
		this.author = author;
		this.identifier = new ArrayList<String>();
		this.identifier.add(identifier);
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

	public List<String> getIdentifier() {
		return identifier;
	}
	
	public void addIdentifier(String identifier)	{
		this.identifier.add(identifier);
	}
	
	@Override
	public String toString() {
		return "Book [title=" + title + ", author=" + author + ", identifier=" + identifier + "]";
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
	 * <br>Differences in the identifiers are disregarded.
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
