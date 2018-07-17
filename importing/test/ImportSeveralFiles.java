package importing.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class ImportSeveralFiles {
	
	public ImportSeveralFiles(String fileListPath) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileListPath));
			StringBuilder lines = new StringBuilder();
			String line = "";
			while (line!= null) {
				line = br.readLine();
				lines.append(line + "\n");
			}
			br.close();
			String temp = lines.toString();
			
			// Places each line as an element in an array of Strings
			String[] allLines = temp.split("\n");
			for (String file : allLines) {
				if (file.length() > 5) {
//					ImportFactory.ImportData("Data/UsuableFormats/" + file.replace("\n", "")); //TODO FIX THIS - It's broken by change to importFactory
					//A lot of tests depend on this class so either change them too or fix this.
				}
			} 
		}	catch (IOException e) {
			// TODO 
			e.printStackTrace();
		}
	}

}
