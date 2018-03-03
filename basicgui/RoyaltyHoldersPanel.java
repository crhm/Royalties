package basicgui;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import main.Person;
import main.SalesHistory;

/**A JPanel that contains a JTable representing the persons that have a royalty on one or more of the books managed by PLP, 
 * as well as their balance towards PLP. 
 * <br>The JTable is sorted by person name, and sortable, but not editable.
 * @author crhm
 *
 */
@SuppressWarnings("serial")
public class RoyaltyHoldersPanel extends JPanel {

	public RoyaltyHoldersPanel() {
		super();
		this.setLayout(new GridLayout());
		this.add(new JScrollPane(getTable()));

	}

	/**Returns the JTable representing royalty holders and their balance.
	 */
	private JTable getTable() {
		//Sets the table model
		String[] columnNames = {"Name", "Balance (USD)"};
		DefaultTableModel model = new DefaultTableModel(getData(), columnNames) {
			@Override
			public Class<?> getColumnClass(int column) {
				switch (column ) {
				case 1 : return Double.class;
				default : return String.class;
				}
			}
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		//Sets the table
		JTable table = new JTable(model);

		//Sorts the table by person name
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
		table.setRowSorter(sorter);
		List<RowSorter.SortKey> sortKeys = new ArrayList<>();
		int columnIndexToSort = 0;
		sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));
		sorter.setSortKeys(sortKeys);
		sorter.sort();

		return table;
	}

	/**Returns the data to be plugged into the appropriate table model.
	 * <br>Strips person names of quotation marks for cleanliness of presentation.
	 */
	private Object[][] getData(){
		Object[][] data = new Object[SalesHistory.get().getListRoyaltyHolders().values().size()][2];
		int count = 0;
		for (Person p : SalesHistory.get().getListRoyaltyHolders().values()) {
			data[count][0] = p.getName();
			data[count][1] = p.getBalance();
			count++;
		}
		return data;
	}

}
