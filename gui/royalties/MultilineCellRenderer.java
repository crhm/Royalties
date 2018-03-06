package gui.royalties;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**Custom TableCellRenderer which renders cells as a scrollpane containing a JList
 * <br>Note: can render (display) scrollbars but cannot let user interact with them - for that, set an
 * instance of MultilineCellEditor as a custom cellEditor on the same column.
 */
@SuppressWarnings("serial")
public class MultilineCellRenderer extends JScrollPane implements TableCellRenderer {

	private JList<String> list;

	/**Constructs a custom TableCellRenderer which renders cells as a scrollpane containing a JList
	 */
	public MultilineCellRenderer() {
		list = new JList<String>();
		getViewport().add(list);
	}

	/**If the value parameter is an array of strings, it changes the JList content to match it.
	 * Returns itself (a scrollpane containing a JList) to be rendered.
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		//Note: when a cell has been scrolled, value parameter is the return value of MultilineCellEditor.getCellEditorValue()
		if (value instanceof String[]) {
			list.setListData((String[]) value);
		}	
		
		//Makes sure the whole line is highlighted when selected, which still allows for user to select individual items in JList later
		if (isSelected) {
	         setForeground(table.getSelectionForeground());
	         setBackground(table.getSelectionBackground());
	         list.setForeground(table.getSelectionForeground());
	         list.setBackground(table.getSelectionBackground());
	      } else {
	         setForeground(table.getForeground());
	         setBackground(table.getBackground());
	         list.setForeground(table.getForeground());
	         list.setBackground(table.getBackground());
	      }
	
		return this;
	}
}
