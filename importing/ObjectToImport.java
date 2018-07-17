package importing;

/**Class destined to hold the details of where to locate, and how to process, a certain string that stands for an object 
 * needing to be plugged into a sale. 
 * <br>The type of operations it can remember to perform on such a string are:
 * <br>remove first or last character, remove a fixed string from it, remove quotations marks.
 * <br>It can also return a fixed value instead of fetching a new string and processing it.
 * <br>It can also return a default value if the string equals some other string, for example if string = amazon.com, then return "US".
 * <br>For operations that need the user to specify a string, use the setter methods not the constructor.
 * @author crhm
 *
 */
public class ObjectToImport {

	private Boolean shouldRemoveQuoteMarks = false;
	private int columnIndex;
	private Boolean removeFirstCharacter = false;
	private Boolean removeLastCharacter = false;
	private String stringToRemove = "";
	private String fixedValue = "";
	private String defaultValue = "";
	private String defaultValueCondition = "";
	
	/**ObjectToImport Constructor. Leaves all variables as initialised and only passes a column index.
	 * 
	 * @param columnIndex
	 * @throws IllegalArgumentException if columnindex is not a positive integer
	 */
	public ObjectToImport(int columnIndex) {
		validate(columnIndex);
		this.columnIndex = columnIndex;
	}
	
	/**ObjectToImport constructor. 
	 * @param columnIndex int index of csv column holding the relevant information.
	 * @param shouldRemoveQuoteMarks Boolean of whether quotation marks should be removed from string
	 * @param removeFirstCharacter Boolean of whether the first character of the string should be removed
	 * @param removeLastCharacter Boolean of whether the last character of the string should be removed
	 * @throws IllegalArgumentException if booleans are null or if columnIndex is not a positive integer
	 */
	public ObjectToImport(int columnIndex, Boolean shouldRemoveQuoteMarks, Boolean removeFirstCharacter, Boolean removeLastCharacter) {
		validate(columnIndex);
		validate(shouldRemoveQuoteMarks);
		validate(removeFirstCharacter);
		validate(removeLastCharacter);
		this.columnIndex = columnIndex;
		this.shouldRemoveQuoteMarks = shouldRemoveQuoteMarks;
		this.removeFirstCharacter = removeFirstCharacter;
		this.removeLastCharacter = removeLastCharacter;
	}

	/**
	 * @return the columnIndex
	 */
	public int getColumnIndex() {
		return columnIndex;
	}
	
	/**
	 * @param shouldRemoveQuoteMarks the shouldRemoveQuoteMarks to set
	 */
	public void setShouldRemoveQuoteMarks(Boolean shouldRemoveQuoteMarks) {
		this.shouldRemoveQuoteMarks = shouldRemoveQuoteMarks;
	}

	/**
	 * @param removeFirstCharacter the removeFirstCharacter to set
	 */
	public void setRemoveFirstCharacter(Boolean removeFirstCharacter) {
		this.removeFirstCharacter = removeFirstCharacter;
	}

	/**
	 * @param removeLastCharacter the removeLastCharacter to set
	 */
	public void setRemoveLastCharacter(Boolean removeLastCharacter) {
		this.removeLastCharacter = removeLastCharacter;
	}

	/**
	 * @param stringToRemove the stringToRemove to set
	 */
	public void setStringToRemove(String stringToRemove) {
		this.stringToRemove = stringToRemove;
	}

	/**
	 * @param fixedValue the fixedValue to set
	 */
	public void setFixedValue(String fixedValue) {
		this.fixedValue = fixedValue;
	}

	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @param defaultValueCondition the defaultValueCondition to set
	 */
	public void setDefaultValueCondition(String defaultValueCondition) {
		this.defaultValueCondition = defaultValueCondition;
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
		if (defaultValue != null && !defaultValue.isEmpty() && defaultValueCondition != null && !defaultValueCondition.isEmpty()) {
			if (formattedString.equals(defaultValueCondition)) {
				formattedString = defaultValue;
			}
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
