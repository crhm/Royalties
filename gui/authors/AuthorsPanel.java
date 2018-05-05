package gui.authors;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import main.Person;
import main.SalesHistory;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class AuthorsPanel extends JPanel {
	private JTable listAuthors;
	
	public AuthorsPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		listAuthors = getTable();
		scrollPane.setViewportView(listAuthors);
	}
	
	/**Returns the JTable representing the list of authors.
	 */
	private JTable getTable() {
		//Prepares table model
		Object[] columnNames = {"Author Name"};
		DefaultTableModel model = new DefaultTableModel(getData(), columnNames) {
			@Override
			public Class<?> getColumnClass(int column) {
				return String.class;
			}
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		//Sets up table
		JTable table = new JTable(model);
		setTableSettings(table);

		return table;
	}

	/**Returns the data to be plugged into the model for the JTable representing the list of books managed by PLP
	 * Titles and Authors are stripped of potential quotation marks for cleanliness of presentation.
	 */
	private Object[][] getData(){
		Object[][] data = new Object[SalesHistory.get().getListAuthors().size()][1];
		int count = 0;
		for (Person p : SalesHistory.get().getListAuthors()) {
			data[count][0] = p.getName();
			count++;
		}
		return data;
	}
	
	/**Ensures the table passed as argument is in single selection mode, is sorted by its first column, 
	 * and forbids the reordering of columns.
	 * @param table
	 */
	private void setTableSettings(JTable table) {
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		//Sets up sorting
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
		table.setRowSorter(sorter);
		List<RowSorter.SortKey> sortKeys = new ArrayList<>();
		int columnIndexToSort = 0;
		sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));
		sorter.setSortKeys(sortKeys);
		sorter.sort();
		
		//Disables the user-reordering table columns
		table.getTableHeader().setReorderingAllowed(false);
	}

	/**Updates the data in the table of authors
	 */
	public void updateData() {
		TableModel model = getTable().getModel();
		listAuthors.setModel(model);
		setTableSettings(listAuthors);
	}

}
