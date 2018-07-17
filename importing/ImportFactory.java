package importing;

import importing.forex.AmazonForexFileFormat;
import importing.forex.AppleForexFileFormat;
import main.SalesHistory;

public class ImportFactory implements java.io.Serializable {

	private static final long serialVersionUID = 8392393437563388498L;

	//TODO Improve this to be less crude & more flexible (maybe by letting user distinguish whether he is selecting sales files or 
	//FX files etc...

	public static void ImportSales(String fileName) {
		Boolean flagImported = false;
		
		if (fileName.toLowerCase().contains("kobo")) { //KOBO
			for (SalesFileFormat sff : SalesHistory.get().getChannel("Kobo").getListSalesFileFormats()) {
				try {
					sff.importData(fileName);
					flagImported = true;
					break;
				} catch (Exception e) {
				}
			}
		} else if (fileName.toLowerCase().contains("nook")) { //NOOK
			for (SalesFileFormat sff : SalesHistory.get().getChannel("Nook").getListSalesFileFormats()) {
				try {
					sff.importData(fileName);
					flagImported = true;
					break;
				} catch (Exception e) {
				}
			}
		} else if (fileName.toLowerCase().contains("createspace")) { //CREATESPACE
			for (SalesFileFormat sff : SalesHistory.get().getChannel("Createspace").getListSalesFileFormats()) {
				try {
					sff.importData(fileName);
					flagImported = true;
					break;
				} catch (Exception e) {
				}
			}
		} else if (fileName.toLowerCase().endsWith(".txt")) { //APPLE
			for (SalesFileFormat sff : SalesHistory.get().getChannel("Apple").getListSalesFileFormats()) {
				try {
					sff.importData(fileName);
					flagImported = true;
					break;
				} catch (Exception e) {
				}
			}

		} else if (fileName.toLowerCase().contains("amazon") && fileName.toLowerCase().contains("sales")) { //AMAZON
			for (SalesFileFormat sff : SalesHistory.get().getChannel("Amazon").getListSalesFileFormats()) {
				try {
					sff.importData(fileName);
					flagImported = true;
					break;
				} catch (Exception e) {
				}
			}
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

}
