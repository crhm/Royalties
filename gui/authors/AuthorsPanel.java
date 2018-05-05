package gui.authors;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import main.Person;
import main.SalesHistory;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class AuthorsPanel extends JPanel implements ListSelectionListener {
	private JTable listAuthors;
	
	public AuthorsPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel buttonPanel = new JPanel();
		add(buttonPanel, BorderLayout.NORTH);
		buttonPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		JLabel lblEmpty1 = new JLabel("");
		buttonPanel.add(lblEmpty1);
		
		JLabel lblEmpty2 = new JLabel("");
		buttonPanel.add(lblEmpty2);
		
		JButton btnAddAuthor = new JButton("Add New Author");
		buttonPanel.add(btnAddAuthor);
		
		JButton btnEditAuthor = new JButton("Edit Author");
		buttonPanel.add(btnEditAuthor);
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		listAuthors = getTable();
		listAuthors.getSelectionModel().addListSelectionListener(this);
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
		listAuthors.getSelectionModel().removeListSelectionListener(this);
		TableModel model = getTable().getModel();
		listAuthors.setModel(model);
		setTableSettings(listAuthors);
		listAuthors.getSelectionModel().addListSelectionListener(this);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		//TODO
	}

}
