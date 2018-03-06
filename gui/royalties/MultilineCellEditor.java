package gui.royalties;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**Custom TableCellEditor which allows user to scroll the JLists in each cell (if content of JList does not fit in cell).
 * <br>Meant to be paired with MultilineCellRenderer.
 * @author crhm
 */
@SuppressWarnings("serial")
public class MultilineCellEditor extends AbstractCellEditor implements TableCellEditor {
	private JScrollPane scrollpane;
	private JList<String> list;
	private String[] content;

	/**Constructs a custom TableCellEditor which allows user to scroll the JLists in each cell.
	 */
	public MultilineCellEditor() {
		super();
		scrollpane = new JScrollPane();
		list = new JList<String>(); 
		scrollpane.getViewport().add(list);
	}

	/**If the value parameter is an array of strings, it changes the JList content to match it.
	 */
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		//Note: returns the component to be updated / repainted / edited
		if (value instanceof String[]) {
			content = (String[]) value;
			list.setListData(content);
		}
		return scrollpane;
	}

	/**Returns the value contained in the editor, which here is  
	 *the content of the last JList to have been 'edited' (in this case selected or scrolled).
	 */
	@Override
	public Object getCellEditorValue() {	
		//Note: is only called to obtain the value of the cell after its 'edition'
		//hence why returning last edited JList content works.
		return content;
	}
}
