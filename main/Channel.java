package main;

import java.util.HashMap;

import importing.IFileFormat;

/**Class designed to represent the different channels through which PLP sells books, e.g. Apple, Amazon, Nook, Kobo, Createspace...
 * <br>A Channel has a name, an import File Format it is associated with, and a list of royalties, since it can vary per channel for 
 * the same book and the same royalty holder.
 * <br>The list of royalties maps books to a list of persons associated with the type of royalty they hold for that book.
 * <br>The import file Format must be an implementation of the IFileFormat interface, and can be changed at runtime.
 * <br>This class allows the user to add a royalty to the list of royalties through the addRoyalty method.
 * @author crhm
 *
 */
public class Channel {
	
	private IFileFormat fileFormat;
	private final String name;
	private final HashMap<Book, HashMap<Person, IRoyaltyType>> listRoyalties = new HashMap<Book, HashMap<Person, IRoyaltyType>>();

	//TODO print list royalties?
	
	/**Channel constructor. Initialises channel names and fileFormat to the corresponding arguments passed by user.
	 * @param name String name of channel
	 * @param fileFormat IFileFormat implementation to be associated with the channel
	 */
	public Channel(String name, IFileFormat fileFormat) {
		this.name = name;
		this.fileFormat = fileFormat;
	}

	public IFileFormat getfileFormat() {
		return fileFormat;
	}

	public void setfileFormat(IFileFormat fileFormat) {
		this.fileFormat = fileFormat;
	}

	public String getName() {
		return name;
	}
	
	/** Returns the list of royalties of that channel, as a HashMap mapping books to Hashmaps 
	 * which map Persons to the type of Royalty that they hold. Hence each book may have several 
	 * royalty holders with each a different type of royalty.
	 * @return the list of royalties of that channel, as a HashMap mapping books to Hashmaps 
	 * which map Persons to the type of Royalty that they hold.
	 */
	public HashMap<Book, HashMap<Person, IRoyaltyType>> getListRoyalties() {
		return listRoyalties;
	}

	/**Adds a royalty to the list of royalties of this channel.
	 * @param b Book for which the royalty is held
	 * @param royaltyHolder Person which holds the royalty
	 * @param royalty royalty type that is held by the person for this book
	 */
	public void addRoyalty(Book b, Person royaltyHolder, IRoyaltyType royalty) {
		HashMap<Person, IRoyaltyType> listHolder = null;
		if (listRoyalties.containsKey(b)) {
			listHolder = listRoyalties.get(b);
		} else {
			listHolder = new HashMap<Person, IRoyaltyType>();
		}
		listHolder.put(royaltyHolder, royalty);
		this.listRoyalties.put(b, listHolder);
		if (!SalesHistory.get().getListRoyaltyHolders().values().contains(royaltyHolder)) {
			SalesHistory.get().addRoyaltyHolder(royaltyHolder);
		}
	}

	@Override
	public String toString() {
		return "Channel [name=" + name + "]";
	}
	
	

}
