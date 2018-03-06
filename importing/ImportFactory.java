package importing;

import importing.forex.AmazonForexFileFormat;
import importing.forex.AppleForexFileFormat;
import importing.sales.AmazonFileFormat;
import importing.sales.AppleFileFormat;
import importing.sales.CreatespaceFileFormat;
import importing.sales.KoboFileFormat;
import importing.sales.NookFileFormat;

public class ImportFactory implements java.io.Serializable {

	private static final long serialVersionUID = 8392393437563388498L;

	public static void ImportData(String fileName) {
		FileFormat temp = null;
		if (fileName.toLowerCase().contains("kobo")) { //KOBO
			temp = new KoboFileFormat();
		} else if (fileName.toLowerCase().contains("nook")) { //NOOK
			temp = new NookFileFormat();
		} else if (fileName.toLowerCase().contains("createspace")) { //CREATESPACE
			temp = new CreatespaceFileFormat();
		} else if (fileName.toLowerCase().endsWith(".txt")) { //APPLE
			temp = new AppleFileFormat();
		} else if (fileName.toLowerCase().contains("apple")) {
			temp = new AppleForexFileFormat();
		} else if (fileName.toLowerCase().contains("amazon") && fileName.toLowerCase().contains("sales")) { //AMAZON
			temp = new AmazonFileFormat();
		} else if (fileName.toLowerCase().contains("amazon") && fileName.toLowerCase().contains("payment")) {
			temp = new AmazonForexFileFormat();
		}
		temp.importData(fileName);
	}

}
