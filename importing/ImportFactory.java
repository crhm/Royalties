package importing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import gui.importfile.NewImportFormatDialog;
import importing.forex.AmazonForexFileFormat;
import importing.forex.AppleForexFileFormat;
import main.Channel;
import main.SalesHistory;

public class ImportFactory implements java.io.Serializable {

	private static final long serialVersionUID = 8392393437563388498L;

	public static void ImportSales(String fileName) {
		Boolean flagImported = false;
		
		String channelName = getChannelName(fileName);
		if (!channelName.isEmpty()) {
			//TODO user verification of channel it thinks it is?
			flagImported = tryImportForChannel(channelName, fileName);
		}
		//Enters here if getChannelName failed to find one or if the importing (for the channel that was found) did not work.
		if (!flagImported) { 
			NewImportFormatDialog newImportFormatDialog = new NewImportFormatDialog(fileName);
			//TODO figure out a way to report the successful creation of a new format for a given channel and to 
			//return here with the channel name so that the data can be imported.
		}

		if (flagImported) {
			String[] pathSeparated = fileName.split("/");
			SalesHistory.get().addImportedFile(pathSeparated[pathSeparated.length - 1]);
		}
	}

	public static void importFX(String fileName) {
		FileFormat temp = null;
		if (fileName.toLowerCase().contains("apple")) {
			temp = new AppleForexFileFormat();
		} else if (fileName.toLowerCase().contains("amazon") && fileName.toLowerCase().contains("payment")) {
			temp = new AmazonForexFileFormat();
		}
		temp.importData(fileName);
		String[] pathSeparated = fileName.split("/");
		SalesHistory.get().addImportedFile(pathSeparated[pathSeparated.length - 1]);
	}

	private static Boolean tryImportForChannel(String channelName, String fileName) {
		Boolean successfullyImported = false;

		for (SalesFileFormat sff : SalesHistory.get().getChannel(channelName).getListSalesFileFormats()) {
			try {
				sff.importData(fileName);
				successfullyImported = true;
				break; //Exits for loop when it successfully imports the file
			} catch (Exception e) {
			}
		}

		return successfullyImported;
	}
	
	/**Returns the name of the channel the file most likely belongs to, or an empty string if it can't find any that are probable.
	 * <br>First it looks at the file name, and sees if there are any channel Names in there (in their many variations of capitalization), 
	 * and if so it gets the first it finds and assumes that's the channel.
	 * <br>If there aren't any in the fileName, it opens the file and looks for them in each line, one after the other (in order). 
	 * If the file has occurences of channel names, it will assume the first one to be found is the channel name for this file. 
	 * This mirrors the state of things in files as far as I've seen (they are usually on the first line of csv if not in filename, 
	 * and later occurences are to be disregarded).
	 * <br>If there are no instances of any channel names and their variation in the filename and none in the file, it returns an 
	 * empty string.
	 * @param fileName path and name of file to be examined.
	 * @return String channel name or empty String.
	 */
	private static String getChannelName(String fileName) {
		String channelName = "";
		try {
			
			//Making a list of all channel names
			List<String> channelNames = new ArrayList<String>();
			for (Channel ch : SalesHistory.get().getListChannels()) {
				for (String name : ch.getListNames()) {
					channelNames.add(name);
				}
			}
			
			for (String s : channelNames) { //Checks for channel name in fileName and formats it correctly for the getChannel(channelName) method
				if (fileName.contains(s)) {
					channelName = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
					break;
				}
			}
			
			if (channelName.isEmpty()) { //If channel name isn't present in filename, look inside the file
				BufferedReader br = new BufferedReader(new FileReader(fileName));
				StringBuilder lines = new StringBuilder();
				String line = "";
				while (line!= null) {
					line = br.readLine();
					lines.append(line + "\n");
				}
				br.close();
				String temp = lines.toString();
				String[] fileContentPerLine = temp.split("\n"); //An array of the csv lines
				
				for (String s : fileContentPerLine) {
					//For each line in turn, see it contains any of channelNames, and return the first occurence of one in the line if it does.
					 Optional<String> holder = channelNames.parallelStream().filter(s::contains).findFirst();
					 if (holder.isPresent()) { //If there is, 
						 String unformatted = holder.get(); //get it, and format it (next line)
						 channelName = unformatted.substring(0, 1).toUpperCase() + unformatted.substring(1).toLowerCase();
						 break;
					 }
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return channelName;
	}

}
