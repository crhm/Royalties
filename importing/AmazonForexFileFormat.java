package importing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import main.SalesHistory;

public class AmazonForexFileFormat implements IFileFormat {

	private String monthAndYear = "";
	private HashMap<String, Double> listForex = new HashMap<String, Double>();
	
	@Override
	public void importData(String filePath) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			StringBuilder lines = new StringBuilder();
			String line = "";
			while (line!= null) {
				line = br.readLine();
				lines.append(line + "\n");
			}
			String temp = lines.toString();
			
			// Places each line as an element in an array of Strings
			String[] allLines = temp.split("\n");
			br.close();
			int counter = 1;
			SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat newFormat = new SimpleDateFormat("MMM yyyy");
			Date date = null;
			String[] secondLine = allLines[1].split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
			String oldDate = secondLine[0];
			try {
				date = oldFormat.parse(oldDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.monthAndYear = newFormat.format(date);
			while (counter< allLines.length && allLines[counter].length() > 15) {
				importForex(allLines[counter]);
				counter = counter + 2;
			}
			if (SalesHistory.get().getHistoricalForex().get(monthAndYear) != null) {
				HashMap<String, Double> existingList = SalesHistory.get().getHistoricalForex().get(monthAndYear);
				for (String s : existingList.keySet()) { //To ensure no Forex already existing but missing from this one is deleted.
					listForex.put(s, existingList.get(s));
				}	
			}
			SalesHistory.get().addHistoricalForex(monthAndYear, listForex);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void importForex(String line) {
		String[] lineDivided = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
		double exchangeRate = 0;
		String currency = "";
		exchangeRate = Double.parseDouble(lineDivided[15]) / Double.parseDouble(lineDivided[8]);
		currency = lineDivided[7];
		this.listForex.put(currency, exchangeRate);
	}
}
