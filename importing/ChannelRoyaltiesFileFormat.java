package importing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import main.Book;
import main.Channel;
import main.ObjectFactory;
import main.SalesHistory;
import main.royalties.IRoyaltyType;
import main.royalties.RoyaltyPercentage;

public class ChannelRoyaltiesFileFormat extends FileFormat implements java.io.Serializable {

	private static final long serialVersionUID = 7189545679878555838L;
	private Channel channel;

	@Override
	public void importData(String filePath) {
		try {
			for (Channel ch : SalesHistory.get().getListChannels().values()) {
				if (filePath.contains(ch.getName())) {
					this.channel = ch;
				}
			}

			//Reads the file
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			StringBuilder lines = new StringBuilder();
			String line = "";
			while (line!= null) {
				line = br.readLine();
				lines.append(line + "\n");
			}
			br.close();

			// Places each line as an element in an array of Strings
			String temp = lines.toString();
			String[] allLines = temp.split("\n");

			//Parses data for each currency and places it in class variable listForex by calling importRoyalties() on each currency line of csv
			//Considers that the first line of currencies is the second line of csv.
			//Stops if counter reaches the total number of lines of csv, or if the line is shorter than 5 characters,
			//so that it doesn't try to parse the summary lines below the last currency line.
			int counter = 1;
			while (counter< allLines.length && allLines[counter].length() > 5) {
				importRoyalties(allLines[counter]);
				counter++;
			}			
		} catch (IOException e) {
			System.out.println("There was a problem importing this file.");
			e.printStackTrace();
		}

	}

	private void importRoyalties(String line) {
		//Divides line into its individual cells by splitting on commas that are not within quotes
		//And trims all leading and trailing whitespace from each value.
		String[] lineDivided = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
		int counter = 0;
		for (String s : lineDivided) {
			lineDivided[counter] = s.trim();
			counter++;
		}

		Book book = obtainBook(lineDivided[0], lineDivided[1]);
		
		int personsIndex = 2; //Because royalties start in 3d column, given first two are book title and id
		//because there are never more than 4 royalty holders, hence never more than 10 columns and last royalty holder should be on 9th column
		while (personsIndex < 9) { //because there are never more than 4 royalty holders, hence never more than 10 columns and last royalty holder should be on 9th
			if (lineDivided[personsIndex].length() > 1) {
				IRoyaltyType royalty = new RoyaltyPercentage(Double.parseDouble(lineDivided[personsIndex + 1]));
				this.channel.addRoyalty(book, lineDivided[personsIndex], royalty);
			}
			personsIndex = personsIndex + 2; //because royalty holders are every two columns
		}
	}
	
	private Book obtainBook(String bookTitle, String identifier) {
		Book book = null; 
		if (SalesHistory.get().getBook(bookTitle)!=null) {
			book = SalesHistory.get().getBook(bookTitle);
			if (!identifier.isEmpty()) {
				book.addIdentifier(identifier);
			}
		} else {
			Boolean newBook = true;
			for (Book b : SalesHistory.get().getListPLPBooks()) {
				if (b.getIdentifiers().contains(identifier)) {
					book = b;
					book.addTitle(bookTitle);
					newBook = false;
				} else if (b.getTitle().toLowerCase().equals(bookTitle.toLowerCase())){
					book = b;
					book.addTitle(bookTitle);
					if (!identifier.isEmpty()) {
						b.addIdentifier(identifier);
					}
					newBook = false;
				}
			}
			if (newBook) {
				book = ObjectFactory.createBook(bookTitle, "", identifier);
			}
		}
		return book;
	}

}
