package importing;

//TODO Read https://stackoverflow.com/questions/21817816/java-reading-a-file-different-methods

/**This interface is meant to be implemented by all classes that import raw data from a file, sales or FX. 
 * <br>It enforces an importData(String filePath) method which should perform the import from the file to the database.
 * @author crhm
 *
 */
public interface IFileFormat {
	
	/**Imports the data found in the file, whose path (from src folder) + name + extension is the parameter filePath, into the 
	 * database. Performs that action differently depending on the IFileFormat implementation it is called on.
	 * @param filePath
	 */
	public void importData(String filePath);

}
