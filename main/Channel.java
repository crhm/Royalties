package main;

import java.util.HashMap;

import importing.IFileFormat;

public class Channel {
	
	private IFileFormat fileFormat;
	private final String name;
	private final HashMap<Book, HashMap<Person, IRoyaltyType>> listRoyalties = new HashMap<Book, HashMap<Person, IRoyaltyType>>();

	//TODO print list royalties?
	
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
	
	public HashMap<Book, HashMap<Person, IRoyaltyType>> getListRoyalties() {
		return listRoyalties;
	}

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
