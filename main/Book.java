package main;

public class Book {
	
	private final String title;
	private final String author;
	private final String identifier;
	
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
