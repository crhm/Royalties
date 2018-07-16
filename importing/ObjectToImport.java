package importing;

public class ObjectToImport {

	private Boolean shouldRemoveQuoteMarks;
	private int columnIndex;
	private Boolean removeFirstCharacter;
	private Boolean removeLastCharacter;
	private String stringToRemove;
	
	public ObjectToImport(int columnIndex, Boolean shouldRemoveQuoteMarks, 
			Boolean removeFirstCharacter, Boolean removeLastCharacter, String stringToRemove) {
		this.columnIndex = columnIndex;
		this.shouldRemoveQuoteMarks = shouldRemoveQuoteMarks;
		this.removeFirstCharacter = removeFirstCharacter;
		this.removeLastCharacter = removeLastCharacter;
		this.stringToRemove = stringToRemove;
	}

	/**
	 * @return the columnIndex
	 */
	public int getColumnIndex() {
		return columnIndex;
	}
	
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
		return formattedString;
	}

}
