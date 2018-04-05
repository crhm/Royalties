package importing.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import importing.ImportFactory;

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
					ImportFactory.ImportData("Data/UsuableFormats/" + file.replace("\n", ""));
				}
			} 
		}	catch (IOException e) {
			// TODO 
			e.printStackTrace();
		}
	}

}
