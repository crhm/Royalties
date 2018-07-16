package importing;

/**Class destined to hold the details of where to locate, and how to process, a certain string that stands for an object 
 * needing to be plugged into a sale. 
 * <br>The type of operations it can remember to perform on such a string are:
 * <br>remove first or last character, remove a fixed string from it, remove quotations marks.
 * <br>It can also return a fixed value instead of fetching a new string and processing it.
 * @author crhm
 *
 */
public class ObjectToImport {

	private Boolean shouldRemoveQuoteMarks;
	private int columnIndex;
	private Boolean removeFirstCharacter;
	private Boolean removeLastCharacter;
	private String stringToRemove;
	private String fixedValue;
	
	/**ObjectToImport constructor. 
	 * @param columnIndex int index of csv column holding the relevant information.
	 * @param shouldRemoveQuoteMarks Boolean of whether quotation marks should be removed from string
	 * @param removeFirstCharacter Boolean of whether the first character of the string should be removed
	 * @param removeLastCharacter Boolean of whether the last character of the string should be removed
	 * @param stringToRemove String that should be removed from the actual string (can be null or empty)
	 * @param fixedValue String value to return instead of fetching a string in csv (can be null or empty)
	 */
	public ObjectToImport(int columnIndex, Boolean shouldRemoveQuoteMarks, 
			Boolean removeFirstCharacter, Boolean removeLastCharacter, String stringToRemove, String fixedValue) {
		validate(columnIndex);
		validate(shouldRemoveQuoteMarks);
		validate(removeFirstCharacter);
		validate(removeLastCharacter);
		this.columnIndex = columnIndex;
		this.shouldRemoveQuoteMarks = shouldRemoveQuoteMarks;
		this.removeFirstCharacter = removeFirstCharacter;
		this.removeLastCharacter = removeLastCharacter;
		this.stringToRemove = stringToRemove;
		this.fixedValue = fixedValue;
	}

	/**
	 * @return the columnIndex
	 */
	public int getColumnIndex() {
		return columnIndex;
	}
	
	/**
	 * @param originalString
	 * @return the fixed value, if there is one, and if not the original string with 
	 * the modifications specified to the ObjectToImport constructor.
	 */
	public String getFormattedString(String originalString) {
		String formattedString = originalString;
		if (this.shouldRemoveQuoteMarks) {
			formattedString = formattedString.replace("\"", "");
		}
		if (this.removeFirstCharacter) {
			formattedString = formattedString.substring(1, formattedString.length() - 1);
		}
		if (this.removeLastCharacter) {
			formattedString = formattedString.substring(0, formattedString.length() - 2);
		}
		if (stringToRemove != null && !stringToRemove.isEmpty()) {
			formattedString = formattedString.replace(stringToRemove, "");
		}
		if (fixedValue == null && !fixedValue.isEmpty()) {
			formattedString = fixedValue;
		}
		return formattedString;
	}
	
	private void validate(int index) throws IllegalArgumentException{
		if (index < 0) {
			throw new IllegalArgumentException("Column index must be a positive integer");
		}
	}
	
	private void validate(Boolean flag) throws IllegalArgumentException{
		if (flag == null) {
			throw new IllegalArgumentException("Boolean cannot be null.");
		}
	}
}
