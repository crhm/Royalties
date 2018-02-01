package main;

/** Class representing books managed by PLP and sold via different channels.
 * <br> A book has a title, an author and an identifier which is unique but whose format
 * depends on the channel through which it was added (usually a variation on ISBN).
 * @author crhm
 *
 */
public class Book {
	
	private final String title;
	private final String author;
	private final String identifier;
	
	/** Book constructor. Initialises title, author and identifier to the corresponding arguments passed by the user.
	 * @param title String title of book
	 * @param author String name of author
	 * @param identifier String unique ID of book, e.g. ISBN, ISBN-13, e-ISBN or ASIN
	 */
	public Book(String title, String author, String identifier) {
		this.title = title;
		this.author = author;
		this.identifier = identifier;
	}
	
	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public String getIdentifier() {
		return identifier;
	}

	@Override
	public String toString() {
		return "Book [title=" + title + ", author=" + author + ", identifier=" + identifier + "]";
	}

}
