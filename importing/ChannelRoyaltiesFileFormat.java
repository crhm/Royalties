package importing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import main.Book;
import main.Channel;
import main.IRoyaltyType;
import main.Person;
import main.RoyaltyPercentage;
import main.SalesHistory;

public class ChannelRoyaltiesFileFormat implements IFileFormat {
	
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
		
		Book book = null; 
		if (SalesHistory.get().getListPLPBooks().get(lineDivided[0])!=null) {
			book = SalesHistory.get().getListPLPBooks().get(lineDivided[0]);
		} else {
			book = new Book(lineDivided[0], "", lineDivided[1]);
			SalesHistory.get().addBook(book);
		}
		int personsIndex = 2;
		while (personsIndex < 9) {
			if (lineDivided[personsIndex].length() > 1) {
				Person person = null;
				if (SalesHistory.get().getListRoyaltyHolders().get(lineDivided[personsIndex])!=null) {
					person = SalesHistory.get().getListRoyaltyHolders().get(lineDivided[personsIndex]);
				} else {
					person = new Person(lineDivided[personsIndex]);
				}
				IRoyaltyType royalty = new RoyaltyPercentage(Double.parseDouble(lineDivided[personsIndex + 1]));
				this.channel.addRoyalty(book, person, royalty);
				
			}
			personsIndex = personsIndex + 2;
		}
	}

}
