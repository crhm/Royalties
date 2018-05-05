package gui.persons;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import gui.renderers.NumberRenderer;
import main.Person;
import main.SalesHistory;

@SuppressWarnings("serial")
public class PersonsPanel extends JPanel implements ActionListener, ListSelectionListener {
	JTable royaltyHoldersTable;
	JButton addButton;
	JButton editButton;
	JButton deleteButton;
	JButton mergeButton;
	int selectedIndex1 = -1;
	int selectedIndex2 = -1;

	public PersonsPanel() {
		super();
		this.setLayout(new BorderLayout());
		
		//Setting up buttons
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 4, 0, 0));
		
		addButton = new JButton("Create New Person");
		addButton.addActionListener(this);
		buttonPanel.add(addButton);

		editButton = new JButton("Edit Selected Person");
		editButton.setEnabled(false);
		editButton.addActionListener(this);
		buttonPanel.add(editButton);
		
		deleteButton = new JButton("Delete Selected Person");
		deleteButton.setEnabled(false);
		deleteButton.addActionListener(this);
		buttonPanel.add(deleteButton);
		
		mergeButton = new JButton("Merge Together Two Persons Selected");
		mergeButton.setEnabled(false);
		mergeButton.addActionListener(this);
		buttonPanel.add(mergeButton);
		
		this.add(buttonPanel, BorderLayout.NORTH);
		
		//Setting up table
		royaltyHoldersTable = getTable();
		royaltyHoldersTable.getSelectionModel().addListSelectionListener(this);
		this.add(new JScrollPane(royaltyHoldersTable), BorderLayout.CENTER);

	}

	/**Returns the JTable representing royalty holders and their balance.
	 */
	private JTable getTable() {
		//Sets the table model
		String[] columnNames = {"Name", "Balance (USD)", "Person Number"};
		DefaultTableModel model = new DefaultTableModel(getData(), columnNames) {
			@Override
			public Class<?> getColumnClass(int column) {
				switch (column ) {
				case 1 : return Double.class;
				case 2 : return Long.class;
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
		setTableSettings(table);

		return table;
	}

	/**Returns the data to be plugged into the appropriate table model.
	 * <br>Strips person names of quotation marks for cleanliness of presentation.
	 */
	private Object[][] getData(){
		Object[][] data = new Object[SalesHistory.get().getListPersons().size()][3];
		int count = 0;
		for (Person p : SalesHistory.get().getListPersons()) {
			data[count][0] = p.getName();
			data[count][1] = p.getBalance();
			data[count][2] = p.getPersonNumber();
			count++;
		}
		return data;
	}

	/**Ensures that the table's second column is rendered as a currency, that it is sorted by its first column, 
	 * and that the user cannot reorder the columns.
	 * @param table
	 */
	private void setTableSettings(JTable table) {
		//Renders last column as currency yet allows it to be sorted as a double
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(1).setCellRenderer(NumberRenderer.getCurrencyRenderer());
		//Hides person number column from user while keeping it in the table and the table model so data can be retrieved.
		columnModel.removeColumn(columnModel.getColumn(2));

		//Sorts the table by person name
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

	/**Method to call when the underlying data may have changed and the table needs to be updated.
	 */
	public void updateData() {
		TableModel model = getTable().getModel();
		royaltyHoldersTable.setModel(model);
		setTableSettings(royaltyHoldersTable);
	}

	@Override
	public void actionPerformed(ActionEvent e) { 
		if (e.getSource() == mergeButton) { //Merge button is clicked (should only happen when exactly two persons are selected)
			int row1 = royaltyHoldersTable.convertRowIndexToModel(selectedIndex1);
			int row2 = royaltyHoldersTable.convertRowIndexToModel(selectedIndex2);
			long personNumber1 = (long) royaltyHoldersTable.getModel().getValueAt(row1, 2);
			long personNumber2 = (long) royaltyHoldersTable.getModel().getValueAt(row2, 2);
			Person person1 = SalesHistory.get().getPersonWithNumber(personNumber1);
			Person person2 = SalesHistory.get().getPersonWithNumber(personNumber2);
			int userChoice = JOptionPane.showConfirmDialog(null, "Please confirm that you want to merge these two persons.", "Please Confirm", JOptionPane.OK_CANCEL_OPTION);
			if (userChoice == JOptionPane.OK_OPTION) {
				person1.merge(person2);
				updateData();
			}
		} else if (e.getSource() == addButton) { //Create new Person is clicked
			AddPersonDialog dialog = new AddPersonDialog();
			dialog.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) { //update and repaint table on close of addPersonDialog
					updateData();
				}				
			});
		} else if (e.getSource() == editButton) { //Edit selected person is clicked
			int row1 = royaltyHoldersTable.convertRowIndexToModel(selectedIndex1);
			long personNumber1 = (long) royaltyHoldersTable.getModel().getValueAt(row1, 2);
			Person person1 = SalesHistory.get().getPersonWithNumber(personNumber1);

			EditPersonDialog dialog = new EditPersonDialog(person1);
			dialog.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) { //update and repaint table on close of editPersonDialog
					updateData();
				}				
			});
		} else if (e.getSource() == deleteButton) { //Delete selected person is clicked
			int option = JOptionPane.showConfirmDialog(this, "Please confirm you want to delete this person.", "Warning!", 
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			if (option == 0) { //If user clicks OK
				int row1 = royaltyHoldersTable.convertRowIndexToModel(selectedIndex1);
				long personNumber1 = (long) royaltyHoldersTable.getModel().getValueAt(row1, 2);
				Person person1 = SalesHistory.get().getPersonWithNumber(personNumber1);

				SalesHistory.get().removePerson(person1);
				updateData();
			}

		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) { //When there is a change in the selection of persons
		selectedIndex1 = royaltyHoldersTable.getSelectionModel().getMinSelectionIndex();
		selectedIndex2 = royaltyHoldersTable.getSelectionModel().getMaxSelectionIndex();

		if (royaltyHoldersTable.getSelectedRows().length == 2) {
			mergeButton.setEnabled(true);
			editButton.setEnabled(false);
			deleteButton.setEnabled(false);
		} else {
			mergeButton.setEnabled(false);
			if (royaltyHoldersTable.getSelectedRows().length == 1) {
				editButton.setEnabled(true);
				deleteButton.setEnabled(true);
			} else {
				editButton.setEnabled(false);
				deleteButton.setEnabled(false);
			}
		}
	}

}
