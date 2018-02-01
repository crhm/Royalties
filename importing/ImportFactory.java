package importing;

public class ImportFactory {
	
	public static void ImportData(String fileName) {
		IFileFormat temp = null;
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
